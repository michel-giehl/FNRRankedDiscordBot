package com.fnranked.ranked.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
public class QueuedTeam {

    @Id
    @GeneratedValue
    long Id;

    @OneToOne
    Team team;

    Timestamp timeJoined;

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
}
