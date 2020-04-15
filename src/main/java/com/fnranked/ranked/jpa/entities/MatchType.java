package com.fnranked.ranked.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class MatchType {

    @Id
    @GeneratedValue
    long id;

    int teamSize;

    String name;

    String season;

    @OneToMany
    List<CreativeMap> mapPool;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
