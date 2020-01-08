package com.fnranked.ranked.matchmaking.structures;

import org.springframework.data.annotation.Id;

import java.util.List;

public interface Team {

    @Id
    String id();

    Player captain();

    List<Player> players();

    int size();

    /**
     *
     * @return get average elo.
     */
    double avgElo();

}
