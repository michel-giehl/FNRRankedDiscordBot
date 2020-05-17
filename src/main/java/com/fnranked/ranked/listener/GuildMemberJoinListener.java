package com.fnranked.ranked.listener;

import com.fnranked.ranked.util.ChannelCreator;
import com.fnranked.ranked.jpa.repo.MatchServerRepo;
import com.fnranked.ranked.util.MatchUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;

@Component
public class GuildMemberJoinListener extends ListenerAdapter {

    final Logger logger = LoggerFactory.getLogger(GuildMemberJoinListener.class);

    @Autowired
    MatchUtils matchUtils;
    @Autowired
    MatchServerRepo matchServerRepo;
    @Autowired
    ChannelCreator channelCreator;

    @Override
    @Transactional
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        long guildId = guild.getIdLong();
        boolean isMatchServer = matchServerRepo.findById(guildId).isPresent();
        if(isMatchServer) {
            var matches = matchUtils.findAllMatchesByUserIdInMatchServer(member.getIdLong(), guildId);
            if(!matches.isEmpty()) {
                logger.info("[+] #" + member.getId() + " joined guild #" + guildId + ". They are currently in " + matches.size() + " matches.");
                matches.forEach(m -> channelCreator.addChannelPermissions(member, m));
            } else {
                //kick user if they're not in a match on the specific match server.
                member.getUser().openPrivateChannel().queue(pc -> {
                    pc.sendMessage("You may only join this server if you're in a ranked match. Join https://discord.gg/fnr to play ranked 1v1/2v2").queue(msg -> {
                        guild.kick(event.getMember()).queue(success -> {
                            logger.info("Kicked #" + member.getId() + " from guild #" + guildId + " due to not being in a match.");
                        }, error2 -> {
                            logger.info("Error while trying to kick #" + member.getId() + " from guild #" + guildId + ". Message: " + error2.getMessage());
                        });
                    }, error -> { /* DMS OFF */ });
                });
            }
        }
    }
}
