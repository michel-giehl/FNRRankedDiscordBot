package com.fnranked.ranked.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
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

    /*
        emoji U+2139U+fe0f = information_source
    emoji U+1f6aa = door
    emoji U+1f507 = mute
    emoji U+1f508 = speaker
     */
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
                    ":information_source: Info about your current party\n" +
                    "\n" +
                    "Commands\n" +
                    "invite @User - invite new member to your party\n" +
//                "promote @User - make them party leader\n" +
                    "kick @User - kick someone from your party");

            MessageEmbed embed = eb.build();
            logger.info("purging messages");
            partyChannel.getIterableHistory().queue(messages -> {
                for(Message m : messages) {
                    if(!m.getEmbeds().isEmpty() && m.getEmbeds().get(0).equals(embed)) {
                        m.clearReactions().queue(then -> addReactions(m));
                        return;
                    } else {
                        m.delete().queue();
                    }
                }
                logger.info("Sending new message");
                partyChannel.sendMessage(eb.build()).queue(this::addReactions);
            });
        }
    }

    private void addReactions(Message message) {
        message.addReaction("U+2139U+fe0f").queue();
        message.addReaction("U+1f6aa").queue();
        message.addReaction("U+1f507").queue();
        message.addReaction("U+1f508").queue();
    }
}
