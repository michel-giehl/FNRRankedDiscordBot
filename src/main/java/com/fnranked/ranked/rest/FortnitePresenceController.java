package com.fnranked.ranked.rest;

import com.fnranked.ranked.data.FortnitePresence;
import org.springframework.web.bind.annotation.*;

@RestController
public class FortnitePresenceController {

    @RequestMapping("/presence")
    public FortnitePresence presenceReceived(@RequestHeader(value = "accountId", defaultValue = "") String accountId,
                                             @RequestHeader(value= "sessionId", defaultValue = "") String sessionId,
                                             @RequestHeader(value= "partyId", defaultValue = "") String partyId,
                                             @RequestHeader(value= "kills", defaultValue = "0") int kills) {
        return new FortnitePresence(accountId, sessionId, partyId, kills);
    }
}
