package com.fnranked.ranked.jpa.entities;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.sql.Timestamp;

@Entity
public class MatchmakingBan {

    @Id
    @GeneratedValue
    long Id;

    @OneToOne
    Player player;

    @NonNull
    Timestamp timeOfBan;

    @Nullable
    Timestamp banUntil;
}
