package com.fnranked.ranked.discord.util;

import com.fnranked.ranked.jpa.entities.MatchServer;
import com.fnranked.ranked.jpa.repo.MatchServerRepo;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
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
    public Pair<Guild, MatchServer> getBestMatchServer() {
        List<MatchServer> matchServers = ((List<MatchServer>)matchServerRepo.findAll());
        List<Long> guildIds = matchServers.stream().map(MatchServer::getId).collect(Collectors.toList());
        List<Guild> guilds = new ArrayList<>();
        JDA jda = jdaContainer.getJda();
        for(long guildId : guildIds) {
            Guild guild = jda.getGuildById(guildId);
            if(guild != null)
                guilds.add(guild);
        }
        var guild = guilds.stream().min(Comparator.comparingInt(o -> o.getChannels().size())).get();
        var matchServer = matchServers.stream().filter(m -> m.getId() == guild.getIdLong()).findFirst().get();
        return Pair.of(guild, matchServer);
    }
}
