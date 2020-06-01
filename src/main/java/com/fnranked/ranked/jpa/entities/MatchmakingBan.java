package com.fnranked.ranked.jpa.entities;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class MatchmakingBan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long Id;

    @OneToOne
    Player player;

    @NonNull
    Timestamp timeOfBan;

    @Nullable
    Timestamp banUntil;
}
