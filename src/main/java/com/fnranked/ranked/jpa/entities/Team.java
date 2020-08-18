package com.fnranked.ranked.jpa.entities;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Teams are used by the Matchmaking system.
 * Solo Players (1vs1 matches) are also represented by Team objects to have a uniform system
 * and an easier way to implement 2v2/3v3 matches.
 */
@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long Id;

    boolean active;

    int size;

    Timestamp timeCreated;

    @ManyToOne
    @NonNull
    Player captain;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<Player> playerList;

    @ManyToMany(cascade = CascadeType.ALL)
    List<RankedMatch> rankedMatches;

    public Team() {

    }

    public Team(Player captain, int teamSize) {
        this.captain = captain;
        this.size = teamSize;
        this.active = true;
        this.timeCreated = Timestamp.from(Instant.now());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Team && this.Id == ((Team)obj).getId();
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @NonNull
    public Player getCaptain() {
        return captain;
    }

    public void setCaptain(@NonNull Player captain) {
        this.captain = captain;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public List<RankedMatch> getRankedMatches() {
        return rankedMatches;
    }

    public void setRankedMatches(List<RankedMatch> rankedMatches) {
        this.rankedMatches = rankedMatches;
    }

    public Timestamp getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Timestamp timeCreated) {
        this.timeCreated = timeCreated;
    }
}
