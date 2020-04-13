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

    long supportChannelId;

    @OneToMany
    List<VoteMessage> voteMessages;

    public MatchTemp() {
    }

    public MatchTemp(long guildId) {
        this.guildId = guildId;
        this.startingTime = Timestamp.from(Instant.now());
    }
}
