package com.fnranked.ranked.jpa.entities;

import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

/**
 * Parties are used by the PartyBuilder system.
 */
@Entity
public class Party {

    @Id
    private long id;

    @OneToOne
    @NonNull
    @MapsId
    Player captain;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<Player> playerList;

    public Party() {

    }

    public Party(@NotNull Player captain) {
        this.captain = captain;
        playerList = Collections.singletonList(captain);
    }

    public long getId() {
        return captain.getId();
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void addPlayer(Player player) {
        playerList.add(player);
    }

    public boolean removePlayerById(long playerId) {
        boolean result = false;
        for (Player player : playerList) {
            if (player.getId() == playerId) {
                playerList.remove(player);
                result = true;
            }
        }
        return result;

    }

}
