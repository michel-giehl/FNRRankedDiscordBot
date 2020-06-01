package com.fnranked.ranked.jpa.entities;

import com.fnranked.ranked.api.entities.Region;

import javax.persistence.*;
import java.util.List;

@Entity
public class Queue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long Id;

    @OneToOne
    MatchType matchType;

    Region region;

    boolean enabled;

    boolean inputMethodLocked;

    String inputMethod;

    String estimatedQueueTime;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<QueuedTeam> queueing;

    public long getId() {
        return Id;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isInputMethodLocked() {
        return inputMethodLocked;
    }

    public void setInputMethodLocked(boolean inputMethodLocked) {
        this.inputMethodLocked = inputMethodLocked;
    }

    public String getInputMethod() {
        return inputMethod;
    }

    public void setInputMethod(String inputMethod) {
        this.inputMethod = inputMethod;
    }

    public List<QueuedTeam> getQueueing() {
        return queueing;
    }

    public void setQueueing(List<QueuedTeam> queueing) {
        this.queueing = queueing;
    }

    public String getEstimatedQueueTime() {
        return estimatedQueueTime;
    }

    public void setEstimatedQueueTime(String estimatedQueueTime) {
        this.estimatedQueueTime = estimatedQueueTime;
    }
}
