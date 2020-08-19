package com.fnranked.ranked.util;

import com.fnranked.ranked.jpa.entities.MatchServer;
import com.fnranked.ranked.jpa.repo.MatchServerRepo;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoadBalancer {

    private Logger logger = LoggerFactory.getLogger(LoadBalancer.class);

    @Autowired
    MatchServerRepo matchServerRepo;
    @Autowired
    JDAContainer jdaContainer;

    /**
     * finds the guild with the fewest channels.
     */
    @Nullable
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
        var guildOpt = guilds.stream().min(Comparator.comparingInt(o -> o.getChannels().size()));
        if(guildOpt.isPresent()) {
            var guild = guildOpt.get();
            var matchServer = matchServers.stream().filter(m -> m.getId() == guild.getIdLong()).findFirst().get();
            return Pair.of(guild, matchServer);
        }
        logger.error("Failed to create match: No match server found.");
        return null;
    }
}
