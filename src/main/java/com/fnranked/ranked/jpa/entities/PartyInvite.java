package com.fnranked.ranked.jpa.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Data
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
}
