package com.fnranked.ranked.commands;

import com.fnranked.ranked.api.entities.MatchStatus;
import com.fnranked.ranked.api.entities.PermissionLevel;
import com.fnranked.ranked.commands.commandhandler.listener.CommandListener;
import com.fnranked.ranked.util.ChannelCreator;
import com.fnranked.ranked.util.PermissionUtil;
import com.fnranked.ranked.jpa.entities.RankedMatch;
import com.fnranked.ranked.jpa.repo.MatchTempRepository;
import com.fnranked.ranked.jpa.repo.RankedMatchRepository;
import com.fnranked.ranked.util.MatchUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CancelCommand implements CommandListener {

    @Autowired
    MatchTempRepository matchTempRepository;
    @Autowired
    MatchUtils matchUtils;
    @Autowired
    RankedMatchRepository matchRepository;
    @Autowired
    PermissionUtil permissionUtil;
    @Autowired
    ChannelCreator channelCreator;

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        if(!permissionUtil.hasPermission(sender.getUser(), PermissionLevel.STAFF)) return;
        matchTempRepository.findByMatchChannelId(channel.getIdLong()).ifPresent(matchTemp -> {
            matchTempRepository.delete(matchTemp);
            RankedMatch rankedMatch = new RankedMatch(matchTemp, null, MatchStatus.CANCELED);
            matchRepository.save(rankedMatch);
            channelCreator.deleteChannel(matchTemp);
        });
    }
}
