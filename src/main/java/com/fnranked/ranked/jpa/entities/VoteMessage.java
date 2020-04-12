package com.fnranked.ranked.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Represents a voting message in a Players private channel
 * Used to determine match winner if MatchTracker is not successful.
 *
 * Message ID:
 *   - Discord ID of the message.
 *   - RankedMatch Reference to the match the message is for
 *   - Player Used to determine who's vote message it is. NULL if match channels are used.
 */
@Entity
public class VoteMessage {

    @Id
    long messageId;

    @OneToOne
    RankedMatch rankedMatch;

    @OneToOne
    Player player;
}
