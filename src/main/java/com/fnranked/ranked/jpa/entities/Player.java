package com.fnranked.ranked.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Player {

    @Id
    long Id;

    @OneToMany
    List<Team> teamList;

    public Player() {

    }

    public Player(long Id) {
        this.Id = Id;
    }
}
