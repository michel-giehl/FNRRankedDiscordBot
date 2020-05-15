package com.fnranked.ranked.jpa.util;

import com.fnranked.ranked.jpa.entities.Elo;
import com.fnranked.ranked.jpa.entities.MatchType;
import com.fnranked.ranked.jpa.entities.Player;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.EloRepository;
import com.fnranked.ranked.jpa.repo.PlayerRepository;
import com.fnranked.ranked.jpa.repo.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class TeamUtils {

    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    EloRepository eloRepository;

    /**
     * Finds the team object for a member. Creates player/team entry if it doesn't exist.
     * @param discordId user id
     * @return (solo) team of the user.
     */
    @Transactional
    public Team getSolo(MatchType matchType, long discordId) {
        var playerOpt = playerRepository.findById(discordId);
        final Player player;
        if(playerOpt.isPresent()) {
            player = playerOpt.get();
            Optional<Team> teamOptional = teamRepository.findByCaptainAndSizeAndActiveIsTrue(player, 1);
            if(teamOptional.isPresent()) {
                return teamOptional.get();
            }
        } else {
            player = new Player(discordId);
            playerRepository.save(player);
        }
        Player newPlayer = playerRepository.findById(discordId).get();
        Team team = new Team(newPlayer, 1);
        team.setPlayerList(List.of(newPlayer));
        Elo elo = new Elo(matchType, 200);
        team.setEloList(List.of(elo));
        teamRepository.save(team);
        return team;
    }
}
