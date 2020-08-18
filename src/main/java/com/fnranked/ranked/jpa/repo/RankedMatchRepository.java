package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.Player;
import com.fnranked.ranked.jpa.entities.RankedMatch;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RankedMatchRepository extends CrudRepository<RankedMatch, Long> {
    List<RankedMatch> findAllByTeamAContainingOrTeamBContainingOrderByEndingTimeDesc(Player a, Player b);
}
