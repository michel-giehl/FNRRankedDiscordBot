package com.fnranked.ranked.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CreativeMap {

    @Id
    @GeneratedValue
    long Id;

    String mapCode;

    String description;

    boolean aimAssistEnabled;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
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
