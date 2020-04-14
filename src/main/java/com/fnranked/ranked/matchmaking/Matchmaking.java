package com.fnranked.ranked.matchmaking;

import com.fnranked.ranked.jpa.entities.Queue;
import com.fnranked.ranked.jpa.entities.QueuedTeam;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.PlayerRepository;
import com.fnranked.ranked.jpa.repo.QueueRepository;
import com.fnranked.ranked.jpa.repo.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Component
@EnableScheduling
public class Matchmaking {

    @Autowired
    QueueRepository queueRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    TeamRepository teamRepository;

    public Matchmaking() {

    }

    @Transactional
    public void joinQueue(Queue queue, Team team) {
        queue.getQueueing().add(new QueuedTeam(team));
        queueRepository.save(queue);
    }

    @Transactional
    public void leaveQueue(Queue queue, Team team) {
        var queuedTeam = queue.getQueueing().stream().filter(qt -> qt.getTeam().equals(team)).findFirst();
        queuedTeam.ifPresent(qt -> {
            queue.getQueueing().remove(qt);
            queueRepository.save(queue);
        });
    }

    @Scheduled(initialDelay = 15_000L, fixedRate = 5000L)
    public void findOpponent()  {
        Iterable<Queue> queues = queueRepository.findAll();
        for(Queue queue : queues) {
            queue.getQueueing().sort(Comparator.comparing(QueuedTeam::getTimeJoined));
            List<QueuedTeam> toRemove = new ArrayList<>();
            for(QueuedTeam teamA : queue.getQueueing()) {
                for(QueuedTeam teamB : queue.getQueueing()) {
                    if(teamA.equals(teamB)) continue;
                    if(toRemove.contains(teamB)) continue;
                    if(canMatch(teamA, teamB)) {
                        toRemove.add(teamA);
                        toRemove.add(teamB);
                        //TODO create match and do stuff.
                    }
                }
            }
        }
    }

    /**
     * Returns true if teamA and teamB can match based on their elo difference
     */
    //TODO implement
    private boolean canMatch(QueuedTeam teamA, QueuedTeam teamB) {
        return true;
    }
}
