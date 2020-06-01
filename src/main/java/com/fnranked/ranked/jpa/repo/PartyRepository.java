package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.Party;
import com.fnranked.ranked.jpa.entities.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PartyRepository extends CrudRepository<Party, Long> {

    //QUERY DIRECTLY FROM JOINED TABLE INSTEAD?
    @Query("SELECT p FROM Party p LEFT JOIN FETCH p.playerList l where l = :player")
    Optional<Party> findByPlayerListContaining(Player player);

}
