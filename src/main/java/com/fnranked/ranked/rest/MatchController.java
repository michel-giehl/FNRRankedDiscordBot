package com.fnranked.ranked.rest;

import com.fnranked.ranked.data.MatchType;
import com.fnranked.ranked.data.Region;
import com.fnranked.ranked.data.TeamSize;
import com.fnranked.ranked.jpa.entities.RankedMatch;
import com.fnranked.ranked.jpa.repo.MatchRepository;
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
    MatchRepository matchRepository;

    @PutMapping(path = "/match/start/{type}/{teamSize}")
    @Transactional
    public String startMatch(@RequestHeader(value = "apiKey") String ApiKey,
                            @RequestHeader(value = "playerAId", defaultValue = "") long playerAId,
                            @RequestHeader(value = "playerBId", defaultValue = "") long playerBId,
                            @RequestHeader(value = "region", defaultValue = "") String region,
                            @PathVariable String type, @PathVariable int teamSize) {
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
        if(teamAOpt.isEmpty() || teamBOpt.isEmpty()) {
            return Json.createObjectBuilder().add("status", 400).add("message", "Bad Request")
                    .add("reason", "Team(s) not found").build().toString();
        }
        var teamA = teamAOpt.get();
        var teamB = teamBOpt.get();
        var match = new RankedMatch(teamA, teamB, MatchType.valueOf(type), TeamSize.values()[teamSize-1], Region.valueOf(region));
        matchRepository.save(match);
        return Json.createObjectBuilder().add("status", 200)
                .add("message", "OK")
                .add("matchId", match.getId())
                .build().toString();
    }
}
