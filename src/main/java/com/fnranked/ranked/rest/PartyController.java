package com.fnranked.ranked.rest;

import com.fnranked.ranked.jpa.entities.Party;
import com.fnranked.ranked.jpa.repo.PartyRepository;
import com.fnranked.ranked.jpa.repo.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class PartyController {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PartyRepository partyRepository;


    @GetMapping(path = "/party/{playerId}")
    @Transactional
    public String getParty(@PathVariable long playerId) {
        Optional<Party> party = partyRepository.findByPlayerListContaining(playerId);
        return party.toString();
    }
}
