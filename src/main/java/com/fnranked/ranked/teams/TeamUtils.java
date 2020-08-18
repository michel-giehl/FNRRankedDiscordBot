package com.fnranked.ranked.teams;

import com.fnranked.ranked.jpa.entities.*;
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
import java.util.List;
import java.util.Optional;

@Component
public class TeamUtils {

    private final Logger logger = LoggerFactory.getLogger(TeamUtils.class);

    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    EloRepository eloRepository;
    @Autowired
    PartyRepository partyRepository;

    /**
     * Finds the team object for a member. Creates player/team entry if it doesn't exist.
     * @param discordId user id
     * @return (solo) team of the user.
     */
    @Transactional
    public Team getSolo(MatchType matchType, long discordId) {
        Player player = getPlayer(discordId);
        Optional<Team> teamOptional = teamRepository.findByCaptainAndSize(player, 1);
        if(teamOptional.isPresent()) {
            return teamOptional.get();
        }
        Team team = new Team(player);
        team.setPlayerList(List.of(player));
        teamRepository.save(team);
        return team;
    }

    @Transactional
    @Nullable
    public Team getTeam(MatchType matchType, long discordId) {
        Optional<Party> partyOptional = partyRepository.findById(discordId);
        if (partyOptional.isPresent()) {
            Party party = partyOptional.get();
            Team team = new Team(party);
            teamRepository.save(team);
            return team;
        }
        //Solo needs a special case because a "solo team" is not necessarily in a party
        else return getSolo(matchType, discordId);
    }

    @Transactional
    @Nullable
    public Team getOldTeam(MatchType matchType, long playerA, long playerB) {
        return teamRepository.findByCaptainAndPlayerListContaining(getPlayer(playerA), getPlayer(playerB)).orElse(null);
    }

    @Transactional
    public Player getPlayer(long discordId) {
        var playerOpt = playerRepository.findById(discordId);
        final Player player;
        if(playerOpt.isPresent()) {
            return playerOpt.get();
        } else {
            player = new Player(discordId);
            playerRepository.save(player);
        }
        return playerRepository.findById(discordId).get();
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

    @Transactional
    public Team createDuo(long captainId, long plebId, Timestamp created, boolean active) {
        Player captain = getPlayer(captainId);
        Player pleb = getPlayer(plebId);
        Team team = new Team(captain, List.of(captain, pleb));
        return teamRepository.save(team);
    }
}
