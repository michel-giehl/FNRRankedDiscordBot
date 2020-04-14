package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.MatchMessages;
import org.springframework.data.repository.CrudRepository;

public interface MatchMessagRepo extends CrudRepository<MatchMessages, Long> {
}
