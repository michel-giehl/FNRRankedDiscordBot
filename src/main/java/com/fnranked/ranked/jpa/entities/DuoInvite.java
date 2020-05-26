package com.fnranked.ranked.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
public class DuoInvite {

    @Id
    @GeneratedValue
    long Id;

    long inviterId;

    long inviteeId;

    long messageId;

    Timestamp time;

    public DuoInvite() {

    }

    public DuoInvite(long inviterId, long inviteeId, long messageId) {
        this.inviteeId = inviteeId;
        this.inviterId = inviterId;
        this.messageId = messageId;
        this.time = Timestamp.from(Instant.now());
    }

    public long getInviterId() {
        return inviterId;
    }

    public long getInviteeId() {
        return inviteeId;
    }

    public Timestamp getTime() {
        return time;
    }

    public long getId() {
        return Id;
    }
}
