package com.fnranked.ranked.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Player {

    @Id
    long Id;

    double eloMultiplier;

    Timestamp multiplierActiveUntil;

    @OneToMany
    List<Team> teamList;

    public Player() {

    }

    public Player(long Id) {
        this.Id = Id;
    }
}
