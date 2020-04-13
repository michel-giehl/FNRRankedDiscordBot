package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.CreativeMap;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateiveMapRepository extends CrudRepository<CreativeMap, Long> {
}
