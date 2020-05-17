package com.fnranked.ranked.listener;

import com.fnranked.ranked.jpa.entities.Player;
import com.fnranked.ranked.jpa.repo.TeamRepository;
import com.fnranked.ranked.util.TeamUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class DuoListener extends ListenerAdapter {

    @Autowired
    TeamUtils teamUtils;
    @Autowired
    TeamRepository teamRepository;

    @Value("${fnranked.channels.duo}")
    long duoSettingsChannelId;

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        Player player = teamUtils.getPlayer(event.getMember().getIdLong());
        boolean teamExists = teamRepository.findByPlayerListContainingAndSizeAndActiveIsTrue(player, 2).isPresent();
        if(teamExists) {
            event.getAuthor().openPrivateChannel().queue(pc -> {
                //TODO fancy message
                //TODO option to leave (current) duo by reacting with :door:
                pc.sendMessage("Error! You're already in a team").queue();
            });
            return;
        }
    }
}
