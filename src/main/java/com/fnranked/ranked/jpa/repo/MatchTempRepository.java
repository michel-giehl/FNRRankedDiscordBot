package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.MatchServer;
import com.fnranked.ranked.jpa.entities.MatchTemp;
import com.fnranked.ranked.jpa.entities.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MatchTempRepository extends CrudRepository<MatchTemp, Long> {
    Optional<MatchTemp> findByTeamAOrTeamBAndMatchServer(Team teamA, Team teamB, MatchServer matchServer);
    Optional<MatchTemp> findByMatchChannelId(long channel);
    Optional<MatchTemp> findByVoteMessageId(long a);
    boolean existsByTeamAOrTeamB(Team a, Team b);
    @Query(value = "SELECT m FROM MatchTemp m LEFT JOIN FETCH m.matchAcceptMessages where m.Id = :id")
    Optional<MatchTemp> findByIdWithMessageList(@Param("id") long id);
    @Query(value = "SELECT * FROM gim_fnranked_test.match_temp m LEFT JOIN gim_fnranked_test.match_temp_match_accept_messages n ON m.id = n.match_temp_id WHERE n.match_accept_messages_id = :id", nativeQuery = true)
    Optional<MatchTemp> findByMatchMessageId(@Param("id") long id);
}
