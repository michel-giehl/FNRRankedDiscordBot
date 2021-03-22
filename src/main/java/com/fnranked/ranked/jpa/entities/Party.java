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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, mappedBy = "players")
    List<Player> players;

    public Party() {

    }

    public Party(@NotNull Player captain) {
        this.captain = captain;
        players = Collections.singletonList(captain);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);

    }

}
