package com.fnranked.ranked.jpa.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class MatchServer {

    @Id
    long Id;

    String inviteUrl;
}
