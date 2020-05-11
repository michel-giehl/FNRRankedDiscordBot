package com.fnranked.ranked.matchmaking;

import com.fnranked.ranked.jpa.entities.Elo;
import com.fnranked.ranked.jpa.entities.MatchType;
import com.fnranked.ranked.jpa.entities.Queue;
import com.fnranked.ranked.jpa.entities.QueuedTeam;
import com.fnranked.ranked.jpa.repo.PlayerRepository;
import com.fnranked.ranked.jpa.repo.QueueRepository;
import com.fnranked.ranked.jpa.repo.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Component
@EnableScheduling
public class Matchmaking {

    private static final Logger logger = LoggerFactory.getLogger(Matchmaking.class);

    @Autowired
    QueueRepository queueRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    QueueChanger queueChanger;

    @Scheduled(initialDelay = 15_000L, fixedRate = 5000L)
    public void findMatchesFromQueues() {
        Iterable<Queue> queues = queueRepository.findAll();
        for (Queue queue : queues) {
            queue.getQueueing().sort(Comparator.comparing(QueuedTeam::getTimeJoined));
            HashMap<Queue, List<QueuedTeam>> toRemove = new HashMap<>();
            List<QueuedTeam> notToMatch = new ArrayList<>();
            for (QueuedTeam teamA : queue.getQueueing()) {
                for (QueuedTeam teamB : queue.getQueueing()) {
                    if (teamA.equals(teamB) || notToMatch.contains(teamB)) continue;
                    if (canMatch(queue, teamA, teamB)) {
                        if (toRemove.containsKey(queue)) {
                            List<QueuedTeam> queuedTeams = toRemove.get(queue);
                            queuedTeams.add(teamA);
                            queuedTeams.add(teamB);
                        } else {
                            List<QueuedTeam> queuedTeams = new ArrayList<>();
                            queuedTeams.add(teamA);
                            queuedTeams.add(teamB);
                            toRemove.put(queue, queuedTeams);
                        }
                        notToMatch.add(teamA);
                        notToMatch.add(teamB);
                        //TODO create match and do stuff.
                    }
                }
            }
            queueChanger.removeListFromQueues(toRemove);
        }
    }

    /**
     * Returns true if teamA and teamB can match based on their elo difference
     */
    private boolean canMatch(Queue queue, QueuedTeam teamA, QueuedTeam teamB) {
        Time time = Time.valueOf(LocalTime.now());
        long timeInQueueA = time.getTime() - teamA.getTimeJoined().getTime();
        long timeInQueueB = time.getTime() - teamB.getTimeJoined().getTime();
        double eloRange = getEloRange(Math.min(timeInQueueA, timeInQueueB));
        double teamAElo = getEloRatingForMatchType(queue.getMatchType(), teamA);
        double teamBElo = getEloRatingForMatchType(queue.getMatchType(), teamB);
        return Math.abs(teamAElo - teamBElo) < eloRange;
    }

    /**
     * @param timeInQueue in milliseconds
     * @return double of eloRange they are searching for.
     */
    public double getEloRange(long timeInQueue) {
        if (timeInQueue < 10000) {
            return 25;
        } else if (timeInQueue < 20000) {
            return 50;
        } else if (timeInQueue < 30000) {
            return 75;
        } else if (timeInQueue < 50000) {
            return 100;
        } else if (timeInQueue < 60000) {
            return 125;
        } else if (timeInQueue < 75000) {
            return 150;
        } else if (timeInQueue < 90000) {
            return 200;
        } else if (timeInQueue < 120000) {
            return 275;
        } else return 400;
    }

    private double getEloRatingForMatchType(MatchType matchType, QueuedTeam queuedTeam) {
        for (Elo elo : queuedTeam.getTeam().getEloList()) {
            if (elo.getMatchType().equals(matchType)) {
                return elo.getEloRating();
            }
        }
        //Getting elo should always be successful if they have managed to queue for it but this is to avoid any NPEs
        logger.warn(String.format("Error finding elo for QueuedTeam with team id of \"%s\", and MatchType \"%s\"(%s), defaulting to 200.", queuedTeam.getTeam().getId(), matchType.getName(), matchType.getId()));
        return 200;
    }
}
