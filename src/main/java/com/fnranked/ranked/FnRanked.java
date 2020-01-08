package com.fnranked.ranked;

import com.fnranked.ranked.creator.ReactionListener;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.logging.Logger;

@Component
public class FnRanked {

    private static JDA jda;
    private static Logger logger = Logger.getLogger("Test");

    @Value("${bot.token}")
    private String token;

    @Value("${registration.channel}")
    private long tournamentChannelID;

    @Value("${registration.emote.fnranked}")
    private long fortniteRankedEmoteID;

    private RegistrationMessageListener createTournamentListener;

    private ReactionListener reactionListener;

    @Autowired
    public void reactionListener(ReactionListener reactionListener){
        this.reactionListener = reactionListener;
    }

    @Autowired
    public void createTournamentListener(RegistrationMessageListener createTournamentListener){
        this.createTournamentListener = createTournamentListener;
    }

    public void startBot() {
        logger.info("Initializing Registration Bot");
        initDiscordSession();

    }

    private void initDiscordSession() {
        JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT);
        jdaBuilder.setToken(token);
        jdaBuilder.setAutoReconnect(true);
        jdaBuilder.setStatus(OnlineStatus.ONLINE);
        jdaBuilder.setActivity(Activity.of(Activity.ActivityType.STREAMING, "Tournaments", "https://twitch.tv/fortnite_ranked"));
        logger.info("Starting discord session");
        try {
            jda = jdaBuilder.build();
            logger.info("Discord login successful.");
            jda.addEventListener(createTournamentListener);
            jda.addEventListener(reactionListener);
            jda.awaitReady();
            System.out.println("guilds: "+ jda.getGuilds().size());
        } catch (LoginException | InterruptedException e1) {
            e1.printStackTrace();
        }
        initTournamentChannel();
    }

    private void initTournamentChannel() {
        return;/*
        logger.info("initialising registration channel");
        TextChannel registrationChannel = jda.getTextChannelById(tournamentChannelID);
        System.out.println(jda.getEmotesByName("fnranked", true).get(0).getId());
        List<Message> messages = registrationChannel.getHistory().retrievePast(50).complete();
        while (!messages.isEmpty()) {
            if (messages.size() == 1) {
                registrationChannel.deleteMessageById(messages.get(0).getId()).complete();
            } else {
                registrationChannel.deleteMessages(messages).complete();
            }
            messages = registrationChannel.getHistory().retrievePast(50).complete();
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("**Fortnite Registration**");
        embedBuilder.setDescription("To begin creating a tournament react to this message using the button below");
        embedBuilder.setAuthor("KillerManDan#3850 with the help of Beatz and GiM");
        registrationChannel.sendMessage(embedBuilder.build()).complete();
        registrationChannel.addReactionById(registrationChannel.getLatestMessageIdLong(), "\uD83C\uDDE7").complete();
        logger.info("create-tournament channel initialized");
    */}
}
