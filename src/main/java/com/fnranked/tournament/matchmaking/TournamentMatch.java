package com.fnranked.tournament.matchmaking;

import com.fnranked.tournament.matchmaking.structures.Match;
import com.fnranked.tournament.matchmaking.structures.MatchState;
import com.fnranked.tournament.matchmaking.structures.MatchType;
import com.fnranked.tournament.matchmaking.structures.Player;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TournamentMatch implements Match {

    private String matchID;
    private String region;
    private String mapCode;

    private TextChannel matchChannel;
    private VoiceChannel voiceChannel;
    private Message winnerSelect;
    private List<User> accepted;
    private List<String> voteMessages;
    private HashMap<Player, Player> winnerSelected;

    private Player player1;
    private Player player2;
    private Player winner;

    private MatchState matchState;

    public TournamentMatch() {}

    public TournamentMatch(Player player1, Player player2, String region) {
        this.matchID = String.valueOf(System.currentTimeMillis());
        this.player1 = player1;
        this.player2 = player2;
        this.region = region;
        this.mapCode = "BLA";
        this.accepted = new ArrayList<>();
        this.voteMessages = new ArrayList<>();
        winnerSelected = new HashMap<>();
    }


    @Override
    public String getID() {
        return matchID;
    }

    @Override
    public String getRegion() {
        return region;
    }

    @Override
    public String getMapCode() {
        return mapCode;
    }

    @Override
    public Color getColor() {
        return new Color(0xFF8800);
    }

    @Override
    public TextChannel getMatchChannel() {
        return matchChannel;
    }

    @Override
    public VoiceChannel getVoiceChannel() {
        return voiceChannel;
    }

    @Override
    public Message getWinnerSelect() {
        return winnerSelect;
    }

    @Override
    public Map<Player, Player> winnerSelected() {
        return winnerSelected;
    }

    @Override
    public List<User> getAccepted() {
        return accepted;
    }

    @Override
    public List<String> getVoteMessages() {
        return voteMessages;
    }

    @Override
    public Player getPlayer1() {
        return player1;
    }

    @Override
    public Player getPlayer2() {
        return player2;
    }

    @Override
    public Player getWinner() {
        return winner;
    }

    @Override
    public MatchType getMatchType() {
        return MatchType.TOURNAMENT;
    }

    @Override
    public MatchState getMatchState() {
        return matchState;
    }

    @Override
    public void setMatchChannel(TextChannel matchChannel) {
        this.matchChannel = matchChannel;
    }

    @Override
    public void setVoiceChannel(VoiceChannel voiceChannel) {
        this.voiceChannel = voiceChannel;
    }

    @Override
    public void setWinnerSelectMessage(Message winnerSelectMessage) {
        this.winnerSelect = winnerSelectMessage;
    }

    @Override
    public void setWinner(Player winner) {
        this.winner = winner;
        finish();
    }

    @Override
    public void setAccepted(User user) {
        accepted.add(user);
    }

    @Override
    public void addVoteMessage(String messageID) {
        voteMessages.add(messageID);
    }

    @Override
    public void setMatchState(MatchState matchState) {
        this.matchState = matchState;
    }

    @Override
    public void sendMessage() {
        getMatchChannel().sendMessage(MatchMessages.getEmbed(this)).queue(s -> {
            MatchImpl.getInstance().sendWinnerSelectMessage(this);
        });
    }

    @Override
    public void finish() {
        MatchImpl.getInstance().finishMatch(this);
        if(getVoiceChannel() != null) {
            getVoiceChannel().delete().queue();
        }
    }
}
