package com.fnranked.ranked.data;

public enum TournamentMode {

    KO("K.O"),
    ROUNDS("Rounds"),
    SWISS("Swiss"),
    LADDER("Ladder");

    private final String text;

    TournamentMode(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
