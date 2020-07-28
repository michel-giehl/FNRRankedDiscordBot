package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.Elo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EloRepository extends CrudRepository<Elo, Long> {
}
