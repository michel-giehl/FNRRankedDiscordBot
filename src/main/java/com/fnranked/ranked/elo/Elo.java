package com.fnranked.ranked.elo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Elo {

    /**
     * User ID, most likely discord id?
     * We could still stick to discord, even if we decide to move to a website. We could make use of discords OAauth2
     * so we can still use the same databases and allow for easy "cross platform" support. Many users probably
     * want to stick to discord, as it's more convenient (in-game overlay, everyone has it and knows how it works).
     */
    @Id
    private long userId;

    /**
     * TODO Rework entire elo system
     *      I really want to use Glicko-2, but I still don't know how to proerply implement it :/
     */
    private double elo;

    private EloType eloType;


    public Elo() {
    }
}
