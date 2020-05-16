package com.fnranked.ranked.commands.commandhandler.command;

import com.fnranked.ranked.commands.commandhandler.listener.CommandListener;
import net.dv8tion.jda.api.Permission;

import java.util.ArrayList;

public class CommandBuilder {

    String commandName;
    String commandAlias = "";
    String commandDescription;
    Boolean botReply = false;
    ArrayList<Long> commandChannels = new ArrayList<>();
    ArrayList<Permission> commandPermissions = new ArrayList<>();
    CommandListener handlerListener;

    public CommandBuilder(String commandName, CommandListener handlerListener) {
        if (commandName == null || handlerListener == null) {
            throw new IllegalArgumentException("The command name or listener must not be null!");
        }

        this.commandName = commandName;
        this.handlerListener = handlerListener;
    }

    public CommandBuilder setAlias(String commandAlias) {
        this.commandAlias = commandAlias;

        return this;
    }

    public CommandBuilder setDescription(String commandDescription) {
        this.commandDescription = commandDescription;

        return this;
    }

    public CommandBuilder addAllowedChannel(long channelId) {
        commandChannels.add(channelId);

        return this;
    }

    public CommandBuilder addPermission(Permission permissionId) {
        commandPermissions.add(permissionId);

        return this;
    }

    public CommandBuilder allowBotReply(boolean botReply) {
        this.botReply = botReply;

        return this;
    }

    public Command build() {
        return new Command(this);
    }
}
