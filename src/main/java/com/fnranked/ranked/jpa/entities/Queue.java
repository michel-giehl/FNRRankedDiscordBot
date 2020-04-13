package com.fnranked.ranked.jpa.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Queue {

    @Id
    @GeneratedValue
    long Id;

    @OneToOne
    MatchType matchType;

    String region;

    boolean enabled;

    @OneToMany
    List<Team> queueing;

    @OneToMany
    List<QueueMessage> queueMessages;

}
