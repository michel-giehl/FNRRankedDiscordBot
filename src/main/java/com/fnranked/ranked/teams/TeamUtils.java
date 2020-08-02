package com.fnranked.ranked.teams;

import com.fnranked.ranked.jpa.entities.Elo;
import com.fnranked.ranked.jpa.entities.MatchType;
import com.fnranked.ranked.jpa.entities.Player;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.EloRepository;
import com.fnranked.ranked.jpa.repo.PartyRepository;
import com.fnranked.ranked.jpa.repo.PlayerRepository;
import com.fnranked.ranked.jpa.repo.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class TeamUtils {

    private final Logger logger = LoggerFactory.getLogger(TeamUtils.class);


    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PartyRepository partyRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    EloRepository eloRepository;

    /**
     * Finds the team object for a member. Creates player/team entry if it doesn't exist.
     *
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
        Team team = new Team(player);
        team.setPlayerList(List.of(player));
        //TODO don't handle ELO here.
        Elo elo = new Elo(matchType, 200);
        team.setEloList(List.of(elo));
        teamRepository.save(team);
        return team;
    }

    @Transactional
    @Nullable
    public Team getTeam(MatchType matchType, long discordId) {
        Optional<Player> playerOptional = playerRepository.findById(discordId);
        Optional<Party> partyOptional = partyRepository.findById(discordId);
        if (playerOptional.isEmpty()) {
            logger.error("User attempted to join a queue but was not a valid player.");
            return null;
        }
        if (partyOptional.isPresent()) {
            Party party = partyOptional.get();
            Optional<Team> teamOptional = teamRepository.findByPlayerListIs(party.getPlayerList());
            if (teamOptional.isPresent()) {
                Team team = teamOptional.get();
                team.setCaptain(playerOptional.get());
                teamRepository.save(team);
                return team;
            } else {
                Team team = new Team(playerOptional.get(), party.getPlayerList());
                teamRepository.save(team);
                return team;
            }
        }
        //Solo needs a special case because a "solo team" is not manually created by the player
        //meaning the system needs to create a solo team if it doesn't exist.
        //A team with size > 1 needs to be created manually created, meaning we can just return null if it doesn't exist
        else return getSolo(matchType, discordId);
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
            return player;
        }
    }

    /**
     * Creates a team with of any size
     *
     * @param captainId captain of the team
     * @param playerIds collection of players
     */
    @Transactional
    public void createTeam(long captainId, long... playerIds) {
        System.out.println("CREATING TEAM");
        Player captain = getPlayer(captainId);
        List<Player> players = new ArrayList<>();
        players.add(captain);
        for (long playerId : playerIds) {
            players.add(getPlayer(playerId));
        }
        Team team = new Team(captain, players);
        teamRepository.save(team);
        logger.info(String.format("TEAM ID: %d", team.getId()));
        logger.info(String.format("TEAM SIZE: %d", team.getSize()));
    }
}
