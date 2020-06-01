package com.fnranked.ranked.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
public class PartyInvite {

    @Id
    @GeneratedValue
    long id;

    long inviterId;

    long inviteeId;

    long messageId;

    Timestamp time;

    @ManyToOne
    Party party;

    public PartyInvite() {

    }

    public PartyInvite(Party party, long inviterId, long inviteeId, long messageId) {
        this.party = party;
        this.inviteeId = inviteeId;
        this.inviterId = inviterId;
        this.messageId = messageId;
        this.time = Timestamp.from(Instant.now());
    }

    public Party getParty() {
        return party;
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
        return id;
    }
}
