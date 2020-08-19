package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {
    Optional<Player> findById(long id);
    @Query("SELECT p FROM Player p LEFT JOIN FETCH p.eloList WHERE p.id = :id")
    Optional<Player> findWithEloList(long id);
}
