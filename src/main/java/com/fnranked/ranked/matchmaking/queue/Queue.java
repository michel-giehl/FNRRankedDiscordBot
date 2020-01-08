package com.fnranked.ranked.matchmaking.queue;

import com.fnranked.ranked.data.Region;
import com.fnranked.ranked.data.User;
import com.fnranked.ranked.matchmaking.structures.MatchType;
import com.fnranked.ranked.matchmaking.structures.Team;
import com.google.common.cache.Cache;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.persistence.Id;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public interface Queue {

    @Id
    long internalId();

    @Nullable
    String name();

    ConcurrentLinkedQueue<Team> queuedTeams();

    Region region();

    MatchType matchType();

    boolean isOpen();

    /**
     * Cache<Timestamp, queueTime> Contains timestamp and queueTime in ms for every match that was found
     * within the last 60 minutes to calculate average queue times.
     * Maybe change it so it also tracks average queue time for different amount of elo.
     */
    Cache<Long, Long> lastQueueTimes();

    /**
     * basically how many times you have to find a new opponent until you can match against
     * one you previously played
     * @return last blocked
     */
    int amountOfLastPlayersBlocked();

    int startingEloRange();

    /**
     * how much the elo difference increases after each queue iteration
     */
    int deltaEloRange();

    /**
     * max allowed elo difference
     */
    int maxEloRange();

    /**
     * elo matchmaking will be turned off for everyone with higher elo than this
     * example: maxElo = 850
     * This means a player with 850 elo can queue against
     *  anyone with at least 850-currentEloRange() elo
     *
     *  A player with with 10000 elo could still queue against someone with 850 elo
     *  Needed in order for divine player to be able to find matches.
     */
    int maxElo();

    void add(User user);

    void remove(User user);

    void addLastOpponents(Team team1, Team... opponents);

    void runMatchmaking(Team team);
}
