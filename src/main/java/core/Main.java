package core;

import CommandSystem.commandHandler;
import CommandSystem.commandListener;
import commands.SendJoinMessageCommand;
import listener.JoinTournamentListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import util.BotSettings;

import javax.security.auth.login.LoginException;

public class Main {

    public static JDABuilder builder = new JDABuilder(AccountType.BOT);
    private static JDA jda;

    public static void main(String[] args) {
        builder.setToken(BotSettings.token);
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.setGame(Game.playing("Under maintenance"));

        try {
            jda = builder.build().awaitReady();

            jda.addEventListener(new commandListener());
            jda.addEventListener(new JoinTournamentListener());

            commandHandler.commands.put("sendjoinmsg", new SendJoinMessageCommand());
            commandHandler.commands.put("newtourney", new SendJoinMessageCommand());
            commandHandler.commands.put("newtournament", new SendJoinMessageCommand());
            commandHandler.commands.put("starttournament", new SendJoinMessageCommand());
        } catch (InterruptedException | LoginException e1) {
            e1.printStackTrace();
        }
    }

    public static JDA getJDA() {
        return jda;
    }
}