package com.fnranked.ranked.elo;

import com.fnranked.ranked.jpa.entities.Elo;
import com.fnranked.ranked.jpa.entities.MatchType;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.EloRepository;
import com.fnranked.ranked.jpa.repo.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EloUtils {

    @Autowired
    EloRepository eloRepository;
    @Autowired
    TeamRepository teamRepository;

    public Team getTeamWithElo(long teamId, MatchType matchType) {
        Optional<Team> teamOpt = teamRepository.findTeamByIdWithEloList(teamId);
        Team team = teamOpt.get();
        for(Elo elo : team.getEloList()) {
            if(elo.getMatchType().equals(matchType)) {
                return team;
            }
        }
        Elo elo = new Elo(matchType, 200.0);
        team.getEloList().add(elo);
        return teamRepository.save(team);
    }

    public Elo getTeamElo(long teamId, MatchType matchType) {
        Team team = getTeamWithElo(teamId, matchType);
        return team.getEloList().stream().filter(elo -> elo.getMatchType().equals(matchType)).findFirst().get();
    }
}
