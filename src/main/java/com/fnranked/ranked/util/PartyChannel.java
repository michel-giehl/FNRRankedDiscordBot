package com.fnranked.ranked.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PartyChannel {

    Logger logger = LoggerFactory.getLogger(PartyChannel.class);

    @Autowired
    JDAContainer jdaContainer;

    @Value("${fnranked.channels.party}")
    long partyChannelId;

    public void initPartyChannel() {
        logger.info("Initializing party channel");
        TextChannel partyChannel = jdaContainer.getJda().getTextChannelById(partyChannelId);
        if (partyChannel == null) {
            logger.error("Failure to initialize party channel due to it not being found.");
        } else {


            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("**How to create a Party**");
            eb.setDescription(":door: Leave party\n" +
                    ":mute: Disable party invites\n" +
                    ":speaker: Enable party invites\n" +
                    ":info: Info about your current party\n" +
                    "\n" +
                    "Commands\n" +
                    "invite @User - invite new member to your party\n" +
//                "promote @User - make them party leader\n" +
                    "kick @User - kick someone from your party");
            partyChannel.sendMessage(eb.build()).submit();
        }
    }
}
