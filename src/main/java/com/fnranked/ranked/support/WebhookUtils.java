package com.fnranked.ranked.support;

import com.fnranked.ranked.util.JDAContainer;
import com.fnranked.ranked.jpa.entities.MatchTemp;
import net.dv8tion.jda.api.entities.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebhookUtils {

    @Autowired
    JDAContainer jdaContainer;

    public void setupWebhooks(MatchTemp matchTemp) {
        TextChannel matchChannel = jdaContainer.getJda().getTextChannelById(matchTemp.getMatchChannelId());
        TextChannel supportChannel = jdaContainer.getJda().getTextChannelById(matchTemp.getSupportChannelId());
        matchChannel.createWebhook("a").queue(matchWebhook -> {
            supportChannel.createWebhook("b").queue(supportWebhook -> {

            });
        });
    }
}
