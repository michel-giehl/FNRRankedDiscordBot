package com.fnranked.ranked.jpa.entities;

import lombok.Data;
import net.dv8tion.jda.api.entities.Message;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
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
        this.messageId = message.getIdLong();
        this.channelId = message.getChannel().getIdLong();
    }
}
