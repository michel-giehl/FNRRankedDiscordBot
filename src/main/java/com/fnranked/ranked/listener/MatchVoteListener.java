package com.fnranked.ranked.listener;

import com.fnranked.ranked.api.entities.MatchVote;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.MatchTempRepository;
import com.fnranked.ranked.messages.MessageUtils;
import com.fnranked.ranked.util.MatchUtils;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;

@Component
public class MatchVoteListener extends ListenerAdapter {

    @Autowired
    MatchTempRepository matchTempRepository;
    @Autowired
    MatchUtils matchUtils;
    @Autowired
    MessageUtils messageUtils;

    @Override
    @Transactional
    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        if(event.getUser().isBot()) return;
        if(event.getReactionEmote().isEmote()) return;
        matchTempRepository.findByVoteMessageId(event.getMessageIdLong()).ifPresent(matchTemp -> {
            boolean isTeamA = matchUtils.getTeamByUserId(event.getUserIdLong(), matchTemp).equals(matchTemp.getTeamA());
            switch (event.getReactionEmote().getName()) {
                //trophy
                case "\uD83C\uDFC6":
                    if(isTeamA) {
                        matchTemp.setTeamAVote(MatchVote.TEAM_A);
                    } else {
                        matchTemp.setTeamBVote(MatchVote.TEAM_B);
                    }
                    break;
                //skull
                case "☠️":
                    if(isTeamA) {
                        matchTemp.setTeamAVote(MatchVote.TEAM_B);
                    } else {
                        matchTemp.setTeamBVote(MatchVote.TEAM_A);
                    }
                    break;
                //cancel
                case "\uD83C\uDFF3️":
                    if (isTeamA) {
                        matchTemp.setTeamAVote(MatchVote.CANCEL);
                    } else {
                        matchTemp.setTeamBVote(MatchVote.CANCEL);
                    }
                    break;
                default:
                    break;
            }
            if(matchTemp.getTeamAVote() == matchTemp.getTeamBVote() && matchTemp.getTeamAVote() != MatchVote.PENDING) {
                MatchVote vote = matchTemp.getTeamAVote();
                if(vote == MatchVote.CANCEL) {
                    //CANCEL
                    matchUtils.endMatch(matchTemp, null);
                } else {
                    Team winner = vote == MatchVote.TEAM_A ? matchTemp.getTeamA() : matchTemp.getTeamB();
                    matchUtils.endMatch(matchTemp, winner);
                }
            } else {
                messageUtils.updateVoteMessage(matchTemp);
                matchTempRepository.save(matchTemp);
            }
        });
    }
}
