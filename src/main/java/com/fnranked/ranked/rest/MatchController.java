package com.fnranked.ranked.rest;

import com.fnranked.ranked.jpa.entities.MatchType;
import com.fnranked.ranked.jpa.entities.Player;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.MatchTypeRepository;
import com.fnranked.ranked.jpa.repo.PlayerRepository;
import com.fnranked.ranked.jpa.repo.RankedMatchRepository;
import com.fnranked.ranked.jpa.repo.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.json.Json;
import java.util.Optional;

@RestController
public class MatchController {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    RankedMatchRepository rankedMatchRepository;

    @Autowired
    MatchTypeRepository matchTypeRepository;

    @PutMapping(path = "/match/start/{type}/{teamSize}")
    @Transactional
    public String startMatch(@RequestHeader(value = "apiKey") String ApiKey,
                            @RequestHeader(value = "playerAId", defaultValue = "") long playerAId,
                            @RequestHeader(value = "playerBId", defaultValue = "") long playerBId,
                            @RequestHeader(value = "region", defaultValue = "") String region,
                            @PathVariable long type, @PathVariable int teamSize) {
        Optional<Player> playerAOpt = playerRepository.findById(playerAId);
        Optional<Player> playerBOpt = playerRepository.findById(playerBId);
        if (playerAOpt.isEmpty() || playerBOpt.isEmpty()) {
            return Json.createObjectBuilder().add("status", 400).add("message", "Bad Request")
                    .add("reason", "Player(s) not found").build().toString();
        }
        Player playerA = playerAOpt.get();
        Player playerB = playerBOpt.get();
        Optional<Team> teamAOpt = teamRepository.findByCaptainAndSizeAndActiveIsTrue(playerA, teamSize);
        Optional<Team> teamBOpt = teamRepository.findByCaptainAndSizeAndActiveIsTrue(playerB, teamSize);
        Optional<MatchType> mTypeOpt = matchTypeRepository.findById(type);
        if (teamAOpt.isEmpty() || teamBOpt.isEmpty() || mTypeOpt.isEmpty()) {
            return Json.createObjectBuilder().add("status", 400).add("message", "Bad Request")
                    .add("reason", "Team(s)/MatchType not found").build().toString();
        }
        Team teamA = teamAOpt.get();
        Team teamB = teamBOpt.get();
        MatchType matchType = mTypeOpt.get();
        //var match = new RankedMatch(teamA, teamB, matchType, TeamSize.values()[teamSize-1], Region.valueOf(region));
        //rankedMatchRepository.save(match);
        return Json.createObjectBuilder().add("status", 200)
                .add("message", "OK")
                .add("matchId", /*match.getId()*/ 0L)
                .build().toString();
    }
}
