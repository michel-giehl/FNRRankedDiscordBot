package com.fnranked.ranked.jpa.util;

import com.fnranked.ranked.jpa.entities.Player;
import com.fnranked.ranked.jpa.entities.Team;
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

    /**
     * Finds the team object for a member. Creates player/team entry if it doesn't exist.
     * @param discordId user id
     * @return (solo) team of the user.
     */
    @Transactional
    public Team getSolo(long discordId) {
        var playerOpt = playerRepository.findById(discordId);
        final Player player;
        if(playerOpt.isPresent()) {
            player = playerOpt.get();
        } else {
            player = new Player(discordId);
            playerRepository.save(player);
        }
        Optional<Team> teamOptional = teamRepository.findByCaptainAndSizeAndActiveIsTrue(player, 1);
        if(teamOptional.isPresent()) {
            return teamOptional.get();
        } else {
            Team team = new Team(player, 1);
            team.setPlayerList(List.of(player));
            teamRepository.save(team);
            return team;
        }
    }
}
