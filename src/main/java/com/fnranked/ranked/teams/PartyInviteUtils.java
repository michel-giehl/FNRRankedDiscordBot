package com.fnranked.ranked.teams;

import com.fnranked.ranked.jpa.entities.Party;
import com.fnranked.ranked.jpa.entities.PartyInvite;
import com.fnranked.ranked.jpa.entities.Player;
import com.fnranked.ranked.jpa.repo.PartyInviteRepository;
import com.fnranked.ranked.jpa.repo.PartyRepository;
import com.fnranked.ranked.jpa.repo.PlayerRepository;
import com.fnranked.ranked.jpa.repo.TeamRepository;
import com.fnranked.ranked.messages.MessageUtils;
import com.fnranked.ranked.util.JDAContainer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Component
@EnableScheduling
public class PartyInviteUtils {

    private final Logger logger = LoggerFactory.getLogger(PartyInviteUtils.class);

    @Autowired
    PartyInviteRepository partyInviteRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PartyRepository partyRepository;

    @Autowired
    TeamUtils teamUtils;
    @Autowired
    MessageUtils messageUtils;
    @Autowired
    JDAContainer jdaContainer;
    @Autowired
    TeamRepository teamRepository;

    public static final long INVITE_EXPIRE_TIME = 30_000L;

    @Scheduled(initialDelay = 15000, fixedRate = 30_000)
    @Transactional
    public void checkTTL() {
        JDA jda = jdaContainer.getJda();
        if (jda == null) return;
        Timestamp deleteAllAfter = Timestamp.from(Instant.now().minusMillis(INVITE_EXPIRE_TIME));
        Collection<PartyInvite> partyInviteList = partyInviteRepository.findAllByTimeBefore(deleteAllAfter);

        for (PartyInvite partyInvite : partyInviteList) {
            partyInviteRepository.delete(partyInvite);
            User invitee = jda.getUserById(partyInvite.getInviteeId());
            messageUtils.sendErrorMessage(partyInvite.getInviteeId(), "Duo invite timed out.");
            if (invitee == null) return;
            messageUtils.sendErrorMessage(partyInvite.getInviterId(), invitee.getAsTag() + " did not respond to your invite");
        }
    }

    /**
     * Creates duo invite entity and destroys it if it still exists after ttl ran out.
     * Do not actually call this. Use @link {this#createDuoInvite}
     */
    @Transactional
    public void createInviteEntity(Party party, long inviterId, long inviteeId, long messageId) {
        PartyInvite partyInvite = new PartyInvite(party, inviterId, inviteeId, messageId);
        partyInviteRepository.save(partyInvite);
    }

    /**
     * Decline a duo invite
     */
    @Transactional
    public void declineInvite(PartyInvite partyInvite, boolean declinedAndDisabled) {
        partyInviteRepository.delete(partyInvite);
        User invitee = jdaContainer.getJda().getUserById(partyInvite.getInviteeId());
        messageUtils.sendErrorMessage(partyInvite.getInviterId(), invitee.getAsTag() + " declined your party invite");
        if (declinedAndDisabled) {
            messageUtils.sendSuccessMessage(partyInvite.getInviteeId(), "From now on you will not receive any duo invites.");
        } else {
            messageUtils.sendSuccessMessage(partyInvite.getInviteeId(), "Invite declined.");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void acceptInvite(PartyInvite partyInvite) {
        partyInviteRepository.delete(partyInvite);
        Emote pepeD = jdaContainer.getJda().getEmoteById(712672033263058979L);
        Party party = partyInvite.getParty();
        Optional<Player> playerOptional = playerRepository.findById(partyInvite.getInviteeId());
        if (playerOptional.isPresent()) {
            party.addPlayer(playerOptional.get());
            partyRepository.save(party);
            User captain = jdaContainer.getJda().getUserById(party.getId());
            User invitee = jdaContainer.getJda().getUserById(partyInvite.getInviteeId());
            if (pepeD == null || captain == null || invitee == null) return;
            captain.openPrivateChannel().flatMap(pc -> pc.sendMessage(pepeD.getAsMention() + " " + invitee.getAsTag() + " has joined the party")).queue();
            invitee.openPrivateChannel().flatMap(pc -> pc.sendMessage(pepeD.getAsMention() + " You have successfully joined " + captain.getAsTag() + "'s party")).queue();
        } else {
            logger.error("Someone accepted a party invite but couldn't be found in the player database.");
        }

    }

    /**
     * Sends party invite message and calls function to create party invite
     * Also sends the timeout message if invite was neither accepted nor denied.
     *
     * @param inviterId person who sent invite
     * @param inviteeId person who received invite
     */
    @Transactional
    public void createPartyInvite(Party party, long inviterId, long inviteeId) {
        //get users & build message
        JDA jda = jdaContainer.getJda();
        User inviter = jda.getUserById(inviterId);
        User invitee = jda.getUserById(inviteeId);
        if (inviter == null) return;
        if (invitee == null) return;
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(messageUtils.getFnRankedColor());
        eb.setTitle(String.format(":people_holding_hands: %s  has invited you to %s's party :people_holding_hands:", inviter.getAsTag(), Objects.requireNonNull(jda.getUserById(party.getId())).getAsTag()));
        eb.setDescription(String.format(
                "%s to accept the invite" +
                        "\n%s to decline the invite" +
                        "\n%s to decline and disable future duo invites"
                , messageUtils.getSuccessEmote().getAsMention(), messageUtils.getErrorEmote().getAsMention(), ":mute:"));
        //Send message, create invite, send error message if invite was ignored.
        invitee.openPrivateChannel().flatMap(pc -> pc.sendMessage(eb.build())).queue(msg -> {
            createInviteEntity(party, inviterId, inviteeId, msg.getIdLong());
            msg.addReaction(messageUtils.getSuccessEmote()).queue();
            msg.addReaction(messageUtils.getErrorEmote()).queue();
            msg.addReaction("\uD83D\uDD07").queue();
        });
    }
}
