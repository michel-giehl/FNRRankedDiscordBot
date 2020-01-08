package com.fnranked.ranked.matchmaking.matches;

import com.fnranked.ranked.matchmaking.structures.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Boxfights implements Match {

    private long id;
    private String region;
    private String mapCode;
    private MatchType type;
    private Color color;

    private TextChannel matchChannel;
    private VoiceChannel voiceChannel;

    private Message voteMessage;
    private Collection<String> voteMessages;

    private Team a;
    private Team b;
    private Team winningTeam;


    @Override
    public long id() {
        return this.id;
    }

    @Override
    public String region() {
        return null;
    }

    @Override
    public String mapCode() {
        return null;
    }

    @Override
    public Color color() {
        return null;
    }

    @Override
    public TextChannel matchChannel() {
        return null;
    }

    @Override
    public Optional<VoiceChannel> voiceChannel() {
        return Optional.ofNullable(this.voiceChannel);
    }

    @Override
    public Message voteMessage() {
        return null;
    }

    @Override
    public Map<Team, Team> winnerSelected() {
        return null;
    }

    @Override
    public List<User> acceptedTeams() {
        return null;
    }

    @Override
    public List<String> voteMessages() {
        return null;
    }

    @Override
    public Team teamA() {
        return null;
    }

    @Override
    public Team teamB() {
        return null;
    }

    @Override
    public Optional<Team> winningTeam() {
        return Optional.empty();
    }

    @Override
    public MatchMode mode() {
        return MatchMode.GUILD_CHANNEL;
    }

    @Override
    public MatchType type() {
        return type;
    }

    @Override
    public MatchState state() {
        return null;
    }

    @Override
    public void setChannel(TextChannel matchChannel) {

    }

    @Override
    public void setVoiceChannel(VoiceChannel voiceChannel) {
        this.voiceChannel = voiceChannel;
    }

    @Override
    public void setVoteMessage(Message voteMessage) {
    }

    @Override
    public void setWinner(Player winner) {

    }

    @Override
    public void setAccepted(User user) {

    }

    @Override
    public void addVoteMessage(String messageID) {

    }

    @Override
    public void setState(MatchState matchState) {

    }

    @Override
    public void sendMessage() {

    }

    @Override
    public void finish() {

    }
}
