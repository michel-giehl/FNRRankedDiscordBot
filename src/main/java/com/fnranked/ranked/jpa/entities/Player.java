package com.fnranked.ranked.jpa.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Player {

    @Id
    long Id;

    double eloMultiplier;

    Timestamp multiplierActiveUntil;

    boolean duoInvitesEnabled;

    @Column(columnDefinition = "TINYINT(255) default 0")
    int banStage;

    Timestamp banStageDecrease;

    @OneToMany(cascade = CascadeType.ALL)
    List<Elo> eloList;

    public Player() {

    }

    public Player(long Id) {
        this.Id = Id;
        this.banStage = 0;
    }

    public int getBanStage() {
        return banStage;
    }

    public void setBanStage(int banStage) {
        this.banStage = banStage;
    }

    public Timestamp getBanStageDecrease() {
        return banStageDecrease;
    }

    public void setBanStageDecrease(Timestamp banStageDecrease) {
        this.banStageDecrease = banStageDecrease;
    }

    public List<Elo> getEloList() {
        return eloList;
    }

    public void setEloList(List<Elo> eloList) {
        this.eloList = eloList;
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
