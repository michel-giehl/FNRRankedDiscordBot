package com.fnranked.ranked.messages;

import com.fnranked.ranked.api.entities.MatchVote;
import com.fnranked.ranked.api.entities.Region;
import com.fnranked.ranked.api.entities.TeamSize;
import com.fnranked.ranked.jpa.repo.QueuedTeamRepository;
import com.fnranked.ranked.util.JDAContainer;
import com.fnranked.ranked.util.UserUtils;
import com.fnranked.ranked.elo.EloUtils;
import com.fnranked.ranked.jpa.entities.MatchMessages;
import com.fnranked.ranked.jpa.entities.MatchTemp;
import com.fnranked.ranked.jpa.entities.MatchType;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.MatchMessagRepo;
import com.fnranked.ranked.jpa.repo.MatchTempRepository;
import com.fnranked.ranked.util.MatchUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.fnranked.ranked.api.entities.MatchVote.PENDING;

@Component
public class MessageUtils {

    Logger logger = LoggerFactory.getLogger(MessageUtils.class);

    @Autowired
    JDAContainer jdaContainer;
    @Autowired
    MatchTempRepository matchTempRepository;
    @Autowired
    MatchMessagRepo matchMessagRepo;
    @Autowired
    EloUtils eloUtils;
    @Autowired
    MatchUtils matchUtils;
    @Autowired
    UserUtils userUtils;
    @Autowired
    QueuedTeamRepository queuedTeamRepository;

    @Value("${emote.error}")
    long errorEmoteId;
    @Value("${emote.loading}")
    long loadingEmoteId;
    @Value("${emote.success}")
    long successEmoteId;
    @Value("${colors.fnranked}")
    int fnrankedColor;
    @Value("${colors.error}")
    int errorColor;
    @Value("${colors.success}")
    int successColor;
    @Value("${colors.loading}")
    int loadingColor;

    public MessageEmbed getQueueEmbed(ArrayList<MatchType> matchTypeList, @Nullable Region region) {
        String regionString = region == null ? "" : region.toString() + " ";
        StringBuilder desc = new StringBuilder();
        EmbedBuilder queueEmbed = new EmbedBuilder();
        queueEmbed.setColor(0xF7347A);
        queueEmbed.setTitle(regionString + "Ranked Matchmaking");
        for(MatchType m : matchTypeList) {
            //ex. :EMOTE: Ranked 1v1 Boxfight queue
            desc.append("\n")
                    .append(m.getDisplayEmote())
                    .append(" **Ranked ")
                    .append(TeamSize.fromInt(m.getTeamSize()).toString())
                    .append(" ")
                    .append(m.getName())
                    .append("** queue");
        }
        queueEmbed.setDescription(desc.toString());
        return queueEmbed.build();
    }

    public void sendDMQueueMessage(long userId) {
        User user = jdaContainer.getJda().getUserById(userId);
        if(user == null) return;
        EmbedBuilder qMsg = new EmbedBuilder();
        qMsg.setColor(loadingColor);
        qMsg.setTitle(encodeEmote(loadingEmoteId) + " Looking for suitable opponent...");
        user.openPrivateChannel()
                .flatMap(pc -> pc.sendMessage(qMsg.build()))
                .flatMap(msg -> msg.addReaction("❌")).queue();
    }

    public MessageEmbed getMatchNotAcceptedEmbed()  {
        EmbedBuilder notAccepted = new EmbedBuilder();
        notAccepted.setColor(errorColor);
        notAccepted.setDescription("The match was canceled because someone didn't accept.");
        return notAccepted.build();
    }

    public void sendMatchReadyMessage(MatchTemp matchTemp, TextChannel tc) {
        tc.createInvite().setMaxAge(60*60).queue(invite -> {
            EmbedBuilder readyEmbed = new EmbedBuilder();
            readyEmbed.setColor(successColor);
            readyEmbed.setTitle(encodeEmote(successEmoteId) + " Match ready");
            readyEmbed.setDescription("Your match is ready. Click [here](" + invite.getUrl() + ") to get to your match channel");
            userUtils.getUsersInMatch(matchTemp).forEach(u -> {
                u.openPrivateChannel()
                        .flatMap(pc -> pc.sendMessage(readyEmbed.build())).queue();
            });
        });

    }

