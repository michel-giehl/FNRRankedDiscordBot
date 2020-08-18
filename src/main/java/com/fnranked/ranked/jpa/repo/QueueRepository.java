package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.api.entities.Region;
import com.fnranked.ranked.jpa.entities.MatchType;
import com.fnranked.ranked.jpa.entities.Queue;
import com.fnranked.ranked.jpa.entities.QueuedTeam;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QueueRepository extends CrudRepository<Queue, Long> {
    Optional<Queue> findById(long id);

    Optional<Queue> findByMatchTypeAndRegion(MatchType a, Region r);
    Optional<Queue> findByQueueingContaining(QueuedTeam t);

    List<Queue> findAllByRegionIs(Region region);

    List<Queue> findAllByEnabledIsTrue();
}
