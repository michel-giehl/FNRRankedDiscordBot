package com.fnranked.tournament.matchmaking;

import com.fnranked.tournament.matchmaking.structures.Match;
import com.fnranked.tournament.matchmaking.structures.MatchState;
import com.fnranked.tournament.matchmaking.structures.MatchType;
import com.fnranked.tournament.matchmaking.structures.Player;
import com.fnranked.tournament.util.EpicAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MatchImpl {

    private static ConcurrentHashMap<String, Match> currentMatches;
    private static Map<MatchType, Match> matches;
    private static MatchImpl matchImpl;
    private static int maxMatches = 350;
    private static Logger logger;


    private Timer timer;

    public MatchImpl() {
        logger = LoggerFactory.getLogger(MatchImpl.class);
        if(matchImpl == null) {
            logger.info("Initializing Match Controller...");
            matchImpl = this;
            currentMatches = new ConcurrentHashMap<>();
            matches = new HashMap<>();
            loadMatches();
        }
    }

    public static MatchImpl getInstance() {
        return matchImpl;
    }

    private void loadMatches() {
        matches.put(MatchType.TOURNAMENT, new TournamentMatch());
        logger.info("Loaded " + matches.size() + " match types!");
    }

    public static List<Match> getMatches() {
        return new ArrayList<>(currentMatches.values());
    }
    public static Match getMatch(String matchID) {
        return currentMatches.get(matchID);
    }

    public static Match getMatchByChannelID(String channelID) {
        for(Match match : currentMatches.values()) {
            if(match.getMatchChannel() != null) {
                if(match.getMatchChannel().getId().equals(channelID)) {
                    return match;
                }
            }
        }
        return null;
    }

    public static Match getMatchByUserID(String userID) {
        for(Match match : currentMatches.values()) {
            if(match.getPlayer1().getMember().getId().equals(userID) ||
                    match.getPlayer2().getMember().getId().equals(userID)) {
                return match;
            }
        }
        return null;
    }

    public void startMatch(MatchType matchType, String region, Member member1, Member member2) {
        if(currentMatches.size() < maxMatches) {
            timer = new Timer();
            Player player1 = getPlayer(matchType, member1);
            Player player2 = getPlayer(matchType, member2);
            var match = new TournamentMatch();
            match.setMatchState(MatchState.AWAITING_WINNER_SELECT);
            currentMatches.put(match.getID(), match);
            startTimer(match);
            logger.info("Started tournament match"
            );
        }
    }

    public void finishMatch(Match match) {
    }

    private void startTimer(Match match) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(match.getMatchState() == MatchState.AWAITING_MATCH_ACCEPT) {
                    match.getPlayer1().getMember().getUser().openPrivateChannel().queue(privateChannel -> {
                        privateChannel.sendMessage(":x: One or more players didn't accept the match.").queue(s -> {}, e -> {});
                    });
                    match.getPlayer2().getMember().getUser().openPrivateChannel().queue(privateChannel -> {
                        privateChannel.sendMessage(":x: One or more players didn't accept the match.").queue(s -> {}, e -> {});
                    });
                    match.finish();
                }
            }
        }, 1000*60);
    }

    public void sendWinnerSelectMessage(Match match) {
        EmbedBuilder winnerSelect = new EmbedBuilder();
        winnerSelect.setColor(match.getColor());
        winnerSelect.setTitle("**Please select the winner**");
        winnerSelect.setDescription(
                ":trophy: if you **won**"
                + "\n:skull_crossbones: If you **lost**"
                + "\n:telephone: if you want to call staff. Abusing this feature will get you banned!"
                + "\n\n:warning: Selecting the wrong winner on purpose will get you banned!"
        );
        match.getMatchChannel().sendMessage(winnerSelect.build()).queue(s -> {
            match.setWinnerSelectMessage(s);
            s.addReaction("\uD83C\uDFC6").queue();
            s.addReaction("☠️").queue();
            s.addReaction("☎️").queue();
        });
    }


//    public void createChannel(Match match, Callback<TextChannel> channel) {
//        if(getCat() == null) {
//            logger.error("Match categories null. WTF went wrong??");
//            return;
//        }
//        getCat().createTextChannel(match.getMatchType().toString() + "-" + match.getID().substring(match.getID().length() - 4)).queue(textChannel -> {
//            textChannel.createPermissionOverride(match.getPlayer1().getMember()).setAllow(Permission.MESSAGE_READ).queue();
//            textChannel.createPermissionOverride(match.getPlayer2().getMember()).setAllow(Permission.MESSAGE_READ).queue();
//            match.setMatchChannel(textChannel);
//            match.sendMessage();
//            channel.invoke(textChannel);
//        });
//    }

    private Category getCat() {
        Guild guild = null;
        var cat1 = guild.getCategoryById("601062215122157579");
        var cat2 = guild.getCategoryById("608385977303433238");
        var cat3= guild.getCategoryById("608385996861734912");
        var cat4 = guild.getCategoryById("608385858252570654");
        var cat5 = guild.getCategoryById("608385939701628934");
        var cat6 = guild.getCategoryById("611506803292897310");
        var cat7 = guild.getCategoryById("611506840739512331");
        if(cat1 == null || cat2 == null || cat3 == null || cat4 == null || cat5 == null || cat6 == null
                || cat7 == null) {
            return null;
        }
        if(cat1.getChannels().size() < 50) {
            return cat1;
        } else if(cat2.getChannels().size() < 50) {
            return cat2;
        } else if(cat3.getChannels().size() < 50) {
            return cat3;
        } else if(cat4.getChannels().size() < 50) {
            return cat4;
        }else if(cat5.getChannels().size() < 50) {
            return cat5;
        }else if(cat6.getChannels().size() < 50) {
            return cat6;
        }else {
            return cat7;
        }
    }

    private Player getPlayer(MatchType matchType, Member member) {
        var elo = 0.0D;
        String epicDisplayName = EpicAPI.getEpicName(member.getId());
        return new Player() {

            private Message voteMessage;

            @Override
            public Member getMember() {
                return member;
            }

            @Override
            public String getEpicDisplayName() {
                return epicDisplayName;
            }

            @Override
            public void setVoteMessage(Message voteMessage) {
                this.voteMessage = voteMessage;
            }

            @Override
            public String toString() {
                return "#" + getMember().getId() + " / ";
            }
        };
    }
}