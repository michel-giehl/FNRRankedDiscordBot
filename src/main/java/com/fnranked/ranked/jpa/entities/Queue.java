package com.fnranked.ranked.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Queue {

    @Id
    @GeneratedValue
    long Id;

    String matchType;

    String region;

    boolean enabled;


    @OneToMany
    List<Team> queueing;

    @OneToMany
    List<QueueMessage> queueMessages;

}
