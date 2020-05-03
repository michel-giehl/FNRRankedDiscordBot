package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.MatchType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchTypeRepository extends CrudRepository<MatchType, Long> {
    Optional<MatchType> findById(long Id);

}
