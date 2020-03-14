package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.MatchTemp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchTempRepository extends CrudRepository<MatchTemp, Long> {
}
