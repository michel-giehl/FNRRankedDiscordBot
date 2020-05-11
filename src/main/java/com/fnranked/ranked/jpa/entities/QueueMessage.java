package com.fnranked.ranked.jpa.entities;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Map;

@Entity
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

    public long getQueueMessageId() {
        return queueMessageId;
    }

    public void setQueueMessageId(long queueMessageId) {
        this.queueMessageId = queueMessageId;
    }

    public boolean isDMQueue() {
        return isDMQueue;
    }

    public void setDMQueue(boolean DMQueue) {
        isDMQueue = DMQueue;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
