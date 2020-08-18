package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.Player;
import com.fnranked.ranked.jpa.entities.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {
    Optional<Team> findByCaptainAndSizeAndActiveIsTrue(Player captain, int teamSize);
    Optional<Team> findByPlayerListContainingAndSizeAndActiveIsTrue(Player containing, int teamSize);
    Optional<Team> findByCaptainAndPlayerListContaining(Player captain, Player contain);
    Iterable<Team> findAllByPlayerListContaining(Player player);
    @Query(value = "SELECT t from Team t LEFT JOIN FETCH t.playerList where t.Id = :id")
    Optional<Team> findTeamByIdWithPlayerList(@Param("id") long id);
}
