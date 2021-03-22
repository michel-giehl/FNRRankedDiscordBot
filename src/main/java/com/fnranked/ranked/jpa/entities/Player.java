package com.fnranked.ranked.jpa.entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
public class Player {

    @Id
    long id;

    double eloMultiplier;

    Timestamp multiplierActiveUntil;

    boolean duoInvitesEnabled;

    @Column(columnDefinition = "TINYINT(255) default 0")
    int banStage;

    Timestamp banStageDecrease;

    @OneToMany(cascade = CascadeType.ALL)
    List<Elo> eloList;

    @ManyToOne
    Party party;

    public Player() {
    }

    public Player(long id) {
        this.id = id;
        this.banStage = 0;
    }

    @Override
    public boolean equals(Object player) {
        if (player instanceof Player) {
            return ((Player) player).getId() == this.getId();
        }
        return false;
    }
}
