package com.fnranked.ranked.jpa.entities;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

/**
 * Parties are used by the PartyBuilder system.
 */
@Entity
@Data
public class Party {

    @Id
    private Long id;

    @OneToOne
    @NonNull
    @MapsId
    Player captain;

    @OneToMany(fetch = FetchType.EAGER)
    List<Player> playerList;

    public Party() {

    }

    public Party(@NotNull Player captain) {
        this.captain = captain;
        playerList = Collections.singletonList(captain);
    }

    public void addPlayer(Player player) {
        playerList.add(player);
    }

    public void removePlayer(Player player) {
        playerList.remove(player);

    }

}
