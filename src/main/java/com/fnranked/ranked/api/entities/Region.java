package com.fnranked.ranked.api.entities;

import java.util.Arrays;

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

    public static Region parseRegion(String region) {
        return Arrays.stream(Region.values()).filter(r -> r.toString().equalsIgnoreCase(region)).findFirst().get();
    }


    @Override
    public String toString() {
        return region;
    }
}
