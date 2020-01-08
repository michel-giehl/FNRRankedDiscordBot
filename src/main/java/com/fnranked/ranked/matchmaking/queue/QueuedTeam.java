package com.fnranked.ranked.matchmaking.queue;

import com.fnranked.ranked.matchmaking.structures.Team;

import javax.persistence.*;
import java.util.Collection;

public class QueuedTeam {

    /**
     * Team ID if 2v2, user id if 1v1.
     */
    @Id
    long internalId;

    @ManyToOne
    private Team team;

    private double elo;

    private int eloRange;

    private boolean hasPriorityQueue;

    private Queue currentQueue;

    private Collection<Team> blockedTeams;

    private long timeJoined;
}
