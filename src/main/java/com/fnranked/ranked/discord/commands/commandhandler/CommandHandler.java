package com.fnranked.ranked.discord.commands.commandhandler;


import com.fnranked.ranked.discord.commands.commandhandler.command.Command;

public class CommandHandler {
    private CommandHandlerBuilder commandHandlerBuilder;

    CommandHandler(CommandHandlerBuilder commandHandlerBuilder) {
        this.commandHandlerBuilder = commandHandlerBuilder;
    }

    public void addCommand(Command commandClass) {
        this.commandHandlerBuilder.commandList.add(commandClass);
    }
}
