package com.fnranked.ranked.discord.util;

import com.fnranked.ranked.jpa.entities.MatchServer;
import com.fnranked.ranked.jpa.repo.MatchServerRepo;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LoadBalancer {

    @Autowired
    MatchServerRepo matchServerRepo;
    @Autowired
    JDAContainer jdaContainer;

    /**
     * finds the guild with the fewest channels.
     */
    public Optional<Guild> getBestGuild() {
        List<Long> guildIds = ((List<MatchServer>)matchServerRepo.findAll()).stream().map(MatchServer::getId).collect(Collectors.toList());
        List<Guild> guilds = new ArrayList<>();
        JDA jda = jdaContainer.getJda();
        for(long guildId : guildIds) {
            Guild guild = jda.getGuildById(guildId);
            if(guild != null)
                guilds.add(guild);
        }
        return guilds.stream().min(Comparator.comparingInt(o -> o.getChannels().size()));
    }
}
