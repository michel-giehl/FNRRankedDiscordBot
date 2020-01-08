package com.fnranked.ranked.rest;

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
public class FortnitePresence {

    private final String sessionId;
    private final String partyId;
    private final String accountId;

    private final int kills;

    public FortnitePresence(String accountId, String sessionId, String partyId, int kills) {
        this.accountId = accountId;
        this.sessionId = sessionId;
        this.partyId = partyId;
        this.kills = kills;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public String getPartyId() {
        return this.partyId;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public int getKills() {
        return kills;
    }
}
