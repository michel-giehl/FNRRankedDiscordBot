package com.fnranked.ranked.commands.commandhandler.command;

import com.fnranked.ranked.commands.commandhandler.listener.CommandListener;
import net.dv8tion.jda.api.Permission;

import java.util.ArrayList;

public class Command {
    private CommandBuilder commandBuilder;

    public Command(CommandBuilder commandBuilder) {
        this.commandBuilder = commandBuilder;
    }

    public String getCommandName() {
        return commandBuilder.commandName;
    }

    public String getCommandAlias() {
        return commandBuilder.commandAlias;
    }

    public String getCommandDescription() {
        return commandBuilder.commandDescription;
    }

    public Boolean getBotAllowance() {
        return commandBuilder.botReply;
    }

    public ArrayList<Long> getCommandChannels() {
        return commandBuilder.commandChannels;
    }

    public ArrayList<Permission> getCommandPermissions() {
        return commandBuilder.commandPermissions;
    }

    public CommandListener getHandlerListener() {
        return commandBuilder.handlerListener;
    }
}
