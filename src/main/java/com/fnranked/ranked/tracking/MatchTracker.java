package com.fnranked.ranked.tracking;

import com.fnranked.ranked.jpa.repo.FortnitePresenceRepository;
import com.fnranked.ranked.jpa.repo.MatchTempRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author GiM
 * MatchTracker
 * This will be called once a new presence was received
 * and will run the logic to determine the score of the match.
 */
@RestController
public class MatchTracker {

    //private static Logger logger = LoggerFactory.getLogger("MatchTracker");

    @Autowired
    MatchTempRepository matchTempRepository;
    @Autowired
    FortnitePresenceRepository fortnitePresenceRepository;
}
