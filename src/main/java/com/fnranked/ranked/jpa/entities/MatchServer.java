package com.fnranked.ranked.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MatchServer {

    @Id
    long Id;

    String inviteUrl;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getInviteUrl() {
        return inviteUrl;
    }

    public void setInviteUrl(String inviteUrl) {
        this.inviteUrl = inviteUrl;
    }
}
