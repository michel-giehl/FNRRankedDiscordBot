package com.fnranked.ranked.rest;

import com.fnranked.ranked.jpa.repo.MatchTypeRepository;
import com.fnranked.ranked.jpa.repo.RankedMatchRepository;
import com.fnranked.ranked.jpa.repo.PlayerRepository;
import com.fnranked.ranked.jpa.repo.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.json.Json;

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
        var playerAOpt = playerRepository.findById(playerAId);
        var playerBOpt = playerRepository.findById(playerBId);
        if(playerAOpt.isEmpty() || playerBOpt.isEmpty()) {
            return Json.createObjectBuilder().add("status", 400).add("message", "Bad Request")
                    .add("reason", "Player(s) not found").build().toString();
        }
        var playerA = playerAOpt.get();
        var playerB = playerBOpt.get();
        var teamAOpt = teamRepository.findByCaptainAndSizeAndActiveIsTrue(playerA, teamSize);
        var teamBOpt = teamRepository.findByCaptainAndSizeAndActiveIsTrue(playerB, teamSize);
        var mTypeOpt = matchTypeRepository.findById(type);
        if(teamAOpt.isEmpty() || teamBOpt.isEmpty() || mTypeOpt.isEmpty()) {
            return Json.createObjectBuilder().add("status", 400).add("message", "Bad Request")
                    .add("reason", "Team(s)/MatchType not found").build().toString();
        }
        var teamA = teamAOpt.get();
        var teamB = teamBOpt.get();
        var matchType = mTypeOpt.get();
        //var match = new RankedMatch(teamA, teamB, matchType, TeamSize.values()[teamSize-1], Region.valueOf(region));
        //rankedMatchRepository.save(match);
        return Json.createObjectBuilder().add("status", 200)
                .add("message", "OK")
                .add("matchId", /*match.getId()*/ 0L)
                .build().toString();
    }
}
