package com.fnranked.ranked;

import com.fnranked.ranked.discord.util.JDAContainer;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.hooks.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;
import java.util.List;

@Component
public class FnRanked {

    private static Logger logger = LoggerFactory.getLogger(FnRanked.class);

    @Value("${fnranked.token}")
    String token;

    @Autowired
    JDAContainer jdaContainer;

    @Autowired
    List<EventListener> eventListeners;

    public void startBot() {
        logger.info("Initializing FN Ranked Bot");
        JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT);
        jdaBuilder.setToken(token);
        jdaBuilder.setAutoReconnect(true);
        jdaBuilder.setStatus(OnlineStatus.ONLINE);
        JDA jda;
        try {
            jda = jdaBuilder.build();
            logger.info("Discord login successful.");
            for (EventListener eventListener : eventListeners) {
                jda.addEventListener(eventListener);
            }
            jda.awaitReady();
            jdaContainer.setJda(jda);
            logger.info("FN Ranked Bot Initialised");
        } catch (LoginException | InterruptedException e1) {
            logger.error(e1.toString());
        }

    }
}
