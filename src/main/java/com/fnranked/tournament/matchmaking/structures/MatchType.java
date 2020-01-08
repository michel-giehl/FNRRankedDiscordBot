package com.fnranked.tournament.matchmaking.structures;

public enum MatchType {
    TOURNAMENT("tournament");

    private final String text;

    MatchType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
