package com.fnranked.ranked.jpa.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
public class Elo {

    @Id
    @GeneratedValue
    long id;

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
}
