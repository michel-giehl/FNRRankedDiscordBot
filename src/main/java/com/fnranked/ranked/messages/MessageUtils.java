package com.fnranked.ranked.messages;

import com.fnranked.ranked.api.entities.MatchVote;
import com.fnranked.ranked.api.entities.Region;
import com.fnranked.ranked.api.entities.TeamSize;
import com.fnranked.ranked.elo.EloUtils;
import com.fnranked.ranked.jpa.entities.*;
import com.fnranked.ranked.jpa.repo.*;
import com.fnranked.ranked.util.JDAContainer;
import com.fnranked.ranked.util.MatchUtils;
import com.fnranked.ranked.util.UserUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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
    @Autowired
    QueueRepository queueRepository;
    @Autowired
    MatchTypeRepository matchTypeRepository;
    @Autowired
    QueueMessageRepository queueMessageRepository;

    @Value("${emote.error}")
    private long errorEmoteId;
    @Value("${emote.loading}")
    private long loadingEmoteId;
    @Value("${emote.success}")
    private long successEmoteId;
    @Value("${colors.fnranked}")
    public int colorFnranked;
    @Value("${colors.error}")
    public int colorError;
    @Value("${colors.success}")
    public int colorSuccess;
    @Value("${colors.loading}")
    public int colorLoading;
    @Value("${fnranked.embed.footer}")
    public String fnrankedFooter;

    public Emote getErrorEmote() {
        return jdaContainer.getJda().getEmoteById(errorEmoteId);
    }

    public Emote getSuccessEmote() {
        return jdaContainer.getJda().getEmoteById(successEmoteId);
    }

    public Emote getLoadingEmote() {
        return jdaContainer.getJda().getEmoteById(loadingEmoteId);
    }

    public int getFnRankedColor() {
        return colorFnranked;
    }

    public void sendMessageEmbed(long userId, MessageEmbed messageEmbed) {
        User user = jdaContainer.getJda().getUserById(userId);
        if (user == null) return;
        user.openPrivateChannel().flatMap(pc -> pc.sendMessage(messageEmbed)).queue();
    }

    public void sendErrorMessage(long userId, String errorMessage) {
        User user = jdaContainer.getJda().getUserById(userId);
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(colorError);
        eb.setDescription(errorMessage);
        eb.setTitle(getErrorEmote().getAsMention() + " Error!");
        if (user == null) return;
        user.openPrivateChannel().flatMap(pc -> pc.sendMessage(eb.build())).queue();
    }

    public void sendSuccessMessage(long userId, String successMessage) {
        User user = jdaContainer.getJda().getUserById(userId);
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(colorSuccess);
        eb.setDescription(successMessage);
        eb.setTitle(getSuccessEmote().getAsMention() + " Success!");
        if(user == null) return;
        user.openPrivateChannel().flatMap(pc -> pc.sendMessage(eb.build())).queue();
    }

    public MessageEmbed getQueueEmbed(List<MatchType> matchTypeList, @Nullable Region region) {
        String regionString = region == null ? "" : region.toString() + " ";
        StringBuilder desc = new StringBuilder();
        EmbedBuilder queueEmbed = new EmbedBuilder();
        queueEmbed.setColor(0xF7347A);
        queueEmbed.setTitle(regionString + "Ranked Matchmaking");
        for (MatchType m : matchTypeList) {
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

    public void sendQueueMessage(User user, MessageChannel messageChannel) {
        List<Queue> queues = queueRepository.findAllByEnabledIsTrue();
        Set<String> emotes = queues.stream().map(q -> q.getMatchType().getDisplayEmote()).collect(Collectors.toSet());
        messageChannel.sendMessage(getQueueEmbed(((ArrayList<MatchType>)matchTypeRepository.findAll()), null)).queue(msg -> {
            QueueMessage queueMessage = new QueueMessage();
            queueMessage.setDMQueue(true);
            queueMessage.setUserId(user.getIdLong());
            queueMessage.setChannelId(msg.getChannel().getIdLong());
            queueMessage.setQueueMessageId(msg.getIdLong());
            queueMessageRepository.save(queueMessage);
            emotes.forEach(s -> msg.addReaction(s).queue());
        });
    }

    public void sendDMQueueMessage(Team team) {
        queuedTeamRepository.findByTeamWithQueueMessages(team).ifPresent(qt -> {
            List<User> users = userUtils.getUsersInTeam(team);
            if(users.isEmpty()) return;
            EmbedBuilder qMsg = new EmbedBuilder();
            qMsg.setColor(colorLoading);
            qMsg.setTitle(getLoadingEmote().getAsMention() + " Looking for suitable opponent...");
            for(User user : users) {
                user.openPrivateChannel().queue(pc -> pc.sendMessage(qMsg.build()).queue(msg -> {
                    msg.addReaction("❌").queue();
                    qt.getMatchMessages().add(new MatchMessages(msg));
                    queuedTeamRepository.save(qt);
                }));
            }
        });
    }

    public void deleteDMQueueMessages(Team t) {
        queuedTeamRepository.findByTeamWithQueueMessages(t).ifPresent(team -> {
            List<User> users = userUtils.getUsersInTeam(team.getTeam());
            if (users.isEmpty()) return;
            for (User user : users) {
                user.openPrivateChannel().queue(pc -> team.getMatchMessages().stream().filter(m -> m.getChannelId() == pc.getIdLong()).findFirst().ifPresent(qMsg -> pc.retrieveMessageById(qMsg.getMessageId()).queue(msg -> msg.delete().queue())));
            }
        });
    }

    public MessageEmbed getMatchNotAcceptedEmbed()  {
        EmbedBuilder notAccepted = new EmbedBuilder();
        notAccepted.setColor(colorError);
        notAccepted.setDescription("The match was canceled because someone didn't accept.");
        return notAccepted.build();
    }

    public void sendMatchReadyMessage(MatchTemp matchTemp, TextChannel tc) {
        tc.createInvite().setMaxAge(60*60).queue(invite -> {
            EmbedBuilder readyEmbed = new EmbedBuilder();
            readyEmbed.setColor(colorSuccess);
            readyEmbed.setTitle(getSuccessEmote().getAsMention() + " Match ready");
            readyEmbed.setDescription("Your match is ready. Click [here](" + invite.getUrl() + ") to get to your match channel");
            userUtils.getUsersInMatch(matchTemp).forEach(u -> u.openPrivateChannel()
                    .flatMap(pc -> pc.sendMessage(readyEmbed.build())).queue());
        });

    }

    @Transactional
    public void sendMatchAccept(long matchTempId) {
        var matchTempOpt = matchTempRepository.findByIdWithMessageList(matchTempId);
        if(matchTempOpt.isEmpty()) {
            logger.warn("MatchTemp object not found.");
            return;
        }
        MatchTemp matchTemp = matchTempOpt.get();
        EmbedBuilder acceptEmbed = new EmbedBuilder();
        acceptEmbed.setTitle("Match found");
        acceptEmbed.setColor(colorFnranked);
        long captainA = matchTemp.getTeamA().getCaptain().getId();
        long captainB = matchTemp.getTeamB().getCaptain().getId();
        JDA jda = jdaContainer.getJda();
        var users = List.of(jda.getUserById(captainA), jda.getUserById(captainB));
        AtomicBoolean a = new AtomicBoolean(false);
        for(User u : users) {
            u.openPrivateChannel().queue(pc -> {
                // Get elo for both teams
                Team userTeam = matchUtils.getTeamByUserId(u.getIdLong(), matchTemp);
                double elo = eloUtils.getTeamElo(userTeam.getId(), matchTemp.getMatchType()).getEloRating();
                Team oppTeam = matchTemp.getTeamA().equals(userTeam) ? matchTemp.getTeamB() : matchTemp.getTeamA();
                double oppElo = eloUtils.getTeamElo(oppTeam.getId(), matchTemp.getMatchType()).getEloRating();
                acceptEmbed.setDescription(String.format("A match has been found for you.%n```py%nYour elo: %.0f%nOpponents elo: %.0f``` Click :white_check_mark: to accept the match.", elo, oppElo));
                pc.sendMessage(acceptEmbed.build()).queue(msg -> {
                    msg.addReaction("✅").queue();
                    matchTemp.getMatchAcceptMessages().add(new MatchMessages(msg));
                    if (a.get()) {
                        matchTempRepository.save(matchTemp);
                    }
                    a.set(true);
                }, e -> {
                });
            });
        }
    }

    public MessageEmbed getMatchHistoryEmbed(RankedMatch rankedMatch, boolean isWinner) {
        String emoji = rankedMatch.getMatchType().getDisplayEmote();

        Team loserTeam = rankedMatch.getTeamA().equals(rankedMatch.getWinner()) ? rankedMatch.getTeamB() : rankedMatch.getTeamA();
        Team winnerTeam = rankedMatch.getWinner();

        boolean isTeamA = isWinner == rankedMatch.getTeamA().equals(winnerTeam);
        double eloChange = isTeamA ? rankedMatch.getTeamAEloChange() : rankedMatch.getTeamBEloChange();

        String desc = String.format(":trophy: `%s`%n:skull_crossbones: `%s`", userUtils.getUsernamesInTeam(winnerTeam), userUtils.getUsernamesInTeam(loserTeam));

        desc += String.format("%n%n**Elo change:** ```diff%n%+.2f```", eloChange);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(isWinner ? colorSuccess : colorError);
        eb.setTitle(String.format("%s **Match Summary** %s", emoji, emoji));
        eb.setDescription(desc);
        return eb.build();
    }

    public MessageEmbed getMatchCanceledEmbed() {
        String desc = "Your match was canceled";
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(colorError);
        eb.setTitle(String.format("%s **Match canceled** %s", getErrorEmote().getAsMention(), getErrorEmote().getAsMention()));
        eb.setDescription(desc);
        return eb.build();
    }

    public MessageEmbed getMatchInformationEmbed(MatchTemp matchTemp) {
        EmbedBuilder embed = new EmbedBuilder();
        //ex EU Boxfight 1v1 Match
        String title = String.format("**%s %s Match**", matchTemp.getRegion().toString(), matchTemp.getMatchType().getName());
        embed.setTitle(title);
        embed.setColor(0xF7347A);
        embed.setDescription(String.format("**Have fun and good luck! Remember: First to win %d rounds takes the W**", matchTemp.getMatchType().getRequiredRoundsToWin()));
        embed.addField("**Map Code**", String.format("```glsl%n%s```", "1234-1234-1234"), false);
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
        String team1Emote = getErrorEmote().getAsMention();
        String team2Emote = getErrorEmote().getAsMention();
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

    private String getVoteEmote(MatchVote teamVote) {
        return teamVote == PENDING ? getLoadingEmote().getAsMention() : getSuccessEmote().getAsMention();
    }

    private boolean isVoteError(MatchVote teamAVote, MatchVote teamBVote) {
        return (teamAVote != teamBVote) && !(teamAVote == PENDING || teamBVote == PENDING);
    }
}
