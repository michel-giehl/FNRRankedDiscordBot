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


    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MatchTemp getMatchTemp() {
        return matchTemp;
    }

    public void setMatchTemp(MatchTemp matchTemp) {
        this.matchTemp = matchTemp;
    }
}
