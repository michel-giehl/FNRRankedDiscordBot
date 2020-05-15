package com.fnranked.ranked.discord.commands;

import com.fnranked.ranked.data.MatchStatus;
import com.fnranked.ranked.discord.commands.commandhandler.listener.CommandListener;
import com.fnranked.ranked.jpa.entities.RankedMatch;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.MatchTempRepository;
import com.fnranked.ranked.jpa.repo.RankedMatchRepository;
import com.fnranked.ranked.jpa.util.MatchUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class CancelCommand implements CommandListener {

    @Autowired
    MatchTempRepository matchTempRepository;
    @Autowired
    MatchUtils matchUtils;
    @Autowired
    RankedMatchRepository matchRepository;

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        matchTempRepository.findByMatchChannelId(channel.getIdLong()).ifPresent(matchTemp -> {
            matchTempRepository.delete(matchTemp);
            RankedMatch rankedMatch = new RankedMatch(matchTemp, null, MatchStatus.CANCELED);
            matchRepository.save(rankedMatch);
            channel.sendMessage("Match canceled.").queue();
            channel.delete().queueAfter(10, TimeUnit.SECONDS);
        });
    }
}
