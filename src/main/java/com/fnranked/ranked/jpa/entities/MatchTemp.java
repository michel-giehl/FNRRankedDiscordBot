package com.fnranked.ranked.jpa.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Used to store temporary Match Data.
 */
@Entity
public class MatchTemp {

    @Id
    @GeneratedValue
    long Id;

    long guildId;

    Timestamp startingTime;

    @OneToOne
    MatchType matchType;

    String region;

    @OneToOne
    Team teamA;

    @OneToOne
    Team teamB;

    @OneToOne
    Team teamAVote;

    @OneToOne
    Team TeamBVote;

    boolean teamAAccepted;

    boolean teamBAccepted;

    long supportChannelId;

    @OneToMany
    List<VoteMessage> voteMessages;

    @OneToMany
    List<MatchMessages> matchMessages;

    public MatchTemp() {
    }

    public MatchTemp(long guildId) {
        this.guildId = guildId;
        this.startingTime = Timestamp.from(Instant.now());
    }

    public long getId() {
        return Id;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public Timestamp getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(Timestamp startingTime) {
        this.startingTime = startingTime;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Team getTeamA() {
        return teamA;
    }

    public void setTeamA(Team teamA) {
        this.teamA = teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public void setTeamB(Team teamB) {
        this.teamB = teamB;
    }

    public Team getTeamAVote() {
        return teamAVote;
    }

    public void setTeamAVote(Team teamAVote) {
        this.teamAVote = teamAVote;
    }

    public Team getTeamBVote() {
        return TeamBVote;
    }

    public void setTeamBVote(Team teamBVote) {
        TeamBVote = teamBVote;
    }

    public boolean isTeamAAccepted() {
        return teamAAccepted;
    }

    public void setTeamAAccepted(boolean teamAAccepted) {
        this.teamAAccepted = teamAAccepted;
    }

    public boolean isTeamBAccepted() {
        return teamBAccepted;
    }

    public void setTeamBAccepted(boolean teamBAccepted) {
        this.teamBAccepted = teamBAccepted;
    }

    public long getSupportChannelId() {
        return supportChannelId;
    }

    public void setSupportChannelId(long supportChannelId) {
        this.supportChannelId = supportChannelId;
    }

    public List<VoteMessage> getVoteMessages() {
        return voteMessages;
    }

    public void setVoteMessages(List<VoteMessage> voteMessages) {
        this.voteMessages = voteMessages;
    }

    public List<MatchMessages> getMatchMessages() {
        return matchMessages;
    }
}
