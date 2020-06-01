package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.FortnitePresence;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FortnitePresenceRepository extends CrudRepository<FortnitePresence, Long> {
}
