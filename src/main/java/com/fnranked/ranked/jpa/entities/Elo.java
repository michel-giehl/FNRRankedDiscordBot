package com.fnranked.ranked.jpa.entities;

import javax.persistence.*;

@Entity
public class Elo {

    @Id
    @GeneratedValue
    long Id;

    @OneToOne
    Team team;

    @OneToOne
    MatchType matchType;

    double eloRating;
}
