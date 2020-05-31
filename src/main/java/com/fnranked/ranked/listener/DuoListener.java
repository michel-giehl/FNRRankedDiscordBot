package com.fnranked.ranked.listener;

import com.fnranked.ranked.jpa.repo.DuoInviteRepository;
import com.fnranked.ranked.teams.DuoInviteUtils;
import com.fnranked.ranked.util.UserUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class DuoListener extends ListenerAdapter {

    @Autowired
    DuoInviteUtils duoInviteUtils;
    @Autowired
    UserUtils userUtils;
    @Autowired
    DuoInviteRepository duoInviteRepository;

    @Value("${fnranked.channels.duo}")
    public long DUO_SETTINGS_CHANNEL_ID;
    @Value("${emote.error}")
    private long errorEmoteId;
    @Value("${emote.success}")
    private long successEmoteId;

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if(event.getChannel().getIdLong() == DUO_SETTINGS_CHANNEL_ID) {
            System.out.println("message in duo channel");
            long inviter = event.getAuthor().getIdLong();
            //TODO UserFinder
            long invitee = event.getMessage().getMentionedUsers().get(0).getIdLong();
            if(duoInviteUtils.canCreateDuoInvite(inviter, invitee)) {
                duoInviteUtils.createDuoInvite(inviter, invitee);
            } else {
                event.getAuthor().openPrivateChannel().flatMap(pc ->
                    pc.sendMessage("<:PepeHands:712672036178362418> You can't create a duo because someone already has a duo partner or a pending invite.")
                ).queue();
            }
        }
    }

    @Override
    public void onPrivateMessageReactionAdd(@Nonnull PrivateMessageReactionAddEvent event) {
        if(event.getUser().isBot()) return;
        duoInviteRepository.findByMessageId(event.getMessageIdLong()).ifPresent(invite -> {
            if(event.getReactionEmote().isEmoji()) {
                duoInviteUtils.declineInvite(invite, true);
                return;
            }
            long emoteId = event.getReactionEmote().getEmote().getIdLong();
            if(emoteId == errorEmoteId) {
                duoInviteUtils.declineInvite(invite, false);
            } else if(emoteId == successEmoteId) {
                duoInviteUtils.acceptInvite(invite);
            }
        });
    }
}
