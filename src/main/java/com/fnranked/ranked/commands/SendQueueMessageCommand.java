package com.fnranked.ranked.commands;

import com.fnranked.ranked.api.entities.PermissionLevel;
import com.fnranked.ranked.commands.commandhandler.listener.CommandListener;
import com.fnranked.ranked.messages.MessageUtils;
import com.fnranked.ranked.util.PermissionUtil;
import com.fnranked.ranked.jpa.entities.MatchType;
import com.fnranked.ranked.jpa.entities.Queue;
import com.fnranked.ranked.jpa.entities.QueueMessage;
import com.fnranked.ranked.jpa.repo.MatchTypeRepository;
import com.fnranked.ranked.jpa.repo.QueueMessageRepository;
import com.fnranked.ranked.jpa.repo.QueueRepository;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    @Autowired
    MessageUtils messageUtils;
    @Autowired
    MatchTypeRepository matchTypeRepository;
    @Autowired
    PermissionUtil permissionUtil;

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        if(!permissionUtil.hasPermission(sender.getUser(), PermissionLevel.MANAGEMENT)) return;
        message.delete().queue();
        TextChannel c = channel;
        if(args.length == 1) {
            c = message.getMentionedChannels().get(0);
        }
        List<Queue> queues = queueRepository.findAllByEnabledIsTrue();
        Set<String> emotes = queues.stream().map(q -> q.getMatchType().getDisplayEmote()).collect(Collectors.toSet());
        c.sendMessage(messageUtils.getQueueEmbed(queueRepository.findAllByEnabledIsTrue().stream().map(Queue::getMatchType).collect(Collectors.toList()), null)).queue(msg -> {
            QueueMessage queueMessage = new QueueMessage();
            queueMessage.setChannelId(msg.getChannel().getIdLong());
            queueMessage.setQueueMessageId(msg.getIdLong());
            queueMessage.setDMQueue(false);
            queueMessageRepository.save(queueMessage);
            channel.sendMessage("Queue message sent.").delay(10, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            emotes.forEach(s -> msg.addReaction(s).queue());
        });
    }
}
