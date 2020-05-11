package com.fnranked.ranked.discord.commands.commandhandler;

import com.fnranked.ranked.discord.commands.commandhandler.command.Command;
import net.dv8tion.jda.api.JDA;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

public class CommandHandlerBuilder {

    protected ArrayList<Command> commandList = new ArrayList<>();
    JDA jdaObject;
    String prefix;

    public CommandHandlerBuilder(JDA jdaObject) {
        if (jdaObject == null) {
            throw new IllegalArgumentException("JDA null");
        }

        this.jdaObject = jdaObject;
    }

    public CommandHandlerBuilder setPrefix(String prefix) {
        this.prefix = prefix;

        return this;
    }

    public CommandHandlerBuilder addCommand(Command commandClass) {
        commandList.add(commandClass);

        return this;
    }

    public CommandHandler build() {
        return new CommandHandler(this);
    }
}
