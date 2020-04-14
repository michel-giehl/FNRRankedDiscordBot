package com.fnranked.ranked.discord;

import com.fnranked.ranked.discord.util.JDAContainer;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;

@Component
public class DiscordBoot {

    private static Logger logger = LoggerFactory.getLogger(DiscordBoot.class);
    private JDA jda;


    @Autowired
    JDAContainer jdaContainer;

    @Value("${bot.token}")
    String token;

    private void initDiscordSession() {
        JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT);
        jdaBuilder.setToken(token);
        jdaBuilder.setAutoReconnect(true);
        jdaBuilder.setStatus(OnlineStatus.ONLINE);
        jdaBuilder.setActivity(Activity.watching("Netflix"));
        logger.info("Starting discord session");
        try {
            jda = jdaBuilder.build();
            logger.info("Discord login successful.");
            jdaContainer.setJda(jda);
        } catch (LoginException e1) {
            e1.printStackTrace();
        }
    }
}
