package com.fnranked.ranked.discord.support;

import com.fnranked.ranked.discord.util.JDAContainer;
import com.fnranked.ranked.jpa.entities.MatchTemp;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SupportChannels {

    private static Logger logger = LoggerFactory.getLogger(SupportChannels.class);

    @Autowired
    JDAContainer jdaContainer;

    @Value("${guild}")
    private long guildId;

    public void createSupportChannel(MatchTemp matchTemp) {
        Guild guild = jdaContainer.getJda().getGuildById(guildId);
        if(guild == null) {
            logger.error("Guild object was null while trying to create support channel for match #" + matchTemp.getId());
            return;
        }
        Category supportCat = getSupportCat(guild);
        if(supportCat == null) {
            logger.error("Support Category was null while trying to create support channel for match #" + matchTemp.getId());
            return;
        }
        supportCat.createTextChannel("support-" + matchTemp.getId()).queue(supportChannel -> {
            //TODO update matchTemp
        });
    }

    private Category getSupportCat(Guild guild) {
        return null;
    }
}
