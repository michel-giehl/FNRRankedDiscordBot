package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.QueueMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueueMessageRepository extends CrudRepository<QueueMessage, Long> {
}
