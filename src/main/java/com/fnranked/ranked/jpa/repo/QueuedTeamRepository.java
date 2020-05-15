package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.QueuedTeam;
import com.fnranked.ranked.jpa.entities.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QueuedTeamRepository extends CrudRepository<QueuedTeam, Long> {
    boolean existsByTeam(Team t);
    Optional<QueuedTeam> findByTeam(Team t);
}
