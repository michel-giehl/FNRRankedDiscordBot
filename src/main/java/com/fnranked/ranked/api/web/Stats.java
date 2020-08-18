package com.fnranked.ranked.api.web;

import com.fnranked.ranked.jpa.entities.Player;
import com.fnranked.ranked.jpa.entities.RankedMatch;
import com.fnranked.ranked.jpa.repo.PlayerRepository;
import com.fnranked.ranked.jpa.repo.RankedMatchRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/stats")
public class Stats {

    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    RankedMatchRepository rankedMatchRepository;

    @GetMapping(path = "/{id}")
    public JSONObject getStats(@PathVariable(name = "id")long id) {
        Player player = playerRepository.findById(id).get();
        List<RankedMatch> recentMatches = rankedMatchRepository.findAllByTeamAContainingOrTeamBContainingOrderByEndingTimeDesc(player, player);
        return null;
    }
}
