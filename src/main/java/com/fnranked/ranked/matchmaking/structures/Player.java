package com.fnranked.ranked.matchmaking.structures;

import com.fnranked.ranked.epic.EpicUserData;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public interface Player {

    Member getMember();

    EpicUserData getEpic();

    void setVoteMessage(Message voteMessage);

    @Override
    String toString();

}