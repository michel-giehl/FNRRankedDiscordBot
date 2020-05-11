package com.fnranked.ranked.discord.commands;

import com.fnranked.ranked.discord.commands.commandhandler.listener.CommandListener;
import com.fnranked.ranked.jpa.entities.MatchType;
import com.fnranked.ranked.jpa.repo.MatchTypeRepository;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AddMatchTypeCommand implements CommandListener {

    @Autowired
    MatchTypeRepository matchTypeRepository;

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        message.delete().queue();
        if(args.length < 5) {
            channel.sendMessage("Use: `!addMatchType <int team size> <int required rounds to win> <int season> <str emote> <str name>`").queue();
            return;
        }
        try {
            int teamSize = Integer.parseInt(args[0]);
            int requiredRoundsToWin = Integer.parseInt(args[1]);
            int season = Integer.parseInt(args[2]);
            String name = args[3];
            String emote = String.join(" ", Arrays.copyOfRange(args, 4, args.length));
            MatchType matchType = new MatchType();
            matchType.setDisplayEmote(emote);
            matchType.setName(name);
            matchType.setTeamSize(teamSize);
            matchType.setSeason(season);
            matchType.setRequiredRoundsToWin(requiredRoundsToWin);
            matchTypeRepository.save(matchType);
            channel.sendMessage(String.format("Match type `%s` added.", name)).queue();
        }catch(NumberFormatException e) {
            channel.sendMessage("Invalid syntax once again. ").queue();
        }
    }
}
