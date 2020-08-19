package com.fnranked.ranked.jpa.entities;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class CreativeMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @OneToOne
    MatchType matchType;

    String mapCode;

    String description;

    boolean aimAssistEnabled;
}
