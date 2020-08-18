package com.fnranked.ranked.api.web;

import com.fnranked.ranked.jpa.entities.Elo;
import com.fnranked.ranked.jpa.repo.EloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/leaderboard")
public class Leaderboard {

    @Autowired
    EloRepository eloRepository;

    @GetMapping(path = "/{platform}/{region}/{gamemode}/")
    public Iterable<Elo> getLeaderboard(@PathVariable(name = "platform")int platform,
                                        @PathVariable(name = "region") int region,
                                        @PathVariable(name = "gamemode") int gamemode) {
        return eloRepository.findAll();
    }
}