    @Transactional
    public void sendMatchAccept(long matchTempId) {
        System.out.println("=00");
        var matchTempOpt = matchTempRepository.findByIdWithMessageList(matchTempId);
        if(matchTempOpt.isEmpty()) {
            logger.warn("MatchTemp object not found.");
            return;
        }
        MatchTemp matchTemp = matchTempOpt.get();
        EmbedBuilder acceptEmbed = new EmbedBuilder();
        acceptEmbed.setTitle("Match found");
        acceptEmbed.setColor(fnrankedColor);
        long captainA = matchTemp.getTeamA().getCaptain().getId();
        long captainB = matchTemp.getTeamB().getCaptain().getId();
        JDA jda = jdaContainer.getJda();
        var users = List.of(jda.getUserById(captainA), jda.getUserById(captainB));
        AtomicBoolean a = new AtomicBoolean(false);
        for(User u : users) {
            //TODO user elo
            u.openPrivateChannel().queue(pc -> {
                Team userTeam = matchUtils.getTeamByUserId(u.getIdLong(), matchTemp);
                double elo = eloUtils.getTeamElo(userTeam.getId(), matchTemp.getMatchType()).getEloRating();
                Team oppTeam = matchTemp.getTeamA().equals(userTeam) ? matchTemp.getTeamB() : matchTemp.getTeamA();
                double oppElo = eloUtils.getTeamElo(oppTeam.getId(), matchTemp.getMatchType()).getEloRating();
                acceptEmbed.setDescription(String.format("A match has been found for you.\n```py\nYour elo: %.0f\nOpponents elo: %.0f``` Click :white_check_mark: to accept the match.", elo, oppElo));
                pc.sendMessage(acceptEmbed.build()).queue(msg -> {
                    msg.addReaction("✅").queue();
                    matchTemp.getMatchAcceptMessages().add(new MatchMessages(msg));
                    if(a.get()) {
                        matchTempRepository.save(matchTemp);
                    }
                    a.set(true);
                }, e-> {});
            });
        }
    }

    public MessageEmbed getMatchInformationEmbed(MatchTemp matchTemp) {
        EmbedBuilder embed = new EmbedBuilder();
        //ex EU Boxfight 1v1 Match
        String title = String.format("**%s %s Match**", matchTemp.getRegion().toString(), matchTemp.getMatchType().getName());
        embed.setTitle(title);
        embed.setColor(0xF7347A);
        embed.setDescription(String.format("**Have fun and good luck! Remember: First to win %d rounds takes the W**", matchTemp.getMatchType().getRequiredRoundsToWin()));
        embed.addField("**Map Code**", String.format("```glsl\n%s```", "1234-1234-1234"), false);
        return embed.build();
    }

    public void updateVoteMessage(MatchTemp matchTemp) {
        TextChannel tc = jdaContainer.getJda().getTextChannelById(matchTemp.getMatchChannelId());
        if(tc == null) return;
        tc.retrieveMessageById(matchTemp.getVoteMessageId())
                .flatMap(msg ->msg.editMessage(getVoteMessage(matchTemp))).queue(null, null);
    }

    public MessageEmbed getVoteMessage(MatchTemp matchTemp) {
        EmbedBuilder winnerSelect = new EmbedBuilder();
        winnerSelect.setColor(0xF7347A);
        winnerSelect.setTitle("**Please select the winner**");
        String team1Emote = encodeEmote(errorEmoteId);
        String team2Emote = encodeEmote(errorEmoteId);
        if(!isVoteError(matchTemp.getTeamAVote(), matchTemp.getTeamBVote())) {
            team1Emote = getVoteEmote(matchTemp.getTeamAVote());
            team2Emote = getVoteEmote(matchTemp.getTeamBVote());
        }
        winnerSelect.setDescription(
                ":trophy: if you **won**"
                        + "\n:skull_crossbones: If you **lost**"
                        + "\n:flag_white: to cancel the match"
                        + "\n\n:telephone: if you want to call staff. Abusing this feature will get you banned!"
                        + "\n:warning: Selecting the wrong winner on purpose will get you banned!"
                        + "\n\n**Vote Status**"
                        + "\n" + team1Emote + " **Team 1** " + getVoteStatus(matchTemp.getTeamAVote())
                        + "\n" + team2Emote + " **Team 2** " + getVoteStatus(matchTemp.getTeamBVote())
        );
        return winnerSelect.build();
    }

    private String getVoteStatus(MatchVote teamVote) {
        switch (teamVote) {
            case CANCEL:
                return "wants to **cancel** the match";
            case TEAM_A:
                return "selected **Team 1**";
            case TEAM_B:
                return "selected **Team 2**";
            default:
                return "*Vote pending*";
        }
    }

    private String encodeEmote(long id) {
        var emote = jdaContainer.getJda().getEmoteById(id);
        if(emote == null) {
            return "";
        }
        return emote.getAsMention();
    }

    private String getVoteEmote(MatchVote teamVote) {
        return teamVote == PENDING ? encodeEmote(loadingEmoteId) : encodeEmote(successEmoteId);
    }

    private boolean isVoteError(MatchVote teamAVote, MatchVote teamBVote) {
        return (teamAVote != teamBVote) && !(teamAVote == PENDING || teamBVote == PENDING);
    }
}
