package com.fnranked.ranked.rest;

import com.fnranked.ranked.jpa.entities.Player;
import com.fnranked.ranked.jpa.repo.PlayerRepository;
import com.fnranked.ranked.jpa.repo.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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

}
