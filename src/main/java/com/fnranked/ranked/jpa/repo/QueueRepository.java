package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.Queue;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QueueRepository extends CrudRepository<Queue, Long> {
    Optional<Queue> findById(long Id);
    Iterable<Queue> findAll();
}
