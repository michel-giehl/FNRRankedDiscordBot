package com.fnranked.ranked.data;

public enum TeamSize {
    SOLO(1, "1v1"),
    DUO(2, "2v2"),
    TRIO(3, "3v3"),
    SQUAD(4, "4v4");

    private int teamSize;
    private String text;

    TeamSize(int teamSize, String text) {
        this.teamSize = teamSize;
        this.text = text;
    }

    public static TeamSize fromInt(int teamSize) {
        return values()[teamSize-1];
    }

    public int toInt() {
        return teamSize;
    }

    @Override
    public String toString() {
        return text;
    }
}
