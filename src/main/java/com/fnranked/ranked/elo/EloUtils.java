package com.fnranked.ranked.elo;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.fnranked.ranked.jpa.entities.Elo;
import com.fnranked.ranked.jpa.entities.MatchType;
import com.fnranked.ranked.jpa.entities.Player;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.EloRepository;
import com.fnranked.ranked.jpa.repo.PlayerRepository;
import com.fnranked.ranked.jpa.repo.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EloUtils {

    @Autowired
    EloRepository eloRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    PlayerRepository playerRepository;

    double DEFAULT_RATING = 200.0;

    @Transactional
    public Elo getPlayerElo(Player player, MatchType matchType) {
        var playerWithElo = playerRepository.findWithEloList(player.getId()).get();
        for(Elo elo : playerWithElo.getEloList()) {
            if (elo.getMatchType().equals(matchType)) {
                return elo;
            }
        }
        Elo playerElo = new Elo(matchType, DEFAULT_RATING);
        playerWithElo.getEloList().add(playerElo);
        playerRepository.save(playerWithElo);
        return playerElo;
    }

    @Transactional
    public void updateElo(Player player, Elo currentElo, double newElo) {
        currentElo.setEloRating(newElo);
        eloRepository.save(currentElo);
    }

    public double getTeamElo(long teamId, MatchType matchType) {
        long start = System.currentTimeMillis();
        Team team = teamRepository.findTeamByIdWithPlayerList(teamId).get();
        double maxElo = -1;
        double eloRating = 0.0;
        for(Player player : team.getPlayerList()) {
            var elo = getPlayerElo(player ,matchType);
            eloRating += elo.getEloRating();
            maxElo = Math.max(maxElo, elo.getEloRating());
        }
        eloRating += maxElo;
        eloRating /= (team.getPlayerList().size() + 1);
        long ms = System.currentTimeMillis() - start;
        return eloRating;
    }
}
