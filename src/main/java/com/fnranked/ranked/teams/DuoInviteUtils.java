package com.fnranked.ranked.teams;

import com.fnranked.ranked.jpa.entities.DuoInvite;
import com.fnranked.ranked.jpa.repo.DuoInviteRepository;
import com.fnranked.ranked.jpa.repo.TeamRepository;
import com.fnranked.ranked.messages.MessageUtils;
import com.fnranked.ranked.util.JDAContainer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;

@Component
@EnableScheduling
public class DuoInviteUtils {

    @Autowired
    DuoInviteRepository duoInviteRepository;
    @Autowired
    TeamUtils teamUtils;
    @Autowired
    MessageUtils messageUtils;
    @Autowired
    JDAContainer jdaContainer;
    @Autowired
    TeamRepository teamRepository;

    public final long INVITE_TTL = 30_000L;

    @Scheduled(initialDelay = 15000, fixedRate = 30_000)
    @Transactional
    public void checkTTL() {
        JDA jda = jdaContainer.getJda();
        if(jda == null) return;
        Timestamp deleteAllAfter = Timestamp.from(Instant.now().minusMillis(INVITE_TTL));
        duoInviteRepository.findAllByTimeBefore(deleteAllAfter).iterator().forEachRemaining(duoInvite -> {
            duoInviteRepository.delete(duoInvite);
            User invitee = jda.getUserById(duoInvite.getInviteeId());
            messageUtils.sendErrorMessage(duoInvite.getInviteeId(), "Duo invite timed out.");
            if(invitee == null) return;
            messageUtils.sendErrorMessage(duoInvite.getInviterId(), invitee.getAsTag() + " did not respond to your invite");
        });
    }

    /**
     * whether or not inviter can send a duo invite to invitee
     * @param inviterId id of inviting player
     * @param inviteeId id of player who is invited
     * @return whether invite is possible
     */
    @Transactional
    public boolean canCreateDuoInvite(long inviterId, long inviteeId) {
        return teamRepository.findByPlayerListContainingAndSizeAndActiveIsTrue(teamUtils.getPlayer(inviteeId), 2).isEmpty() &&
                teamRepository.findByPlayerListContainingAndSizeAndActiveIsTrue(teamUtils.getPlayer(inviteeId), 2).isEmpty() &&
                duoInviteRepository.findByInviteeId(inviteeId).isEmpty() &&
                duoInviteRepository.findByInviterId(inviterId).isEmpty() &&
                duoInviteRepository.findByInviterId(inviteeId).isEmpty() &&
                duoInviteRepository.findByInviteeId(inviterId).isEmpty();
    }

    /**
     * Creates duo invite entity and destroys it if it still exists after ttl ran out.
     * Do not actually call this. Use @link {this#createDuoInvite}
     */
    @Transactional
    public void createInviteEntity(long inviterId, long inviteeId, long messageId) {
        DuoInvite duoInvite = new DuoInvite(inviterId, inviteeId, messageId);
        duoInviteRepository.save(duoInvite);
    }

    /**
     * Decline a duo invite
     */
    @Transactional
    public void declineInvite(DuoInvite duoInvite, boolean declinedAndDisabled) {
        duoInviteRepository.delete(duoInvite);
        User invitee = jdaContainer.getJda().getUserById(duoInvite.getInviteeId());
        messageUtils.sendErrorMessage(duoInvite.getInviterId(), invitee.getAsTag() + " declined your duo invite");
        if (declinedAndDisabled) {
            messageUtils.sendSuccessMessage(duoInvite.getInviteeId(), "From now on you will not receive any duo invites.");
        } else {
            messageUtils.sendSuccessMessage(duoInvite.getInviteeId(), "Invite declined.");
        }
    }

    @Transactional
    public void disableInvites(long userId) {

    }

    @Transactional
    public void acceptInvite(DuoInvite duoInvite) {
        duoInviteRepository.delete(duoInvite);
        teamUtils.createTeam(duoInvite.getInviterId(), duoInvite.getInviteeId());
        Emote pepeD = jdaContainer.getJda().getEmoteById(712672033263058979L);
        User inviter = jdaContainer.getJda().getUserById(duoInvite.getInviterId());
        User invitee = jdaContainer.getJda().getUserById(duoInvite.getInviteeId());
        if(pepeD == null || inviter == null || invitee == null) return;
        inviter.openPrivateChannel().flatMap(pc -> pc.sendMessage(pepeD.getAsMention() + " " + invitee.getAsTag() + " is now your duo partner")).queue();
        invitee.openPrivateChannel().flatMap(pc -> pc.sendMessage(pepeD.getAsMention() + " " + inviter.getAsTag() + " is now your duo partner")).queue();
    }

    /**
     * Sends duo invite message and calls function to create duo invite
     * Also sends the timeout message if invite was neither accepted nor denied.
     * @param inviterId person who sent invite
     * @param inviteeId person who received invite
     */
    public void createDuoInvite(long inviterId, long inviteeId) {
        //get users & build message
        JDA jda = jdaContainer.getJda();
        User inviter = jda.getUserById(inviterId);
        User invitee = jda.getUserById(inviteeId);
        if(inviter == null) return;
        if(invitee == null) return;
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(messageUtils.getFnRankedColor());
        eb.setTitle(":people_holding_hands: " + inviter.getAsTag() + " wants to be your partner :people_holding_hands:");
        eb.setDescription(String.format(
                "%s to accept the invite" +
                        "\n%s to decline the invite" +
                        "\n%s to decline and disable future duo invites"
                , messageUtils.getSuccessEmote().getAsMention(), messageUtils.getErrorEmote().getAsMention(), ":mute:"));
        //Send message, create invite, send error message if invite was ignored.
        invitee.openPrivateChannel().flatMap(pc -> pc.sendMessage(eb.build())).queue(msg -> {
            createInviteEntity(inviterId, inviteeId, msg.getIdLong());
            msg.addReaction(messageUtils.getSuccessEmote()).queue();
            msg.addReaction(messageUtils.getErrorEmote()).queue();
            msg.addReaction("\uD83D\uDD07").queue();
        });
    }
}
