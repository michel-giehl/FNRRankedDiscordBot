package com.fnranked.ranked;

import com.fnranked.ranked.discord.commands.AddMatchTypeCommand;
import com.fnranked.ranked.discord.commands.AddQueueCommand;
import com.fnranked.ranked.discord.commands.SendQueueMessageCommand;
import com.fnranked.ranked.discord.commands.WinnerCommand;
import com.fnranked.ranked.discord.commands.commandhandler.CommandHandlerBuilder;
import com.fnranked.ranked.discord.commands.commandhandler.CommandHandlerListener;
import com.fnranked.ranked.discord.commands.commandhandler.command.CommandBuilder;
import com.fnranked.ranked.discord.listener.GuildMemberJoinListener;
import com.fnranked.ranked.discord.listener.MatchAcceptListener;
import com.fnranked.ranked.discord.listener.MatchVoteListener;
import com.fnranked.ranked.discord.listener.QueueListener;
import com.fnranked.ranked.discord.util.JDAContainer;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
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

    @Autowired
    JDAContainer jdaContainer;

    @Autowired
    AddMatchTypeCommand addMatchTypeCommand;
    @Autowired
    AddQueueCommand addQueueCommand;
    @Autowired
    SendQueueMessageCommand sendQueueMessageCommand;
    @Autowired
    CommandHandlerListener commandHandlerListener;
    @Autowired
    WinnerCommand winnerCommand;

    @Autowired
    QueueListener queueListener;
    @Autowired
    GuildMemberJoinListener guildMemberJoinListener;
    @Autowired
    MatchAcceptListener matchAcceptListener;
    @Autowired
    MatchVoteListener matchVoteListener;


    @Value("${bot.token}")
    String token;

    public void initDiscordSession() {
        JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT);
        jdaBuilder.setToken(token);
        jdaBuilder.setAutoReconnect(true);
        jdaBuilder.setStatus(OnlineStatus.ONLINE);
        jdaBuilder.setActivity(Activity.watching("Netflix"));
        logger.info("Starting discord session");
        try {
            JDA jda = jdaBuilder.build();
            logger.info("Discord login successful.");
            jdaContainer.setJda(jda);
            jda.addEventListener(commandHandlerListener);
            jda.addEventListener(queueListener);
            jda.addEventListener(guildMemberJoinListener);
            jda.addEventListener(matchAcceptListener);
            jda.addEventListener(matchVoteListener);

            commandHandlerListener.init(
            new CommandHandlerBuilder(jda).setPrefix("!")
                    .addCommand(new CommandBuilder("addmatchtype", addMatchTypeCommand).build())
                    .addCommand(new CommandBuilder("addqueue", addQueueCommand).build())
                    .addCommand(new CommandBuilder("winner", winnerCommand).build())
                    .addCommand(new CommandBuilder("sendqueuemessage", sendQueueMessageCommand).build()));
        } catch (LoginException e1) {
            e1.printStackTrace();
        }
    }
}
