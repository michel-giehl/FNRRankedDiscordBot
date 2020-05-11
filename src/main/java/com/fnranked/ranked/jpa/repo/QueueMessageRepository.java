package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.QueueMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QueueMessageRepository extends CrudRepository<QueueMessage, Long> {
    Optional<QueueMessage> findByQueueMessageId(long messageId);
}
