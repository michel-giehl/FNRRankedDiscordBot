package com.fnranked.ranked.jpa.entities;

import com.fnranked.ranked.api.entities.Region;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Queue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @OneToOne
    MatchType matchType;

    Region region;

    boolean enabled;

    boolean inputMethodLocked;

    String inputMethod;

    String estimatedQueueTime;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<QueuedTeam> queueing;
}
