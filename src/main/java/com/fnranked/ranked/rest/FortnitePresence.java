package com.fnranked.ranked.rest;

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
