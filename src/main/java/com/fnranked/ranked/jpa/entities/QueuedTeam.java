package com.fnranked.ranked.jpa.entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Entity
@Data
public class QueuedTeam {

    @Id
    @GeneratedValue
    long id;

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
}
