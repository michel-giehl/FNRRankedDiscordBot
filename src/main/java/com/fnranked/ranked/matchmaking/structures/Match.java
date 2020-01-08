package com.fnranked.ranked.matchmaking.structures;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Match {

    long id();
    String region();
    String mapCode();
    Color color();

    TextChannel matchChannel();
    Optional<VoiceChannel> voiceChannel();
    Message voteMessage();
    Map<Team, Team> winnerSelected();
    Collection<User> acceptedTeams();
    Collection<String> voteMessages();

    Team teamA();
    Team teamB();
    Optional<Team> winningTeam();

    MatchMode mode();
    MatchType type();
    MatchState state();

    void setChannel(TextChannel matchChannel);

    void setVoiceChannel(VoiceChannel voiceChannel);

    void setVoteMessage(Message voteMessage);

    void setWinner(Player winner);

    void setAccepted(User user);

    void addVoteMessage(String messageID);

    void setState(MatchState matchState);

    void sendMessage();

    void finish();
}
