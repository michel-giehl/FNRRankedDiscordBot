package com.fnranked.ranked.jpa.repo;

import com.fnranked.ranked.jpa.entities.Party;
import com.fnranked.ranked.jpa.entities.Player;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PartyRepository extends CrudRepository<Party, Long> {

    @NotNull
    @Override
    @Query("SELECT p FROM Party p LEFT JOIN FETCH p.playerList")
    Optional<Party> findById(@NotNull @Param("id") Long id);

    Optional<Party> findByPlayerListContaining(Player player);

}
