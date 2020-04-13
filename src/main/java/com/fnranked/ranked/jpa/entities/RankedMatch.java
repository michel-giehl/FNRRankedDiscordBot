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

    @OneToOne
    @NonNull
    Team winner;

    @NonNull
    double teamAEloChange;

    @NonNull
    double teamBEloChange;

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
    public RankedMatch(long matchId, Team teamA, Team teamB, Timestamp startingTime, Region region, String matchType) {
        this.teamA = teamA;
        this.teamB = teamB;
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

    @NonNull
    public Team getTeamA() {
        return teamA;
    }

    public void setTeamA(@NonNull Team teamA) {
        this.teamA = teamA;
    }

    @NonNull
    public Team getTeamB() {
        return teamB;
    }

    public void setTeamB(@NonNull Team teamB) {
        this.teamB = teamB;
    }

    @NonNull
    public Team getWinner() {
        return winner;
    }

    public void setWinner(@NonNull Team winner) {
        this.winner = winner;
    }

    public double getTeamAEloChange() {
        return teamAEloChange;
    }

    public void setTeamAEloChange(double teamAEloChange) {
        this.teamAEloChange = teamAEloChange;
    }

    public double getTeamBEloChange() {
        return teamBEloChange;
    }

    public void setTeamBEloChange(double teamBEloChange) {
        this.teamBEloChange = teamBEloChange;
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
