package com.fnranked.tournament.matchmaking.structures;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public interface Player {

    Member getMember();

    String getEpicDisplayName();

    void setVoteMessage(Message voteMessage);

    @Override
    String toString();

}