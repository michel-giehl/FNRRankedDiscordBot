package com.fnranked.ranked.util;

import com.fnranked.ranked.api.entities.Result;
import com.fnranked.ranked.jpa.entities.MatchTemp;
import com.fnranked.ranked.jpa.repo.MatchTempRepository;
import com.fnranked.ranked.jpa.repo.TeamRepository;
import com.fnranked.ranked.messages.MessageUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ChannelCreator {

    Logger logger = LoggerFactory.getLogger(ChannelCreator.class);

    @Autowired
    TeamRepository teamRepository;
    @Autowired
    MatchTempRepository matchTempRepository;
    @Autowired
    JDAContainer jdaContainer;
    @Autowired
    LoadBalancer loadBalancer;
    @Autowired
    UserUtils userUtils;
    @Autowired
    MessageUtils messageUtils;

    /**
     * Creates a match channel for a match. Also tries to grant permissions to members if they joined guild
     * before channel got created.
     */
    @Transactional
    //to ignore "result ignored"
    @SuppressWarnings("all")
    public void createMatchChannel(MatchTemp tempMatch, Result<TextChannel> result) {
        var matchServerOpt = loadBalancer.getBestMatchServer();
        if (matchServerOpt == null || matchServerOpt.getFirst() == null) {
            return;
        }
        Guild guild = matchServerOpt.getFirst();
        tempMatch.setMatchServer(matchServerOpt.getSecond());
        String matchId = (tempMatch.getStartingTime().toInstant().getEpochSecond() + "");
        var channelAction = guild.createTextChannel("match-" + matchId.substring(matchId.length() - 4));
        var members = userUtils.getAllMembersInTempMatch(tempMatch);
        for (Member member : members) {
            channelAction.addPermissionOverride(member, List.of(Permission.VIEW_CHANNEL), null);
        }
        //Create channel. Send Information message. Send Vote message. Save temp match
        channelAction.queue(tc -> {
            result.invoke(tc);
            tc.sendMessage(messageUtils.getMatchInformationEmbed(tempMatch)).queue(infoMessage -> {
                tc.sendMessage(messageUtils.getVoteMessage(tempMatch)).queue(voteMessage -> {
                    tempMatch.setMatchChannelId(tc.getIdLong());
                    tempMatch.setVoteMessageId(voteMessage.getIdLong());
                    matchTempRepository.save(tempMatch);
                    voteMessage.addReaction("\uD83C\uDFC6").queue();
                    voteMessage.addReaction("☠️").queue();
                    voteMessage.addReaction("\uD83C\uDFF3️").queue();
                });
            });
        });
    }

    /**
     * Grants user permissions to view match channel.
     */
    public void addChannelPermissions(Member member, MatchTemp matchTemp) {
        TextChannel tc = member.getGuild().getTextChannelById(matchTemp.getMatchChannelId());
        if(tc == null) {
            logger.trace(String.format("Match channel #%d null.", matchTemp.getMatchChannelId()));
            return;
        }
        tc.getManager().putPermissionOverride(member, List.of(Permission.VIEW_CHANNEL), null).queue();
    }

    public void deleteChannel(MatchTemp matchTemp) {
        Guild guild = jdaContainer.getJda().getGuildById(matchTemp.getMatchServer().getId());
        if(guild == null) return;
        TextChannel matchChannel = guild.getTextChannelById(matchTemp.getMatchChannelId());
        if(matchChannel == null) return;
        matchChannel.delete().queue();
    }
}
