package com.fnranked.ranked.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class MatchType {

    @Id
    @GeneratedValue
    long id;

    int teamSize;

    int requiredRoundsToWin;

    String name;

    int season;

    String displayEmote;

    @OneToMany
    List<CreativeMap> mapPool;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDisplayEmote() {
        return displayEmote;
    }

    public void setDisplayEmote(String displayEmote) {
        this.displayEmote = displayEmote;
    }

    public List<CreativeMap> getMapPool() {
        return mapPool;
    }

    public void setMapPool(List<CreativeMap> mapPool) {
        this.mapPool = mapPool;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public int getRequiredRoundsToWin() {
        return requiredRoundsToWin;
    }

    public void setRequiredRoundsToWin(int requiredRoundsToWin) {
        this.requiredRoundsToWin = requiredRoundsToWin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    @Override
    public boolean equals(Object obj) {
        return this.id == ((MatchType)obj).getId();
    }
}
