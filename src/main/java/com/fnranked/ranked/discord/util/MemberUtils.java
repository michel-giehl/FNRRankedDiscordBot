package com.fnranked.ranked.discord.util;

import com.fnranked.ranked.jpa.entities.MatchTemp;
import com.fnranked.ranked.jpa.entities.Player;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MemberUtils {

    @Autowired
    JDAContainer jdaContainer;

    /**
     * Finds all members for a given match
     * @param matchTemp match object
     * @return list of members in that match who are also in the guild
     */
    public List<Member> getAllMembersInTempMatch(MatchTemp matchTemp) {
        List<Member> memberList = new ArrayList<>();
        Guild guild = jdaContainer.getJda().getGuildById(matchTemp.getMatchServer().getId());
        if(guild == null) return memberList;
        var teamAPlayers = matchTemp.getTeamA().getPlayerList();
        var teamBPlayers = matchTemp.getTeamB().getPlayerList();
        var userIds = Stream.concat(teamAPlayers.stream(), teamBPlayers.stream()).map(Player::getId).collect(Collectors.toList());
        for(Long uId : userIds) {
            Member m = guild.getMemberById(uId);
            if(m != null)
                memberList.add(m);
        }
        return memberList;
    }

    public void kickMembersAfterMatch(MatchTemp matchtemp) {
        List<Member> memberToKick = getAllMembersInTempMatch(matchtemp);
        if(!memberToKick.isEmpty()) {
            for(Member m : memberToKick) {
                m.getGuild().kick(m).queue();
            }
        }
    }
}
