package com.fnranked.ranked.jpa.entities;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class MatchmakingBan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    boolean banned;

    @Nullable
    @OneToOne
    Player staffMember;

    @OneToOne
    Player player;

    long durationMillis;

    @NonNull
    Timestamp timeOfBan;

    @Column(columnDefinition = "VARCHAR(1000)")
    String reason;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @NonNull
    public Timestamp getTimeOfBan() {
        return timeOfBan;
    }

    public void setTimeOfBan(@NonNull Timestamp timeOfBan) {
        this.timeOfBan = timeOfBan;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    @Nullable
    public Player getStaffMember() {
        return staffMember;
    }

    public void setStaffMember(@Nullable Player staffMember) {
        this.staffMember = staffMember;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
