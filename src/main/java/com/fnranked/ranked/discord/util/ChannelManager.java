package com.fnranked.ranked.discord.util;

import com.fnranked.ranked.jpa.entities.MatchTemp;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.MatchTempRepository;
import com.fnranked.ranked.jpa.repo.TeamRepository;
import net.dv8tion.jda.api.Permission;
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

    /**
     * Creates a match channel for a match. Also tries to grant permissions to members if they joined guild
     * before channel got created.
     */
    @Transactional
    public void createMatchChannel(MatchTemp tempMatch, List<Team> teamsInMatch) {
        var guild = loadBalancer.getBestGuild();
        if(guild == null) {
            logger.trace("Unable to create match channel for " + tempMatch.toString() + " due to the JDA Guild with id #" + tempMatch.getGuildId() + " being null");
            return;
        }
        var channelAction = guild.createTextChannel("match-" + tempMatch.getId());
        var members = memberUtils.getAllMembersInTempMatch(tempMatch);
        for(Member member : members) {
            channelAction.addPermissionOverride(member, List.of(Permission.VIEW_CHANNEL), null);
        }
        channelAction.queue(tc -> {
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
}
