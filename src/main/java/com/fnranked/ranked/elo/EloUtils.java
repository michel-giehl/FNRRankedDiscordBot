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

    public Optional<Elo> getTeamElo(long teamId, MatchType matchType) {
        Optional<Team> teamOpt = teamRepository.findTeamByIdWithEloList(teamId);
        if(teamOpt.isEmpty()) return Optional.empty();
        Team team = teamOpt.get();
        return team.getEloList().stream().filter(e -> e.getMatchType().equals(matchType)).findFirst();
    }
}
