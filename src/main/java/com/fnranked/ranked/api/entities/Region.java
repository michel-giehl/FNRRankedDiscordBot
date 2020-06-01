package com.fnranked.ranked.api.entities;

import java.util.Arrays;

public enum  Region {
    EUROPE("EU"),
    NA_WEST("NAW"),
    NA_EAST("NAE"),
    OCEANIA("OCE"),
    ASIA("ASIA"),
    BRAZIL("BR"),
    STAFF("STAFF"),
    MIDDLE_EAST("ME");

    private final String region;

    Region(String region) {
        this.region = region;
    }

    public static Region parseRegion(String region) {
        switch (region.toUpperCase()) {
            case "EU":
                return EUROPE;
            case "NAE":
                return NA_EAST;
            case "NAW":
                return NA_WEST;
            case "OCE":
                return OCEANIA;
            case "ASIA":
                return ASIA;
            case "BR":
                return BRAZIL;
            case "ME":
                return MIDDLE_EAST;
            default:
                return STAFF;
        }
        //return Arrays.stream(Region.values()).filter(r -> r.toString().equalsIgnoreCase(region)).findFirst().get();
    }


    @Override
    public String toString() {
        return region;
    }
}
