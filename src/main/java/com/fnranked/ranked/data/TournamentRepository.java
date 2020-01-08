package com.fnranked.ranked.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentRepository extends CrudRepository<Tournament, Integer> {

    List<Tournament> findAllByHostDiscordIDIsAndCompletedIsFalseAndPublishedFalse(long host_discordID);

}
