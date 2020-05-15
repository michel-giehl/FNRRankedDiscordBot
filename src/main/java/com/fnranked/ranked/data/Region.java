package com.fnranked.ranked.data;

public enum  Region {
    EUROPE("EU"),
    NA_WEST("NAW"),
    NA_EAST("NAE"),
    OCEANIA("OCE"),
    ASIA("ASIA"),
    MIDDLE_EAST("ME");

    private final String region;

    Region(String region) {
        this.region = region;
    }


    @Override
    public String toString() {
        return region;
    }
}
