package com.fnranked.ranked.jpa.entities;

import com.fnranked.ranked.api.entities.MatchVote;
import com.fnranked.ranked.api.entities.Region;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to store temporary Match Data.
 */
@Entity
@Data
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

    int teamAScore;

    int teamBScore;

    MatchVote teamAVote;

    MatchVote teamBVote;

    boolean teamAAccepted;

    boolean teamBAccepted;

    long matchChannelId;

    long supportChannelId;

    @OneToOne
    MatchServer matchServer;

    long voteMessageId;

    @ManyToMany(cascade = CascadeType.ALL)
    List<FortnitePresence> matchPresences;

    @ManyToMany(cascade = CascadeType.PERSIST)
    List<FortnitePresence> currentRoundPresences;

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
}
