package com.fnranked.ranked.listener;

import com.fnranked.ranked.jpa.entities.Party;
import com.fnranked.ranked.jpa.repo.PartyInviteRepository;
import com.fnranked.ranked.jpa.repo.PartyRepository;
import com.fnranked.ranked.teams.PartyInviteUtils;
import com.fnranked.ranked.util.PartyBuilder;
import com.fnranked.ranked.util.UserUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

@Component
public class PartyListener extends ListenerAdapter {

    @Autowired
    PartyInviteUtils partyInviteUtils;
    @Autowired
    UserUtils userUtils;
    @Autowired
    PartyBuilder partyBuilder;

    @Autowired
    PartyInviteRepository partyInviteRepository;

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
            System.out.println("message in party channel");
            if (event.getMessage().getContentRaw().startsWith("invite")) {
                Party party = partyBuilder.getOrCreatePartyWithPlayer(event.getAuthor());
                long inviter = event.getAuthor().getIdLong();
                //TODO UserFinder
                long invitee = event.getMessage().getMentionedUsers().get(0).getIdLong();
                if (partyBuilder.isUserInviteable(invitee)) {
                    partyInviteUtils.createPartyInvite(party, inviter, invitee);
                } else {
                    event.getAuthor().openPrivateChannel().flatMap(pc ->
                            pc.sendMessage("<:PepeHands:712672036178362418> You can't invite this user because they are either not registered or have invites disabled.")
                    ).queue();
                }
            } else if (event.getMessage().getContentRaw().startsWith("kick")) {
                Optional<Party> partyOptional = partyRepository.findById(event.getAuthor().getIdLong());
                if (partyOptional.isPresent()) {
                    Party party = partyOptional.get();
                    boolean result = party.removePlayerById(event.getMessage().getMentionedUsers().get(0).getIdLong());
                    if (result) {
                        partyRepository.save(party);
                        event.getAuthor().openPrivateChannel().flatMap(pc ->
                                pc.sendMessage("Invite sent!")
                        ).queue();
                    } else {
                        event.getAuthor().openPrivateChannel().flatMap(pc ->
                                pc.sendMessage("<:PepeHands:712672036178362418> You can't invite this user because they are either not registered or have invites disabled.")
                        ).queue();
                    }
                }

            }

        }
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
