package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.data.Region;
import com.fnranked.ranked.jpa.entities.Queue;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QueueRepository extends CrudRepository<Queue, Long> {
    Optional<Queue> findById(long Id);

    Iterable<Queue> findAll();

    List<Queue> findAllByRegionIs(Region region);
}
