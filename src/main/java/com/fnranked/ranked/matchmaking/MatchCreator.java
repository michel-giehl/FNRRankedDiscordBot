package com.fnranked.ranked.matchmaking;

import com.fnranked.ranked.jpa.entities.MatchTemp;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.MatchTempRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class MatchCreator {

    @Autowired
    MatchTempRepository matchTempRepository;

    @Value("${match.match_accept_timer}")
    long ttl;

    public void createMatch(Team teamA, Team teamB) {
        var tmpMatch = new MatchTemp();
        matchTempRepository.save(tmpMatch);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                matchTempRepository.findById(tmpMatch.getId()).ifPresent(matchTemp -> {
                    if(!matchTemp.isTeamAAccepted() || !matchTemp.isTeamBAccepted()) {
                        matchTempRepository.delete(matchTemp);
                        //TODO DM people, put back in queue.
                    }
                });
            }
        }, ttl);
    }
}
