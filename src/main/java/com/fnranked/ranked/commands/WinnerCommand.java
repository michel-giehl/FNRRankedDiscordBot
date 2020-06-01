package com.fnranked.ranked.commands;

import com.fnranked.ranked.api.entities.PermissionLevel;
import com.fnranked.ranked.commands.commandhandler.listener.CommandListener;
import com.fnranked.ranked.util.PermissionUtil;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.MatchTempRepository;
import com.fnranked.ranked.util.MatchUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class WinnerCommand implements CommandListener {

    @Autowired
    MatchTempRepository matchTempRepository;
    @Autowired
    MatchUtils matchUtils;
    @Autowired
    PermissionUtil permissionUtil;

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        if(!permissionUtil.hasPermission(sender.getUser(), PermissionLevel.STAFF)) return;
        matchTempRepository.findByMatchChannelId(channel.getIdLong()).ifPresent(matchTemp -> {
            matchTempRepository.delete(matchTemp);
            Team winner = matchUtils.getTeamByUserId(message.getMentionedMembers().get(0).getIdLong(), matchTemp);
            matchUtils.endMatch(matchTemp, winner);
        });
    }
}
