package com.fnranked.ranked.jpa.entities;

import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class MatchmakingBan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    boolean banned;

    @Nullable
    @OneToOne
    Player staffMember;

    @OneToOne
    Player player;

    long durationMillis;

    @NonNull
    Timestamp timeOfBan;

    @Column(columnDefinition = "VARCHAR(1000)")
    String reason;
}
