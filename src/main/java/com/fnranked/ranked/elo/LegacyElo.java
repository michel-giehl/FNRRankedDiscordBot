package com.fnranked.ranked.elo;

public class LegacyElo {

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

    private double[] performCalculationsS02(double rating1, double rating2, boolean playerAWon) {

        double Pb = probability(rating1, rating2);

        double Pa = probability(rating2, rating1);
        final double rating1Old = rating1;
        final double rating2Old = rating2;
        if (playerAWon) {
            rating1 += c * (1.0F - Pa);
            rating2 += c * (0.0F - Pb);
        }
        else {
            rating1 += c * (0.0F - Pa);
            rating2 += c * (1.0F - Pb);
        }
        if(rating1 < 0) rating1 = 0;
        if(rating2 < 0) rating2 = 0;
        return new double[] { rating1, rating2 };
    }

    private double[] performCalculationsS03(double rating1, double rating2, boolean playerAWon) {

        double Pb = probability(rating1, rating2);

        double Pa = probability(rating2, rating1);
        final double rating1Old = rating1;
        final double rating2Old = rating2;
        if (playerAWon) {
            rating1 += c * (1.0F - Pa);
            rating2 += c * (0.0F - Pb);
            if(rating1 - rating1Old < 15)
                rating1 = rating1Old + 15;
        }
        else {
            rating1 += c * (0.0F - Pa);
            rating2 += c * (1.0F - Pb);
            if(rating2 - rating2Old < 10)
                rating2 = rating2Old + 15;
        }
        if(rating1 < 0) rating1 = 0;
        if(rating2 < 0) rating2 = 0;
        return new double[] { rating1, rating2 };
    }
}
