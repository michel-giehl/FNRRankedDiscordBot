package com.fnranked.ranked.rest;

import org.springframework.web.bind.annotation.*;

@RestController
public class FortnitePresenceController {

    @RequestMapping("/presence")
    public FortnitePresence presenceReceived(@RequestHeader(value = "botId", defaultValue = "") String botId,
            @RequestHeader(value= "sessionId", defaultValue = "") String sessionId,
            @RequestHeader(value= "partyId", defaultValue = "") String partyId,
            @RequestHeader(value= "epic-Ids", defaultValue = "") String[] accountIds) {
        return new FortnitePresence(botId, sessionId, partyId, 0);
    }
}
