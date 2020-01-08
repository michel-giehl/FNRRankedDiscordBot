package com.fnranked.ranked.matchmaking.matches;

import com.fnranked.ranked.data.Region;
import com.fnranked.ranked.matchmaking.MatchTracker;
import com.fnranked.ranked.matchmaking.structures.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.awt.*;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class Boxfights_1v1_DM implements Match {

    private long id;
    private Region region;
    private String mapCode;
    private MatchType type;
    private Color color;

    private TextChannel matchChannel;
    private VoiceChannel voiceChannel;

    private Message voteMessage;
    private Collection<String> voteMessages;

    private Map<Team, Team> winnerVotes;
    private Collection<User> accepted;

    private MatchState state;

    private Team a;
    private Team b;
    private Team winningTeam;


    @Override
    public long id() {
        return this.id;
    }

    @Override
    public Region region() {
        return region;
    }

    @Override
    public String mapCode() {
        return mapCode;
    }

    @Override
    public Color color() {
        return color;
    }

    @Override
    public MatchTracker matchTracker() {
        return null;
    }

    @Override
    public TextChannel matchChannel() {
        return matchChannel;
    }

    @Override
    public Optional<VoiceChannel> voiceChannel() {
        return Optional.ofNullable(this.voiceChannel);
    }

    @Override
    public Message voteMessage() {
        return voteMessage;
    }

    @Override
    public Map<Team, Team> winnerSelected() {
        return winnerVotes;
    }

    @Override
    public Collection<User> acceptedTeams() {
        return accepted;
    }

    @Override
    public Collection<String> voteMessages() {
        return voteMessages;
    }

    @Override
    public Team teamA() {
        return a;
    }

    @Override
    public Team teamB() {
        return b;
    }

    @Override
    public Optional<Team> winningTeam() {
        return Optional.ofNullable(winningTeam);
    }

    @Override
    public MatchMode mode() {
        return MatchMode.PRIVATE_CHANNEL;
    }

    @Override
    public MatchType type() {
        return type;
    }

    @Override
    public MatchState state() {
        return state;
    }

    @Override
    public void setChannel(TextChannel matchChannel) {
        this.matchChannel = matchChannel;
    }

    @Override
    public void setVoiceChannel(VoiceChannel voiceChannel) {
        this.voiceChannel = voiceChannel;
    }

    @Override
    public void setVoteMessage(Message voteMessage) {
        this.voteMessage = voteMessage;
    }

    @Override
    public void setWinner(Team winningTeam) {
        this.winningTeam = winningTeam;
        finish();
    }

    @Override
    public void setAccepted(User user) {
        this.accepted.add(user);
    }

    @Override
    public void addVoteMessage(String messageID) {
        this.voteMessages.add(messageID);
    }

    @Override
    public void setState(MatchState matchState) {
        this.state = matchState;
    }

    @Override
    public void sendMessage() {

    }

    @Override
    public void finish() {

    }
}
