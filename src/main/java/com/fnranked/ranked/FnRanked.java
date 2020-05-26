package com.fnranked.ranked;

import com.fnranked.ranked.commands.*;
import com.fnranked.ranked.commands.commandhandler.CommandHandlerBuilder;
import com.fnranked.ranked.commands.commandhandler.CommandHandlerListener;
import com.fnranked.ranked.commands.commandhandler.command.CommandBuilder;
import com.fnranked.ranked.listener.*;
import com.fnranked.ranked.util.JDAContainer;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.requests.RestAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;

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
    CancelCommand cancelCommand;

    @Autowired
    QueueListener queueListener;
    @Autowired
    GuildMemberJoinListener guildMemberJoinListener;
    @Autowired
    MatchAcceptListener matchAcceptListener;
    @Autowired
    MatchVoteListener matchVoteListener;
    @Autowired
    DuoListener duoListener;



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
            jda.addEventListener(duoListener);

            commandHandlerListener.init(
            new CommandHandlerBuilder(jda).setPrefix("!")
                    .addCommand(new CommandBuilder("addmatchtype", addMatchTypeCommand).build())
                    .addCommand(new CommandBuilder("addqueue", addQueueCommand).build())
                    .addCommand(new CommandBuilder("winner", winnerCommand).build())
                    .addCommand(new CommandBuilder("cancel", cancelCommand).build())
                    .addCommand(new CommandBuilder("sendqueuemessage", sendQueueMessageCommand).build()));
        } catch (LoginException e1) {
            e1.printStackTrace();
        }
        //Ignore DM context exceptions
        RestAction.setDefaultFailure(new ErrorHandler().ignore(ErrorResponse.CANNOT_SEND_TO_USER));
    }
}
