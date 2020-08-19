package com.fnranked.ranked.jpa.entities;

import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Map;

@Entity
@Data
public class QueueMessage {

    @Id
    long queueMessageId;

    @NonNull
    boolean isDMQueue;

    @Nullable
    long channelId;

    @Nullable
    long userId;

    @ManyToMany
    Map<Long, Queue> queues;

    public QueueMessage() {

    }
}
