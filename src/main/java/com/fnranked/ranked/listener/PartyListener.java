package com.fnranked.ranked.listener;

import com.fnranked.ranked.jpa.entities.Party;
import com.fnranked.ranked.jpa.entities.Player;
import com.fnranked.ranked.jpa.repo.PartyInviteRepository;
import com.fnranked.ranked.jpa.repo.PartyRepository;
import com.fnranked.ranked.jpa.repo.PlayerRepository;
import com.fnranked.ranked.messages.MessageUtils;
import com.fnranked.ranked.teams.PartyInviteUtils;
import com.fnranked.ranked.util.JDAContainer;
import com.fnranked.ranked.util.PartyBuilder;
import com.fnranked.ranked.util.PartyUtils;
import com.fnranked.ranked.util.UserUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

@Component
public class PartyListener extends ListenerAdapter {

    private Logger logger = LoggerFactory.getLogger(PartyListener.class);

    @Autowired
    PartyInviteUtils partyInviteUtils;
    @Autowired
    UserUtils userUtils;
    @Autowired
    PartyBuilder partyBuilder;

    @Autowired
    PartyInviteRepository partyInviteRepository;

    @Autowired
    PartyUtils partyUtils;

    @Autowired
    JDAContainer jdaContainer;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    MessageUtils messageUtils;

    @Autowired
    PartyRepository partyRepository;

    @Value("${fnranked.channels.party}")
    public long partyChannelId;
    @Value("${emote.error}")
    private long errorEmoteId;
    @Value("${emote.success}")
    private long successEmoteId;

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (event.getChannel().getIdLong() == partyChannelId) {
            if (event.getMessage().getContentRaw().startsWith("invite")) {
                Party party = partyBuilder.getOrCreatePartyWithPlayer(event.getAuthor());
                long inviter = event.getAuthor().getIdLong();
                if (event.getMessage().getMentionedUsers().isEmpty()) {
                    messageUtils.sendErrorMessage(event.getAuthor().getIdLong(), "Please mention the user you would like to invite.");
                } else {
                    long invitee = event.getMessage().getMentionedUsers().get(0).getIdLong();
                    if (partyBuilder.isUserInviteable(invitee, party)) {
                        partyInviteUtils.createPartyInvite(party, inviter, invitee);
                    } else {
                        event.getAuthor().openPrivateChannel().flatMap(pc ->
                                pc.sendMessage("<:PepeHands:712672036178362418> You can't invite this user because they are either not registered or have invites disabled.")
                        ).queue();
                    }
                }
            } else if (event.getMessage().getContentRaw().startsWith("kick")) {
                if (event.getMessage().getMentionedUsers().isEmpty()) {
                    messageUtils.sendErrorMessage(event.getAuthor().getIdLong(), "Please mention the user you would like to kick.");
                } else {
                    partyBuilder.attemptKick(event.getAuthor().getIdLong(), event.getMessage().getMentionedUsers().get(0).getIdLong());
                }
            } else if (event.getMessage().getContentRaw().startsWith("promote")) {
                if (event.getMessage().getMentionedUsers().isEmpty()) {
                    messageUtils.sendErrorMessage(event.getAuthor().getIdLong(), "Please mention the user you would like to promote.");
                } else {
                    partyBuilder.attemptPromote(event.getAuthor().getIdLong(), event.getMessage().getMentionedUsers().get(0).getIdLong());
                }
            }

        }
    }

    /*
    emoji U+2139U+fe0f = information_source
    emoji U+1f6aa = door
    emoji U+1f507 = mute
    emoji U+1f508 = speaker
     */
    @Override
    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        if (Objects.requireNonNull(event.getUser()).isBot()) return;
        if (event.getChannel().getIdLong() != partyChannelId) return;
        Optional<Player> playerOptional = playerRepository.findById(event.getUserIdLong());
        if (playerOptional.isEmpty()) {
            logger.error(String.format("User <@%s> was able to react in party channel, but was not found as a registered user.", event.getUserIdLong()));
            messageUtils.sendErrorMessage(event.getUserIdLong(), "Failed to register party command, you were not found as a registered user.");
            return;
        }
        if (event.getReactionEmote().isEmoji()) {
            Optional<Party> partyOptional;
            Player player = playerOptional.get();
            switch (event.getReactionEmote().getAsCodepoints()) {
                case "U+2139U+fe0f":
                    partyUtils.displayPartyForPlayer(player);
                    break;
                case "U+1f6aa":
                    partyOptional = partyRepository.findByPlayerListContaining(playerOptional.get());
                    if (partyOptional.isPresent()) {
                        Party party = partyOptional.get();
                        if (party.getCaptain().equals(player)) {
                            //TODO: confirmation to disband?
                            partyRepository.delete(party);
                            messageUtils.sendSuccessMessage(event.getUserIdLong(), "Your party was disbanded.");

                        } else {
                            partyOptional.get().removePlayer(playerOptional.get());
                            messageUtils.sendSuccessMessage(event.getUserIdLong(), "Successfully left party");
                        }
                    } else {
                        messageUtils.sendErrorMessage(event.getUserIdLong(), "You are not currently in a party.");
                    }

                    break;
                case "U+1f507":
                    player.setDuoInvitesEnabled(false);
                    playerRepository.save(player);
                    messageUtils.sendSuccessMessage(event.getUserIdLong(), "Successfully disabled party invites!");

                    break;
                case "U+1f508":
                    player.setDuoInvitesEnabled(true);
                    playerRepository.save(player);
                    messageUtils.sendSuccessMessage(event.getUserIdLong(), "Successfully enabled party invites!");

                    break;
                default:
                    logger.info(String.format("Unexpected reaction in party channel - emoji %s", event.getReactionEmote().getAsCodepoints()));
            }
        } else {
            logger.info(String.format("Unexpected reaction in party channel - emote %s", event.getReactionEmote().getAsCodepoints()));
        }
        partyInviteRepository.findByMessageId(event.getMessageIdLong()).ifPresent(invite -> {
            long emoteId = event.getReactionEmote().getEmote().getIdLong();
            if (emoteId == errorEmoteId) {
                partyInviteUtils.declineInvite(invite, false);
            } else if (emoteId == successEmoteId) {
                partyInviteUtils.acceptInvite(invite);
            }
            event.getChannel().deleteMessageById(event.getMessageId()).queue();
        });
    }

    @Override
    public void onPrivateMessageReactionAdd(@Nonnull PrivateMessageReactionAddEvent event) {
        if (Objects.requireNonNull(event.getUser()).isBot()) return;
        partyInviteRepository.findByMessageId(event.getMessageIdLong()).ifPresent(invite -> {
            if (event.getReactionEmote().isEmoji()) {
                partyInviteUtils.declineInvite(invite, true);
                return;
            }
            long emoteId = event.getReactionEmote().getEmote().getIdLong();
            if (emoteId == errorEmoteId) {
                partyInviteUtils.declineInvite(invite, false);
            } else if (emoteId == successEmoteId) {
                partyInviteUtils.acceptInvite(invite);
            }
        });
    }
}
