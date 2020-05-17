package com.fnranked.ranked.listener;

import com.fnranked.ranked.api.entities.Region;
import com.fnranked.ranked.jpa.entities.Player;
import com.fnranked.ranked.messages.MessageUtils;
import com.fnranked.ranked.util.UserUtils;
import com.fnranked.ranked.jpa.repo.*;
import com.fnranked.ranked.util.TeamUtils;
import com.fnranked.ranked.matchmaking.QueueChanger;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;

@Component
public class QueueListener extends ListenerAdapter {

    @Autowired
    QueueRepository queueRepository;
    @Autowired
    QueueMessageRepository queueMessageRepository;
    @Autowired
    TeamUtils teamUtils;
    @Autowired
    MatchTypeRepository matchTypeRepository;
    @Autowired
    MatchTempRepository matchTempRepository;
    @Autowired
    QueueChanger queueChanger;
    @Autowired
    MessageUtils messageUtils;
    @Autowired
    UserUtils userUtils;
    @Autowired
    QueuedTeamRepository queuedTeamRepository;

    @Transactional
    @Override
    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        if(event.getUser().isBot()) return;
        queueMessageRepository.findByQueueMessageId(event.getMessageIdLong()).ifPresent(qMsg -> {
            matchTypeRepository.findByDisplayEmote(event.getReactionEmote().getAsCodepoints()).ifPresent(mType -> {
                userUtils.retrieveRegistrationData(event.getUserId(), data -> {
                    Region region = Region.parseRegion(data.getString("region"));
                    queueRepository.findByMatchTypeAndRegion(mType, region).ifPresent(q -> {
                        var team = teamUtils.getSolo(mType, event.getUserIdLong());
                        if(queueChanger.joinQueue(q, team)) {
                            messageUtils.sendDMQueueMessage(event.getUserIdLong());
                        } else {
                            event.getUser().openPrivateChannel().queue(pc -> {
                                pc.sendMessage("You're already in a queue/match.").queue(s -> {}, e -> {});
                            });
                        }
                    });
                });
            });
        });
    }

    @Transactional

    @Override
    public void onPrivateMessageReactionAdd(@Nonnull PrivateMessageReactionAddEvent event) {
        if(event.getUser().isBot()) return;
        if(event.getReaction().getReactionEmote().isEmoji() && event.getReaction().getReactionEmote().getName().equalsIgnoreCase("❌")) {
            Player p = teamUtils.getPlayer(event.getUserIdLong());
            System.out.println("user reacted to leave queue");
            queuedTeamRepository.findAll().forEach(qt -> {
                if(qt.getTeam().getPlayerList().contains(p)) {
                    queueRepository.findByQueueingContaining(qt).ifPresent(q -> {
                        queueChanger.leaveQueue(q, qt.getTeam());
                        System.out.println("Team removed from queue");
                        event.getChannel().retrieveMessageById(event.getMessageId()).flatMap(Message::delete).queue();
                    });
                }
            });
        }
    }
}