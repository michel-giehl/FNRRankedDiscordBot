package com.fnranked.ranked.discord.util;

import com.fnranked.ranked.discord.messages.MessageUtils;
import com.fnranked.ranked.jpa.entities.MatchTemp;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.MatchTempRepository;
import com.fnranked.ranked.jpa.repo.TeamRepository;
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
public class ChannelManager {

    Logger logger = LoggerFactory.getLogger(ChannelManager.class);

    @Autowired
    TeamRepository teamRepository;
    @Autowired
    MatchTempRepository matchTempRepository;
    @Autowired
    JDAContainer jdaContainer;
    @Autowired
    LoadBalancer loadBalancer;
    @Autowired
    MemberUtils memberUtils;
    @Autowired
    MessageUtils messageUtils;

    /**
     * Creates a match channel for a match. Also tries to grant permissions to members if they joined guild
     * before channel got created.
     */
    @Transactional
    //to ignore "result ignored"
    @SuppressWarnings("all")
    public void createMatchChannel(MatchTemp tempMatch, List<Team> teamsInMatch) {
        var guildOpt = loadBalancer.getBestGuild();
        if(guildOpt.isEmpty()) {
            logger.trace("Unable to create match channel for " + tempMatch.toString() + " due to the JDA Guild with id #" + tempMatch.getGuildId() + " being null");
            return;
        }
        Guild guild = guildOpt.get();
        var channelAction = guild.createTextChannel("match-" + tempMatch.getId());
        var members = memberUtils.getAllMembersInTempMatch(tempMatch);
        for(Member member : members) {
            channelAction.addPermissionOverride(member, List.of(Permission.VIEW_CHANNEL), null);
        }
        //Create channel. Send Information message. Send Vote message. Save temp match
        channelAction.queue(tc -> {
            tc.sendMessage(messageUtils.getMatchInformationEmbed(tempMatch)).queue(infoMessage -> {
                tc.sendMessage(messageUtils.getVoteMessage(tempMatch)).queue(voteMessage -> {
                    tempMatch.setMatchChannelId(tc.getIdLong());
                    tempMatch.setVoteMessageId(voteMessage.getIdLong());
                    matchTempRepository.save(tempMatch);
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
            logger.trace("Match channel #" + matchTemp.getMatchChannelId() + " null.");
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
