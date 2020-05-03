package com.fnranked.ranked.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MatchServer {

    @Id
    long Id;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }
}
