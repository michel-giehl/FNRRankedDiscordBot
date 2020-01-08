package com.fnranked.ranked.rest;

import com.fnranked.ranked.elo.Elo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EloController {

    @RequestMapping(method = RequestMethod.GET, path = "/elo/")
    public Elo findUserElo(@RequestParam(value = "userId") String userId, @RequestParam(value = "eloType") String eloType) {
        return new Elo();
    }
}
