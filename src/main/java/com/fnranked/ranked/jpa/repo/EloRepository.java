package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.Elo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EloRepository extends CrudRepository<Elo, Long> {
}
