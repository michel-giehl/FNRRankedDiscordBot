package com.fnranked.ranked.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Elo {

    @Id
    @GeneratedValue
    long Id;

    @OneToOne
    MatchType matchType;

    @NotNull
    double eloRating;

    public MatchType getMatchType() {
        return matchType;
    }

    public double getEloRating() {
        return eloRating;
    }

    public Elo(){}

    public Elo(MatchType matchType, double eloRating) {
        this.matchType = matchType;
        this.eloRating = eloRating;
    }
}
