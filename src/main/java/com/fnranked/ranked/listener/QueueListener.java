package com.fnranked.ranked.listener;

import com.fnranked.ranked.api.entities.Region;
import com.fnranked.ranked.jpa.entities.Player;
import com.fnranked.ranked.messages.MessageUtils;
import com.fnranked.ranked.util.BanUtils;
import com.fnranked.ranked.util.UserUtils;
import com.fnranked.ranked.jpa.repo.*;
import com.fnranked.ranked.teams.TeamUtils;
import com.fnranked.ranked.matchmaking.QueueChanger;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
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
    @Autowired
    BanUtils banUtils;

    @Transactional
    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        if(event.getUser().isBot()) return;
        queueMessageRepository.findByQueueMessageId(event.getMessageIdLong()).ifPresent(qMsg -> {
            //TODO use util class to get rid of getAsCodepoints()
            matchTypeRepository.findByDisplayEmote(event.getReactionEmote().getAsCodepoints()).ifPresent(mType -> {
                userUtils.retrieveRegistrationData(event.getUserId(), data -> {
                    Region region = Region.parseRegion(data.getString("region"));
                    queueRepository.findByMatchTypeAndRegion(mType, region).ifPresent(q -> {
                        var team = teamUtils.getTeam(mType, event.getUserIdLong());
                        if(banUtils.isBanned(team)) {
                            //TODO send detailed ban information
                            event.getUser().openPrivateChannel().flatMap(pc -> pc.sendMessage("You can't join the queue because someone in your team is temporarily banned from matchmaking")).queue();
                            return;
                        }
                        if(queueChanger.joinQueue(q, team)) {
                            messageUtils.sendDMQueueMessage(team);
                            //delete message if queue in DMs and delete queue message
                            if(qMsg.isDMQueue()) {
                                event.getChannel().retrieveMessageById(event.getMessageIdLong()).flatMap(Message::delete).queue();
                                queueMessageRepository.delete(qMsg);
                            }
                        } else {
                            event.getUser().openPrivateChannel().queue(pc -> pc.sendMessage("You're already in a queue/match.").queue(s -> {
                            }, e -> {
                            }));
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
            queuedTeamRepository.findAll().forEach(qt -> {
                if(qt.getTeam().getPlayerList().contains(p)) {
                    queueRepository.findByQueueingContaining(qt).ifPresent(q -> {
                        queueChanger.leaveQueue(q, qt.getTeam());
                        event.getChannel().retrieveMessageById(event.getMessageId()).flatMap(Message::delete).queue();
                    });
                }
            });
        }
    }
}
