package com.fnranked.ranked.jpa.util;

import com.fnranked.ranked.jpa.entities.MatchTemp;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.MatchTempRepository;
import com.fnranked.ranked.jpa.repo.PlayerRepository;
import com.fnranked.ranked.jpa.repo.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class MatchUtils {

    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    MatchTempRepository matchTempRepository;
    @Autowired
    TeamRepository teamRepository;

    @Transactional
    public List<MatchTemp> findAllMatchesByUserIdInMatchServer(long userId, long matchServer) {
        List<MatchTemp> matches = new ArrayList<>();
        var playerOpt = playerRepository.findById(userId);
        if(playerOpt.isEmpty()) return matches;
        var teams = teamRepository.findAllByPlayerListContaining(playerOpt.get());
        for(Team team : teams) {
            var match = matchTempRepository.findByTeamAOrTeamBAndMatchServer(team, team, matchServer);
            match.ifPresent(matches::add);
        }
        return matches;
    }
}
