package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.CreativeMap;
import com.fnranked.ranked.jpa.entities.MatchType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreativeMapRepository extends CrudRepository<CreativeMap, Long> {
    @Query(value = "SELECT * FROM creative_map c WHERE c.match_type_id = :matchType ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<CreativeMap> selectRandom(@Param("matchType")MatchType matchType);
}
