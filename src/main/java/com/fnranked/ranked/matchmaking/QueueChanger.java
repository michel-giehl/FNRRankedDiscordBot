package com.fnranked.ranked.matchmaking;

import com.fnranked.ranked.jpa.entities.Queue;
import com.fnranked.ranked.jpa.entities.QueuedTeam;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.MatchTempRepository;
import com.fnranked.ranked.jpa.repo.QueueRepository;
import com.fnranked.ranked.jpa.repo.QueuedTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class QueueChanger {

    @Autowired
    QueueRepository queueRepository;
    @Autowired
    QueuedTeamRepository queuedTeamRepository;
    @Autowired
    MatchTempRepository matchTempRepository;

    @Transactional
    public boolean joinQueue(Queue queue, Team team) {
        if(!isQueueingOrInMatch(team)) {
            queue.getQueueing().add(new QueuedTeam(team));
            queueRepository.save(queue);
            return true;
        }
        return false;
    }

    @Transactional
    public void leaveQueue(Queue queue, Team team) {
        var queuedTeam = queue.getQueueing().stream().filter(qt -> qt.getTeam().equals(team)).findFirst();
        queuedTeam.ifPresent(qt -> {
            queue.getQueueing().remove(qt);
            queueRepository.save(queue);
            queuedTeamRepository.delete(qt);
        });
    }


    @Transactional
    public void removeListFromQueues(Map<Queue, List<QueuedTeam>> queuedTeamsMap) {
        queuedTeamsMap.forEach((queue, queuedTeams) -> {
            queue.getQueueing().removeAll(queuedTeams);
            queuedTeamRepository.deleteAll(queuedTeams);
            queueRepository.save(queue);
        });
    }

    private boolean isQueueingOrInMatch(Team team) {
        return queuedTeamRepository.existsByTeam(team) || matchTempRepository.existsByTeamAOrTeamB(team, team);
    }
}
