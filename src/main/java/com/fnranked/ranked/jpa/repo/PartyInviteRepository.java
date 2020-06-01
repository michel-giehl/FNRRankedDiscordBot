package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.PartyInvite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface PartyInviteRepository extends CrudRepository<PartyInvite, Long> {

    Optional<PartyInvite> findByInviterId(long a);

    Optional<PartyInvite> findByInviteeId(long a);

    Optional<PartyInvite> findByMessageId(long a);

    Iterable<PartyInvite> findAllByTimeBefore(Timestamp time);

    boolean existsById(long a);

    void deleteByInviteeId(long a);
}
