package com.fnranked.ranked.matchmaking.structures;

public enum MatchType {
    BUILDFIGHTS("buildfights"),
    BOXFIGHTS("boxfights"),
    BOXFIGHTS_2V2("boxfights_2v2");

    private final String text;

    MatchType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static MatchType get(String text) {
        text = text.toLowerCase();
        if(text.contains("box")) {
            if(text.contains("2v2")) {
                return BOXFIGHTS_2V2;
            } else {
                return BOXFIGHTS;
            }
        } else if(text.contains("bf") || text.contains("build")) {
            return BUILDFIGHTS;
        }
        return null;
    }
}
