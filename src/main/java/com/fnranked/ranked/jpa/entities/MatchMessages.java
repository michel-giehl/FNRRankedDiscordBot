package com.fnranked.ranked.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class MatchMessages {

    /**
     * message Id used as ID
     */
    @Id
    long Id;

    long authorId;

    @Column(columnDefinition = "VARCHAR(2000)")
    String content;

    @OneToOne
    MatchTemp matchTemp;

}
