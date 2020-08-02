package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.MatchmakingBan;
import com.fnranked.ranked.jpa.entities.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchmakingBanRepository extends CrudRepository<MatchmakingBan, Long> {
    Iterable<MatchmakingBan> findAllByPlayer(Player p);
    //Find latest ban
    Optional<MatchmakingBan> findFirstByPlayerOrderByTimeOfBanDesc(Player p);
}
