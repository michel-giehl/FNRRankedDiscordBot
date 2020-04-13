package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.RankedMatch;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankedMatchRepository extends CrudRepository<RankedMatch, Long> {
}
