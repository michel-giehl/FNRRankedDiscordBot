package com.fnranked.ranked.matchmaking.structures;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public interface Player {

    Member getMember();

    EpicUserData getEpic();

    void setVoteMessage(Message voteMessage);

    @Override
    String toString();

}