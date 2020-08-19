package com.fnranked.ranked.jpa.entities;

import lombok.Data;
import net.dv8tion.jda.internal.utils.EncodingUtil;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class MatchType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    int teamSize;

    int requiredRoundsToWin;

    String name;

    int season;

    String displayEmote;

    @OneToMany
    List<CreativeMap> mapPool;

    public String getDisplayEmote() {
        return EncodingUtil.decodeCodepoint(displayEmote);
    }

    public void setDisplayEmote(String displayEmote) {
        this.displayEmote = EncodingUtil.encodeCodepoints(displayEmote);
    }

    @Override
    public boolean equals(Object obj) {
        return this.id == ((MatchType)obj).getId();
    }
}
