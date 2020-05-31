package com.fnranked.ranked.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DuoChannel {

    Logger logger = LoggerFactory.getLogger(DuoChannel.class);

    @Autowired
    JDAContainer jdaContainer;

    @Value("${fnranked.channels.duo}")
    long duoChannelId;

    public void initDuoChannel() {
        logger.info("Initializing duo channel");
        TextChannel duoChannel = jdaContainer.getJda().getTextChannelById(duoChannelId);
        if(duoChannel == null) return;
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("**How to create a duo**");
        eb.setDescription("Tag your teammate to send an invite.\n" +
                "They will receive a DM.");
    }
}
