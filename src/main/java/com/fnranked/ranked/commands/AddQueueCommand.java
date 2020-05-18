package com.fnranked.ranked.commands;

import com.fnranked.ranked.api.entities.PermissionLevel;
import com.fnranked.ranked.api.entities.Region;
import com.fnranked.ranked.commands.commandhandler.listener.CommandListener;
import com.fnranked.ranked.util.PermissionUtil;
import com.fnranked.ranked.jpa.entities.MatchType;
import com.fnranked.ranked.jpa.entities.Queue;
import com.fnranked.ranked.jpa.repo.MatchTypeRepository;
import com.fnranked.ranked.jpa.repo.QueueRepository;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddQueueCommand implements CommandListener {

    @Autowired
    QueueRepository queueRepository;
    @Autowired
    MatchTypeRepository matchTypeRepository;
    @Autowired
    PermissionUtil permissionUtil;

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        if(!permissionUtil.hasPermission(sender.getUser(), PermissionLevel.MANAGEMENT)) return;
        message.delete().queue();
        if(args.length != 2) {
            channel.sendMessage("Use: `!addQueue <str match type> <str region>`").queue();
            return;
        }
        try {
            MatchType matchType = matchTypeRepository.findByName(args[0]).get();
            String region = args[1];
            Queue queue = new Queue();
            queue.setEnabled(true);
            queue.setInputMethodLocked(false);
            queue.setRegion(Region.valueOf(region.toUpperCase()));
            queue.setMatchType(matchType);
            queueRepository.save(queue);
            channel.sendMessage("Queue added").queue();
        }catch(Exception e) {
            channel.sendMessage("Invalid syntax once again. ").queue();
        }
    }
}
