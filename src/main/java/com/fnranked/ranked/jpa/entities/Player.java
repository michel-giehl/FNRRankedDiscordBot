package com.fnranked.ranked.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class Player {

    @Id
    long id;

    double eloMultiplier;

    Timestamp multiplierActiveUntil;

    boolean duoInvitesEnabled;

    @Column(columnDefinition = "TINYINT(255) default 0")
    int banStage;

    Timestamp banStageDecrease;

    public Player() {

    }

    public Player(long id) {
        this.id = id;
        this.banStage = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getEloMultiplier() {
        return eloMultiplier;
    }

    public void setEloMultiplier(double eloMultiplier) {
        this.eloMultiplier = eloMultiplier;
    }

    public Timestamp getMultiplierActiveUntil() {
        return multiplierActiveUntil;
    }

    public void setMultiplierActiveUntil(Timestamp multiplierActiveUntil) {
        this.multiplierActiveUntil = multiplierActiveUntil;
    }

    public boolean isDuoInvitesEnabled() {
        return duoInvitesEnabled;
    }

    public void setDuoInvitesEnabled(boolean duoInvitesEnabled) {
        this.duoInvitesEnabled = duoInvitesEnabled;
    }

    @Override
    public boolean equals(Object player) {
        if (player instanceof Player) {
            return ((Player) player).getId() == this.getId();
        }
        return false;
    }
}
