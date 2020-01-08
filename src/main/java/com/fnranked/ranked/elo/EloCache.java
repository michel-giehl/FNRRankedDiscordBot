package com.fnranked.ranked.elo;

import com.fnranked.ranked.data.User;
import com.google.common.cache.Cache;

public class EloCache {

    Cache<User, Elo> eloCache;

    public void load() {

    }

    public void refresh() {

    }

    public Elo get(User user) {
        return eloCache.getIfPresent(user);
    }
}
