package com.fnranked.ranked.rest;

import com.fnranked.ranked.jpa.entities.Player;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.PlayerRepository;
import com.fnranked.ranked.jpa.repo.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.json.Json;

@RestController
public class TeamController {

    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    TeamRepository teamRepository;

    @PutMapping(path="/player/{id}/add")
    @Transactional
    public String createPlayer(@PathVariable long id) {
        playerRepository.save(new Player(id));
        return Json.createObjectBuilder().add("status", 200).build().toString();
    }

    @PutMapping(path="/team/add")
    @Transactional
    public String createTeam(@RequestHeader long captainId, @RequestHeader int teamSize) {
        var captainOpt= playerRepository.findById(captainId);
        if(captainOpt.isEmpty()) {
            return Json.createObjectBuilder().add("status", 400).add("message", "Bad Request")
                    .add("reason", "Player(s) not found").build().toString();
        }
        var team = new Team(captainOpt.get(), teamSize);
        teamRepository.save(team);
        return Json.createObjectBuilder().add("status", 200).build().toString();
    }
}
