package com.fnranked.ranked.api.entities;

public enum TeamSize {
    SOLO(1, "1v1"),
    DUO(2, "2v2"),
    TRIO(3, "3v3"),
    SQUAD(4, "4v4");

    private int teamSizeInt;
    private String text;

    TeamSize(int teamSizeInt, String text) {
        this.teamSizeInt = teamSizeInt;
        this.text = text;
    }

    public static TeamSize fromInt(int teamSize) {
        return values()[teamSize - 1];
    }

    public int toInt() {
        return teamSizeInt;
    }

    @Override
    public String toString() {
        return text;
    }
}
