package com.fnranked.ranked.jpa.entities;

import com.fnranked.ranked.data.MatchType;
import com.fnranked.ranked.data.Region;
import com.fnranked.ranked.data.TeamSize;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.sql.Timestamp;
import java.time.Instant;
/**
 * Represents an ongoing or completed Ranked Match.
 *
 * @author Michel
 * @version 1.0
 */
@Entity
public class RankedMatch {

    @Id
    @GeneratedValue
    long Id;

    Timestamp startingTime;

    Timestamp endingTime;

    boolean completed;

    boolean cancelled;

    boolean teamAWon;

    @OneToOne
    Team teamA;

    @OneToOne
    Team teamB;

    MatchType matchType;

    TeamSize teamSize;

    Region region;

    public RankedMatch() {
    }

    /**
     * Used to create a new Match
     *
     * @param teamA team one
     * @param teamB team two
     * @param matchType type of the match
     * @param teamSize the team size of the match, since all Matches are represented by Teams, not Players
     * @param region Where the match took place
     */
    public RankedMatch(Team teamA, Team teamB, MatchType matchType, TeamSize teamSize, Region region) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.matchType = matchType;
        this.teamSize = teamSize;
        this.region = region;
        this.startingTime = Timestamp.from(Instant.now());
        this.completed = false;
        this.cancelled = false;
    }

    public long getId() {
        return Id;
    }

    public Timestamp getStartingTime() {
        return startingTime;
    }

    public Team getTeamA() {
        return teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public TeamSize getTeamSize() {
        return teamSize;
    }

    public Region getRegion() {
        return region;
    }

    public Timestamp getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(Timestamp endingTime) {
        this.endingTime = endingTime;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isTeamAWon() {
        return teamAWon;
    }

    public void setTeamAWon(boolean teamAWon) {
        this.teamAWon = teamAWon;
    }
}
