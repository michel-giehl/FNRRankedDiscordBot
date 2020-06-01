package com.fnranked.ranked.jpa.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Entity
public class QueuedTeam {

    @Id
    @GeneratedValue
    long Id;

    @OneToOne
    Team team;

    Timestamp timeJoined;

    @OneToMany(cascade = CascadeType.ALL)
    List<MatchMessages> matchMessages;

    public QueuedTeam() {

    }

    public QueuedTeam(Team team) {
        this.team = team;
        this.timeJoined = Timestamp.from(Instant.now());
    }

    public long getId() {
        return Id;
    }

    public Team getTeam() {
        return team;
    }

    public Timestamp getTimeJoined() {
        return timeJoined;
    }

    public void setId(long id) {
        Id = id;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setTimeJoined(Timestamp timeJoined) {
        this.timeJoined = timeJoined;
    }

    public List<MatchMessages> getMatchMessages() {
        return matchMessages;
    }

    public void setMatchMessages(List<MatchMessages> matchMessages) {
        this.matchMessages = matchMessages;
    }
}
