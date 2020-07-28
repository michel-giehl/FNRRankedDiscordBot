package com.fnranked.ranked.jpa.entities;

import javax.persistence.*;

@Entity
public class CreativeMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @OneToOne
    MatchType matchType;

    String mapCode;

    String description;

    boolean aimAssistEnabled;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMapCode() {
        return mapCode;
    }

    public void setMapCode(String mapCode) {
        this.mapCode = mapCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAimAssistEnabled() {
        return aimAssistEnabled;
    }

    public void setAimAssistEnabled(boolean aimAssistEnabled) {
        this.aimAssistEnabled = aimAssistEnabled;
    }
}
