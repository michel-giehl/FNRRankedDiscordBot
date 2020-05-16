package com.fnranked.ranked.commands.commandhandler.listener;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public interface CommandListener {
    void onCommand(Member sender, TextChannel channel, Message message, String[] args);
}
