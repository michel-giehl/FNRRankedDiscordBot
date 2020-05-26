package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.DuoInvite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface DuoInviteRepository extends CrudRepository<DuoInvite, Long> {
    Optional<DuoInvite> findByInviterId(long a);
    Optional<DuoInvite> findByInviteeId(long a);
    Optional<DuoInvite> findByMessageId(long a);
    Iterable<DuoInvite> findAllByTimeBefore(Timestamp time);
    boolean existsById(long a);
    void deleteByInviteeId(long a);
}
