package com.fnranked.ranked.rest;

import org.springframework.web.bind.annotation.*;

@RestController
public class MatchController {

    @PutMapping(path = "/matches/tournament/start")
    public boolean startMatch(@RequestHeader(value = "Api-Key") String ApiKey,
                              @RequestHeader(value = "Team-1-Ids", defaultValue = "") String team1Ids,
                              @RequestHeader(value = "Team-2-Ids", defaultValue = "") String team2Ids,
                              @RequestHeader(value = "Tournament-Name", defaultValue = "") String tournamentName,
                              @RequestHeader(value = "Region", defaultValue = "") String Region,
                              @RequestHeader(value = "Finish-URL", defaultValue = "") String finishUrl,
                              @RequestHeader(value = "Map-Code", defaultValue = "0") String mapCode) {
        return true;
    }
}
