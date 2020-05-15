package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.MatchServer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchServerRepo extends CrudRepository<MatchServer, Long> {
    Optional<MatchServer> findById(long a);
}
