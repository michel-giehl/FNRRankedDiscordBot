package com.fnranked.ranked.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Player {

    @Id
    long Id;

    @ManyToMany
    List<Team> teamList;

    public Player() {

    }

    public Player(long Id) {
        this.Id = Id;
    }
}
