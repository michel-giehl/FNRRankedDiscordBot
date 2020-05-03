package com.fnranked.ranked.jpa.entities;

import com.fnranked.ranked.data.MatchVote;
import org.hibernate.annotations.ColumnDefault;

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

    @OneToOne
    CreativeMap map;

    String region;

    @OneToOne
    Team teamA;

    @OneToOne
    Team teamB;

    MatchVote teamAVote;

    MatchVote TeamBVote;

    boolean teamAAccepted;

    boolean teamBAccepted;

    long matchChannelId;

    long supportChannelId;

    @OneToOne
    MatchServer matchServer;

    long voteMessageId;

    @OneToMany
    List<MatchMessages> matchMessages;

    public MatchTemp() {
    }

    public MatchTemp(long guildId) {
        this.guildId = guildId;
        this.startingTime = Timestamp.from(Instant.now());
    }

    public CreativeMap getMap() {
        return map;
    }

    public void setMap(CreativeMap map) {
        this.map = map;
    }

    public MatchServer getMatchServer() {
        return matchServer;
    }

    public void setMatchServer(MatchServer matchServer) {
        this.matchServer = matchServer;
    }

    public long getId() {
        return Id;
    }

    public long getMatchChannelId() {
        return matchChannelId;
    }

    public void setMatchChannelId(long matchChannelId) {
        this.matchChannelId = matchChannelId;
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

    public MatchVote getTeamAVote() {
        return teamAVote;
    }

    public void setTeamAVote(MatchVote teamAVote) {
        this.teamAVote = teamAVote;
    }

    public MatchVote getTeamBVote() {
        return TeamBVote;
    }

    public void setTeamBVote(MatchVote teamBVote) {
        TeamBVote = teamBVote;
    }

    public void setMatchMessages(List<MatchMessages> matchMessages) {
        this.matchMessages = matchMessages;
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

    public long getVoteMessageId() {
        return voteMessageId;
    }

    public void setVoteMessageId(long voteMessageId) {
        this.voteMessageId = voteMessageId;
    }

    public List<MatchMessages> getMatchMessages() {
        return matchMessages;
    }
}
