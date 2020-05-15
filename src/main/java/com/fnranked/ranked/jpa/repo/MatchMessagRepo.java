package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.MatchMessages;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MatchMessagRepo extends CrudRepository<MatchMessages, Long> {
    Optional<MatchMessages> findFirstByMessageId(long a);
}
