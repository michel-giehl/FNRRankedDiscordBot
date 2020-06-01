package com.fnranked.ranked.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
public class Player implements Serializable {

    @Id
    long Id;

    double eloMultiplier;

    Timestamp multiplierActiveUntil;

    boolean duoInvitesEnabled;

    public Player() {

    }

    public Player(long Id) {
        this.Id = Id;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
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
}
