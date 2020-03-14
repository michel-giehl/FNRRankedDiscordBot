package com.fnranked.ranked.jpa.entities;

import javax.persistence.*;
import java.util.List;

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
