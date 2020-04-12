package com.fnranked.ranked.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.util.List;

/**
 * Used to store temporary Match Data.
 */
@Entity
public class MatchTemp {

    @Id
    long Id;

    @OneToOne
    Team teamAVote;

    @OneToOne
    Team teamBVote;

    @ManyToMany
    List<VoteMessage> voteMessages;
}
