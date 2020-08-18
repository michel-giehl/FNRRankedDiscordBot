package com.fnranked.ranked.commands.commandhandler;

import com.fnranked.ranked.commands.commandhandler.command.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.Arrays;

@Component
public class CommandHandlerListener extends ListenerAdapter {

    private CommandHandlerBuilder commandHandlerBuilder;

    public CommandHandlerListener() {
        //empty constructor
    }

    public void init(CommandHandlerBuilder commandHandlerBuilder) {
        this.commandHandlerBuilder = commandHandlerBuilder;
    }

    @Override
    @Transactional
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        String commandPrefix = "!";
        if (!args[0].startsWith(commandPrefix)) {
            return;
        }

        String command = args[0].replace(commandPrefix, "");
        commandHandlerBuilder.commandList.forEach(c -> {
            if (c.getCommandName().equals(command)) {
                handleCommand(c, event.getMember(), event.getChannel(), event.getMessage(), args);
            }
        });
    }

    private void handleCommand(Command command, Member sender, TextChannel channel, Message message, String[] args) {
        if (!command.getBotAllowance() && message.getAuthor().isBot()) {
            return;
        }
        command.getHandlerListener().onCommand(sender, channel, message, Arrays.copyOfRange(args, 1, args.length));
    }
}
