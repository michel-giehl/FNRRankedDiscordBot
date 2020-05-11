package com.fnranked.ranked.discord.commands;

import com.fnranked.ranked.discord.commands.commandhandler.listener.CommandListener;
import com.fnranked.ranked.jpa.entities.Queue;
import com.fnranked.ranked.jpa.entities.QueueMessage;
import com.fnranked.ranked.jpa.repo.QueueMessageRepository;
import com.fnranked.ranked.jpa.repo.QueueRepository;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class SendQueueMessageCommand implements CommandListener {

    @Autowired
    QueueMessageRepository queueMessageRepository;
    @Autowired
    QueueRepository queueRepository;

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        message.delete().queue();
        TextChannel c = channel;
        if(args.length == 1) {
            c = message.getMentionedChannels().get(0);
        }
        List<Queue> queues = queueRepository.findAllWhereEnabledIsTrue();
        Set<String> emotes = queues.stream().map(q -> q.getMatchType().getDisplayEmote()).collect(Collectors.toSet());
        c.sendMessage("Queue").queue(msg -> {
            QueueMessage queueMessage = new QueueMessage();
            queueMessage.setChannelId(msg.getChannel().getIdLong());
            queueMessage.setQueueMessageId(msg.getIdLong());
            queueMessageRepository.save(queueMessage);
            channel.sendMessage("Queue message sent.").queue(m -> m.delete().queueAfter(10, TimeUnit.SECONDS, s -> {}, e -> {}));
            emotes.forEach(s -> msg.addReaction(s).queue());
        });
    }
}
