package com.fnranked.ranked.jpa.entities;

import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;

import javax.persistence.*;
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
    long id;

    int size;

    @ManyToOne
    @NonNull
    Player captain;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<Player> playerList;

    @ManyToMany(cascade = CascadeType.ALL)
    List<RankedMatch> rankedMatches;

    @OneToMany(cascade = CascadeType.ALL)
    List<Elo> eloList;

    public Team() {
        //Empty constructor
    }

    public Team(@NotNull Player captain) {
        this.captain = captain;
    }

    public Team(@NotNull Player captain, List<Player> players) {
        this.captain = captain;
        this.size = players.size();
        this.playerList = players;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Team && this.id == ((Team) obj).getId();
    }

    public long getId() {
        return id;
    }

    public List<Elo> getEloList() {
        return eloList;
    }

    public void setId(long id) {
        this.id = id;
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

    public void setEloList(List<Elo> eloList) {
        this.eloList = eloList;
    }
}
