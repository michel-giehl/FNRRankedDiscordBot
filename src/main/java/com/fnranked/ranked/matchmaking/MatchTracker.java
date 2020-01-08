package com.fnranked.ranked.matchmaking;

import com.fnranked.ranked.matchmaking.structures.Match;
import com.fnranked.ranked.matchmaking.structures.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author GiM
 * MatchTracking instance - used to automatically track match scores
 */
public class MatchTracker {

    private static Logger logger = LoggerFactory.getLogger("MatchTracker");

    private static List<MatchTracker> trackingInstances = new ArrayList<>();

    private Match match;

    @Id
    private String sessionId;

    private Map<String, String> userSessionIds;

    private boolean enabled;

    private Map<Team, Integer> currentScore;

    private int teamSize;

    private int requiredRounds;

    /**
     * Call this to start MatchTracking
     *
     * @param match match to be tracked
     * @param requiredRounds amount of rounds a team has to win to win the match
     */
    public MatchTracker(Match match, int requiredRounds) {
        this.match = match;
        this.requiredRounds = requiredRounds;
        this.teamSize = match.teamA().players().size();
        trackingInstances.add(this);
    }

    public void onPresenceReceived() {

    }
}
