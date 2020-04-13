package com.fnranked.ranked.jpa.entities;

import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class QueueMessage {

    @Id
    long queueMessageId;

    @OneToOne
    @NonNull
    Player player;
}
