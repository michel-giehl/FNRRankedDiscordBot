package com.fnranked.ranked.jpa.entities;

import com.fnranked.ranked.api.entities.MatchStatus;
import com.fnranked.ranked.api.entities.Region;
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

    double eloMultiplier;

    @OneToOne
    MatchType matchType;

    @Nullable
    Region region;

    public RankedMatch() {
    }

    /**
     * Used to create a new Match
     *
     */
    public RankedMatch(MatchTemp matchTemp, @Nullable Team winner, MatchStatus status) {
        this.Id = matchTemp.getId();
        this.teamA = matchTemp.getTeamA();
        this.teamB = matchTemp.getTeamB();
        this.matchType = matchTemp.getMatchType();
        this.region = matchTemp.getRegion();
        this.status = status;
        this.endingTime = Timestamp.from(Instant.now());
        this.startingTime = matchTemp.getStartingTime();
        if(winner != null) {
            this.winner = winner;
        }
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

    public double getEloMultiplier() {
        return eloMultiplier;
    }

    public void setEloMultiplier(double eloMultiplier) {
        this.eloMultiplier = eloMultiplier;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
