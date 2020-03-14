package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.VoteMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteMessageRepository extends CrudRepository<VoteMessage, Long> {
}
