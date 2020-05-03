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

    String season;

    long displayEmoteId;

    @OneToMany
    List<CreativeMap> mapPool;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getDisplayEmoteId() {
        return displayEmoteId;
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

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public void setDisplayEmoteId(long displayEmoteId) {
        this.displayEmoteId = displayEmoteId;
    }
}
