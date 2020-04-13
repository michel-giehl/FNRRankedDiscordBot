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
}
