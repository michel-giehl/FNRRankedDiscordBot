package com.fnranked.ranked.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Elo {

    @Id
    @GeneratedValue
    long Id;

    @OneToOne
    MatchType matchType;

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

    public void setId(long id) {
        Id = id;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public void setEloRating(double eloRating) {
        this.eloRating = eloRating;
    }

    public long getId() {
        return Id;
    }
}
