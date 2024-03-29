package com.fnranked.ranked.elo;

import com.fnranked.ranked.jpa.entities.Elo;
import com.fnranked.ranked.jpa.entities.Player;
import com.fnranked.ranked.jpa.entities.RankedMatch;
import com.fnranked.ranked.jpa.repo.EloRepository;
import com.fnranked.ranked.jpa.repo.PlayerRepository;
import com.fnranked.ranked.jpa.repo.RankedMatchRepository;
import com.fnranked.ranked.jpa.repo.TeamRepository;
import com.fnranked.ranked.util.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class EloCalculator {

    @Autowired
    EloRepository eloRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    RankedMatchRepository rankedMatchRepository;
    @Autowired
    EloUtils eloUtils;

    //amount of elo that will be put in the pool
    private final int c = 60;

    /**
     *
     * @param RatingA player A's elo
     * @param RatingB player B's elo
     * @return probability of player A winning the match
     */
    private double probability(double RatingA, double RatingB) {
        return 1.0 * 1.0 / (1 + 1.0 * (Math.pow(10, 1.0 * (RatingA - RatingB) / 400)));
    }

    private double[] performCalculations(double rating1, double rating2, boolean playerAWon) {
        double Pb = probability(rating1, rating2);

        double Pa = probability(rating2, rating1);
        final double rating1Old = rating1;
        final double rating2Old = rating2;
        if (playerAWon) {
            rating1 += c * (1.0F - Pa);
            rating2 += c/1.1 * (0.0F - Pb);
            if(rating1 - rating1Old < 10)
                rating1 = rating1Old+10;
        }
        else {
            rating1 += c/1.1 * (0.0F - Pa);
            rating2 += c * (1.0F - Pb);
            if(rating2 - rating2Old < 10)
                rating2 = rating2Old+10;
        }
        if(rating1 < 0) rating1 = 0;
        if(rating2 < 0) rating2 = 0;
        return new double[] { rating1, rating2 };
    }

    @Transactional
    public RankedMatch updateRatings(RankedMatch rankedMatch) {
        double teamAElo = eloUtils.getTeamElo(rankedMatch.getTeamA().getId(), rankedMatch.getMatchType());
        double teamBElo = eloUtils.getTeamElo(rankedMatch.getTeamB().getId(), rankedMatch.getMatchType());
        boolean teamAWon = rankedMatch.getWinner().equals(rankedMatch.getTeamA());
        for (Player p : rankedMatch.getTeamA().getPlayerList()) {
            Elo playerElo = eloUtils.getPlayerElo(p, rankedMatch.getMatchType());
            double newElo = performCalculations(playerElo.getEloRating(), teamBElo, teamAWon)[0];
            eloUtils.updateElo(p, playerElo, newElo);
        }
        for (Player p : rankedMatch.getTeamB().getPlayerList()) {
            Elo playerElo = eloUtils.getPlayerElo(p, rankedMatch.getMatchType());
            double newElo = performCalculations(teamBElo, playerElo.getEloRating(), teamAWon)[1];
            eloUtils.updateElo(p, playerElo, newElo);
        }
        double[] newRatings = performCalculations(teamAElo, teamBElo, teamAWon);
        double teamANewRating = newRatings[0];
        double teamBNewRating = newRatings[1];
        double teamAEloChange = teamANewRating - teamAElo;
        double teamBEloChange = teamBNewRating - teamBElo;
        rankedMatch.setTeamAEloChange(teamAEloChange);
        rankedMatch.setTeamBEloChange(teamBEloChange);
        return rankedMatch;
    }
}
