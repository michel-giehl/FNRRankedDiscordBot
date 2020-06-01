package com.fnranked.ranked.jpa.entities;

import com.fnranked.ranked.jpa.entities.MatchTemp;
import com.fnranked.ranked.jpa.entities.Player;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Represents a fortnite presence Object
 *
 * account ID - Epic Account that sent presence
 *            - used to identify who sent presence.
 *
 * Session ID - unique id for each fortnite game. Used to ensure all users are in the same game
 *            - Only set if users are in a game.
 *
 * Party ID   - Unique ID for each party, can be used to reset session ID's if one was set incorrectly.
 *            - Only set if user is in a party.
 *
 * kills      - amount of kills a user has.
 *            - used to track match rounds
 */
@Entity
public class FortnitePresence {

    @Id
    @GeneratedValue
    long Id;

    @OneToOne
    Player player;

    @OneToOne
    MatchTemp matchTemp;

    long accountId;

    @Nullable
    long sessionId;

    @Nullable
    long partyId;

    int kills;

}
