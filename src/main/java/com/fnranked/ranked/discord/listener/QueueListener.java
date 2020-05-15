package com.fnranked.ranked.discord.listener;

import com.fnranked.ranked.data.Region;
import com.fnranked.ranked.jpa.entities.QueuedTeam;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.*;
import com.fnranked.ranked.jpa.util.TeamUtils;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
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
    QueuedTeamRepository queuedTeamRepository;
    @Autowired
    MatchTempRepository matchTempRepository;

    @Transactional
    @Override
    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        if(event.getUser().isBot()) return;
        queueMessageRepository.findByQueueMessageId(event.getMessageIdLong()).ifPresent(qMsg -> {
            matchTypeRepository.findFirstByDisplayEmote(event.getReactionEmote().getEmoji()).ifPresent(mType -> {
                //TODO use player region
                queueRepository.findByMatchTypeAndRegion(mType, Region.EUROPE).ifPresent(q -> {
                    var team = teamUtils.getSolo(mType, event.getUserIdLong());
                    if(!isQueueingOrInMatch(team)) {
                        var queuedTeam = new QueuedTeam(team);
                        queuedTeamRepository.save(queuedTeam);
                        q.getQueueing().add(queuedTeam);
                        queueRepository.save(q);
                    } else {
                        event.getUser().openPrivateChannel().queue(pc -> {
                            pc.sendMessage("You're already in a queue/match.").queue(s -> {}, e -> {});
                        });
                    }
                });
            });
        });
    }
    private boolean isQueueingOrInMatch(Team team) {
        return queuedTeamRepository.existsByTeam(team) || matchTempRepository.existsByTeamAOrTeamB(team, team);
    }
}
