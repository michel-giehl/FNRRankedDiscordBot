package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.MatchTemp;
import com.fnranked.ranked.jpa.entities.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MatchTempRepository extends CrudRepository<MatchTemp, Long> {
    Optional<MatchTemp> findByTeamAOrTeamBAndMatchServer(Team teamA, Team teamB, long matchServer);
}
