package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.Player;
import com.fnranked.ranked.jpa.entities.QueuedTeam;
import com.fnranked.ranked.jpa.entities.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QueuedTeamRepository extends CrudRepository<QueuedTeam, Long> {
    boolean existsByTeam(Team t);
    Optional<QueuedTeam> findByTeam(Team t);

    @Query(value = "SELECT q FROM QueuedTeam q WHERE :p IN(q.team.playerList)")
    Optional<QueuedTeam> findByTeamContaining(@Param("p") Player p);


    @Query(value = "SELECT q FROM QueuedTeam q LEFT JOIN FETCH q.matchMessages WHERE q.team = :team")
    Optional<QueuedTeam> findByTeamWithQueueMessages(@Param("team") Team t);

}
