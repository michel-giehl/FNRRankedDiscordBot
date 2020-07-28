package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.Party;
import com.fnranked.ranked.jpa.entities.PartyInvite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PartyInviteRepository extends CrudRepository<PartyInvite, Long> {

    Optional<PartyInvite> findByInviterId(long a);

    Optional<PartyInvite> findByInviteeId(long a);

    Optional<PartyInvite> findByMessageId(long a);

    Collection<PartyInvite> findAllByTimeBefore(Timestamp time);

    List<PartyInvite> findPartyInvitesByParty(Party party);

    boolean existsById(long a);

    void deleteByInviteeId(long a);
}
