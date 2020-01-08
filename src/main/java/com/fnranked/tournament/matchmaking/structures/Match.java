package com.fnranked.tournament.matchmaking.structures;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.awt.*;
import java.util.List;
import java.util.Map;

public interface Match {

    String getID();
    String getRegion();
    String getMapCode();
    Color getColor();

    TextChannel getMatchChannel();
    VoiceChannel getVoiceChannel();
    Message getWinnerSelect();
    Map<Player, Player> winnerSelected();
    List<User> getAccepted();
    List<String> getVoteMessages();

    Player getPlayer1();
    Player getPlayer2();
    Player getWinner();

    MatchType getMatchType();
    MatchState getMatchState();

    void setMatchChannel(TextChannel matchChannel);

    void setVoiceChannel(VoiceChannel voiceChannel);

    void setWinnerSelectMessage(Message winnerSelectMessage);

    void setWinner(Player winner);

    void setAccepted(User user);

    void addVoteMessage(String messageID);

    void setMatchState(MatchState matchState);

    void sendMessage();

    void finish();
}
