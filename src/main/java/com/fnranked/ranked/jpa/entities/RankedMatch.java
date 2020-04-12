package com.fnranked.ranked.jpa.entities;

import com.fnranked.ranked.data.MatchStatus;
import com.fnranked.ranked.data.Region;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
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
    long Id;

    long guildId;

    Timestamp startingTime;

    Timestamp endingTime;

    MatchStatus status;

    @OneToOne
    @NonNull
    Team teamA;

    @OneToOne
    @NonNull
    Team teamB;

    @NonNull
    String matchType;

    @Nullable
    Region region;

    public RankedMatch() {
    }

    /**
     * Used to create a new Match
     *
     * @param region Where the match took place
     */
    public RankedMatch(long matchId, Team winningTeam, Team losingTeam, Timestamp startingTime, Region region, String matchType) {
        this.teamA = winningTeam;
        this.teamB = losingTeam;
        this.matchType = matchType;
        this.region = region;
        this.endingTime = Timestamp.from(Instant.now());
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

    public Region getRegion() {
        return region;
    }

    public Timestamp getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(Timestamp endingTime) {
        this.endingTime = endingTime;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public void setStartingTime(Timestamp startingTime) {
        this.startingTime = startingTime;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public void setTeamA(Team teamA) {
        this.teamA = teamA;
    }

    public void setTeamB(Team teamB) {
        this.teamB = teamB;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
