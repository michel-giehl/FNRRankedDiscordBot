package com.fnranked.ranked.jpa.entities;

import net.dv8tion.jda.api.entities.Message;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MatchMessages {

    /**
     * message Id used as ID
     */

    @Id
    @GeneratedValue
    long Id;

    long messageId;

    long channelId;

    public MatchMessages() {

    }

    public MatchMessages(Message message) {
        System.out.println("NEW MATCH MESsAGTE");
        this.messageId = message.getIdLong();
        this.channelId = message.getChannel().getIdLong();
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }
}
