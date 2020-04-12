package com.fnranked.ranked.jpa.entities;

import javax.persistence.*;
import java.util.List;

/**
 * Teams are used by the Matchmaking system.
 * Solo Players (1vs1 matches) are also represented by Team objects to have a uniform system
 * and an easier way to implement 2v2/3v3 matches.
 */
@Entity
public class Team {

    @Id
    @GeneratedValue
    long Id;

    boolean active;

    int size;

    @OneToOne
    Player captain;

    @ManyToMany
    List<RankedMatch> rankedMatches;

    @OneToMany
    List<Player> playerList;

    public Team() {

    }

    public Team(Player captain, int teamSize) {
        this.captain = captain;
        this.size = teamSize;
    }
}
