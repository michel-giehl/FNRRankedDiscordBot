package com.fnranked.ranked.discord.listener;

import com.fnranked.ranked.jpa.repo.QueueMessageRepository;
import com.fnranked.ranked.jpa.repo.QueueRepository;
import com.fnranked.ranked.jpa.repo.TeamRepository;
import com.fnranked.ranked.jpa.util.TeamUtils;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;

@Component
public class QueueListener extends ListenerAdapter {

    @Autowired
    QueueRepository queueRepository;
    @Autowired
    QueueMessageRepository queueMessageRepository;
    @Autowired
    TeamUtils teamUtils;

    @Transactional
    @Override
    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        queueMessageRepository.findByQueueMessageId(event.getMessageIdLong()).ifPresent(qMsg -> {

        });
    }
}
