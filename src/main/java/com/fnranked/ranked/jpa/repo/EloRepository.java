package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.Elo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EloRepository extends CrudRepository<Elo, Long> {
    @Query(value = "SELECT t.id, t.match_type, t.elo_rating FROM(SELECT @ranking := @ranking + 1 as ranking, n.id, n.match_type, n.elo_rating FROM elo n, (SELECT @ranking := 0) r ORDER BY n.elo_rating DESC ) t WHERE ranking = ?;", nativeQuery = true)
    Optional<Elo> findByPosition(int position);
}
