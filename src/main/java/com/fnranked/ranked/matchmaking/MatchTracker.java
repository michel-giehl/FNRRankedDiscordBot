//package com.fnranked.ranked.matchmaking;
//
//import com.fnranked.ranked.data.User;
//import com.fnranked.ranked.matchmaking.structures.Match;
//import com.fnranked.ranked.matchmaking.structures.Team;
//import com.fnranked.ranked.data.FortnitePresence;
//import com.google.common.collect.Lists;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.annotation.Id;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * @author GiM
// * MatchTracking instance - used to automatically track match scores
// */
//@SuppressWarnings("all")
//public class MatchTracker {
//
//    private static Logger logger = LoggerFactory.getLogger("MatchTracker");
//
//    private static List<MatchTracker> trackingInstances = new ArrayList<>();
//
//    private Match match;
//
//    @Id
//    private String sessionId;
//
//    private Map<Long, String> userSessionIds;
//
//    private boolean enabled;
//
//    private Map<Team, Integer> currentScore;
//
//    private int teamSize;
//
//    private int requiredRounds;
//
//    private List<FortnitePresence> currentRoundPresences;
//
//    /**
//     * Call this to start MatchTracking
//     *
//     * @param match match to be tracked
//     * @param requiredRounds amount of rounds a team has to win to win the match
//     */
//    public MatchTracker(Match match, int requiredRounds) {
//        this.match = match;
//        this.requiredRounds = requiredRounds;
//        this.teamSize = match.teamA().players().size();
//        this.currentRoundPresences = new LinkedList<>();
//        trackingInstances.add(this);
//    }
//
//    public void onPresenceReceived(FortnitePresence presence) {
//        User user = findUserByEpicAccountId(presence.getAccountId());
//        if(!sessionIdsMatches(user)) {
//            return;
//        }
//        //find old presence
//        var prevPresence = findPresenceForAccountId(presence.getAccountId());
//        //register current presence
//        registerPresence(presence);
//        if(prevPresence.isEmpty()) {
//            return;
//        }
//        var prevKills = prevPresence.get().getKills();
//        var currKills = presence.getKills();
//        //one user can max. get this.teamSize kills per round.
//        if(currKills > prevKills && Math.abs(currKills - prevKills) <= this.teamSize) {
//            if(isRoundFinished(presence)) {
//                //has to be available, otherwise isRoundFinished would be false.
//                var teamReceived = presenceReceivedOfTeam(presence).get();
//                updateScores(teamReceived);
//                //TODO check scores for winner
//            }
//        }
//    }
//
//    /**
//     * Updates scores and resets {@this.currentScore}
//     * @param winningTeam team to win the current round
//     */
//    private void updateScores(Team winningTeam) {
//        this.currentRoundPresences.clear();
//        var currScore = 1;
//        if(this.currentScore.containsKey(winningTeam)) {
//            currScore = currentScore.get(winningTeam) + 1;
//        }
//        this.currentScore.put(winningTeam, currScore);
//    }
//
//    private void registerPresence(FortnitePresence presence) {
//        this.currentRoundPresences.add(presence);
//    }
//
//    private Optional<FortnitePresence> findPresenceForAccountId(String accountId) {
//        Optional<FortnitePresence> presence = Optional.empty();
//        if(currentRoundPresences.isEmpty()) {
//            return presence;
//        }
//        //reverse list -> findFirst() returns latest presence.
//        return Lists.reverse(currentRoundPresences).stream().filter(p -> p.getAccountId().equals(accountId))
//                .findFirst();
//    }
//
//    private User findUserByEpicAccountId(String epicAccountId) {
//        return null;
//    }
//
//    /**
//     * Check if session Ids match. Cancel MatchTracking if they don't.
//     * TODO Reset scores while session Id's don't match to avoid cancelling.
//     * @param user user to check
//     * @return boolean
//     */
//    private boolean sessionIdsMatches(User user) {
//        var discordId = user.getDiscordID();
//        if(this.userSessionIds.containsKey(discordId)) {
//            var userSessionId = userSessionIds.get(discordId);
//            return userSessionId.equals(this.sessionId);
//        }
//        return false;
//    }
//
//    /**
//     * Used to track whether the current kill lead to a round being finished.
//     * Only required for team sizes > 1, as one round can have up to (teamSize*2)-1 kills
//     * before ending, which means you can't just track kills, you have to detect round ends.
//     *
//     * @param presenceReceived presence that was received
//     * @return true if current round is finished.
//     */
//    private boolean isRoundFinished(FortnitePresence presenceReceived) {
//        //Assuming this method is only called if new kills > old kills,
//        //for 1v1s, this will always result in a round finish.
//        if(teamSize == 1) {
//            return true;
//        }
//        //Team Size > 1, meaning currentRoundPresences can't be empty when a round ends.
//        if(currentRoundPresences.isEmpty()) {
//            currentRoundPresences.add(presenceReceived);
//            return false;
//        }
//        var uniqueKillsReceivedThisRound = currentRoundPresences.size();
//        var maxKillsUntilRoundFinishes = (2*this.teamSize)-1;
//        if(uniqueKillsReceivedThisRound >= this.teamSize && uniqueKillsReceivedThisRound <= maxKillsUntilRoundFinishes) {
//            var optTeamReceived = presenceReceivedOfTeam(presenceReceived);
//            //no team received - something went wrong
//            if(optTeamReceived.isEmpty()) {
//                return false;
//            }
//            var teamReceived = optTeamReceived.get();
//            //epic ids of received team
//            var teamReceivedEpicIds = teamReceived.players().stream().map(p -> p.getEpic().epicId()).collect(Collectors.toUnmodifiableSet());
//            var exactKillsRequired = this.teamSize;
//            //filter all kills by teamReceived
//            var teamKillsThisRound = currentRoundPresences.stream().map(FortnitePresence::getAccountId)
//                    .filter(teamReceivedEpicIds::contains).count();
//            return teamKillsThisRound == exactKillsRequired;
//        }
//        //at this point, round can't be finished, as most likely presences received is smaller than required amount.
//        return false;
//    }
//
//    /**
//     * Finds the team of a user based on a FortnitePresence object.
//     *
//     * @param presence presence of a user
//     * @return team of that user
//     */
//    private Optional<Team> presenceReceivedOfTeam(FortnitePresence presence) {
//        var accountId = presence.getAccountId();
//        var teamA = this.match.teamA();
//        var teamB = this.match.teamB();
//        var teamAEpicIds = teamA.players().stream().map(p -> p.getEpic().epicId()).collect(Collectors.toUnmodifiableSet());
//        var teamBEpicIds = teamB.players().stream().map(p -> p.getEpic().epicId()).collect(Collectors.toUnmodifiableSet());
//        Optional<Team> teamReceived = Optional.empty();
//        if(teamAEpicIds.contains(accountId)) {
//            teamReceived = Optional.of(teamA);
//        } else if(teamBEpicIds.contains(accountId)) {
//            teamReceived = Optional.of(teamB);
//        }
//        return teamReceived;
//    }
//}
