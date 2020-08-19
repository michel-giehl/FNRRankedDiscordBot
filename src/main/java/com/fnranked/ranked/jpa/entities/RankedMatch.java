package com.fnranked.ranked.jpa.entities;

import com.fnranked.ranked.api.entities.MatchStatus;
import com.fnranked.ranked.api.entities.Region;
import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
/**
 * Represents an ongoing or completed Ranked Match.
 *
 * @author Michel
 * @version 1.0
 */
@Entity
@Data
public class RankedMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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


    public RankedMatch(Team winner, Team loser, MatchType matchType, Region region, Instant time) {
        this.winner = winner;
        this.teamA = winner;
        this.status = MatchStatus.FINISHED;
        this.teamB = loser;
        this.matchType = matchType;
        this.region = region;
        this.endingTime = Timestamp.from(time);
        this.startingTime = this.endingTime;

    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RankedMatch && this.Id == ((RankedMatch)obj).getId();
    }
}
