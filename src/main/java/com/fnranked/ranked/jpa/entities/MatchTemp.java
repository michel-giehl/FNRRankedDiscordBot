package com.fnranked.ranked.jpa.entities;

import com.fnranked.ranked.data.MatchVote;
import com.fnranked.ranked.data.Region;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

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

    Region region;

    @OneToOne
    Team teamA;

    @OneToOne
    Team teamB;

    MatchVote teamAVote;

    MatchVote teamBVote;

    boolean teamAAccepted;

    boolean teamBAccepted;

    long matchChannelId;

    long supportChannelId;

    @OneToOne
    MatchServer matchServer;

    long voteMessageId;

    @OneToMany(cascade = CascadeType.ALL)
    List<MatchMessages> matchAcceptMessages;

    public MatchTemp() {
    }

    public MatchTemp(MatchType matchType, CreativeMap creativeMap, Team teamA, Team teamB) {
        this.matchType = matchType;
        this.teamA = teamA;
        this.teamB = teamB;
        this.teamAVote = MatchVote.PENDING;
        this.teamBVote = MatchVote.PENDING;
        this.teamAAccepted = false;
        this.teamBAccepted = false;
        this.map = creativeMap;
        this.startingTime = Timestamp.from(Instant.now());
        this.matchAcceptMessages = new ArrayList<>();
    }

    public MatchTemp(long guildId) {
        this.guildId = guildId;
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

    public void setId(long id) {
        Id = id;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
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
        return teamBVote;
    }

    public void setTeamBVote(MatchVote teamBVote) {
        this.teamBVote = teamBVote;
    }

    public void setMatchAcceptMessages(List<MatchMessages> matchMessages) {
        this.matchAcceptMessages = matchMessages;
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

    public List<MatchMessages> getMatchAcceptMessages() {
        return matchAcceptMessages;
    }
}
