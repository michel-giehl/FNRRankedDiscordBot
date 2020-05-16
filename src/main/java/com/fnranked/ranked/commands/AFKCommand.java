package com.fnranked.ranked.commands;

import com.fnranked.ranked.api.entities.PermissionLevel;
import com.fnranked.ranked.commands.commandhandler.listener.CommandListener;
import com.fnranked.ranked.util.PermissionUtil;
import com.fnranked.ranked.jpa.repo.MatchTempRepository;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@EnableScheduling
@Component
public class AFKCommand extends ListenerAdapter implements CommandListener{

    @Autowired
    PermissionUtil permissionUtil;
    @Autowired
    MatchTempRepository matchTempRepository;

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        if(!permissionUtil.hasPermission(sender.getUser(), PermissionLevel.STAFF)) return;
    }

    @Override
    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {

    }
}
