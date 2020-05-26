package com.fnranked.ranked.teams;

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

import java.util.ArrayList;
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
        Player player = getPlayer(discordId);
        Optional<Team> teamOptional = teamRepository.findByCaptainAndSizeAndActiveIsTrue(player, 1);
        if(teamOptional.isPresent()) {
            return teamOptional.get();
        }
        Team team = new Team(player, 1);
        team.setPlayerList(List.of(player));
        //TODO don't handle ELO here.
        Elo elo = new Elo(matchType, 200);
        team.setEloList(List.of(elo));
        teamRepository.save(team);
        return team;
    }

    @Transactional
    public Player getPlayer(long discordId) {
        var playerOpt = playerRepository.findById(discordId);
        final Player player;
        if(playerOpt.isPresent()) {
            System.out.println("PLAYER ALREADY EXISTS");
            return playerOpt.get();
        } else {
            player = new Player(discordId);
            playerRepository.save(player);
        }
        return playerRepository.findById(discordId).get();
    }

    /**
     * Creates a team with of any size
     * @param captainId captain of the team
     * @param playerIds collection of players
     */
    @Transactional
    public void createTeam(long captainId, long... playerIds) {
        System.out.println("CREATING TEAM");
        Player captain = getPlayer(captainId);
        List<Player> players = new ArrayList<>();
        players.add(captain);
        for(long playerId : playerIds) {
            players.add(getPlayer(playerId));
        }
        Team team = new Team(captain, playerIds.length+1);
        team.setPlayerList(players);
        Team T = teamRepository.save(team);
        System.out.println("TEAM ID: " + T.getId());
        System.out.println("TEAM SIZE: " + T.getSize());
    }
}
