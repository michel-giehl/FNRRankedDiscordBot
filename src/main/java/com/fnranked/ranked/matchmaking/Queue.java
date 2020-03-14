package com.fnranked.ranked.matchmaking;

import com.fnranked.ranked.jpa.entities.Team;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Queue {

    List<Team> queueing;


    public void findOpponent(Team team) {
        //Filter for region
        //Filter for elo
        //Queue if successful.
    }

}
