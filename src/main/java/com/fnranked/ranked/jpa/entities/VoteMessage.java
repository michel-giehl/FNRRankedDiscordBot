package com.fnranked.ranked.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class VoteMessage {

    @Id
    long messageId;

    @OneToOne
    RankedMatch rankedMatch;

    @OneToOne
    Player player;
}
