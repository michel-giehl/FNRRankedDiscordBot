package com.fnranked.ranked.commands.commandhandler;

import com.fnranked.ranked.commands.commandhandler.command.Command;
import net.dv8tion.jda.api.JDA;
import org.springframework.stereotype.Component;

@Component
public class CommandLoader {

    private CommandHandlerBuilder builder;

    public void loadCommands(JDA jda) {

    }

    public void addCommand(Command command) {
        builder.addCommand(command);
    }
}
