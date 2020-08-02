package com.fnranked.ranked.jpa.entities;

import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.List;

/**
 * Teams are used by the Matchmaking system.
 * Solo Players (1vs1 matches) are also represented by Team objects to have a uniform system
 * and an easier way to implement 2v2/3v3 matches.
 * Teams are a temporary entity formed when entering a queue from the user's party,
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

    @OneToMany(cascade = CascadeType.ALL)
    List<Elo> eloList;

    public Team() {
        //Empty constructor
    }

    public Team(@NotNull Player captain) {
        this.captain = captain;
        this.size = 1;
    }

    public Team(@NotNull Party party) {
        this.captain = party.getCaptain();
        this.size = party.getPlayerList().size();
        this.playerList = party.getPlayerList();
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

    @NonNull
    public Player getCaptain() {
        return captain;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public void setEloList(List<Elo> eloList) {
        this.eloList = eloList;
    }
}
