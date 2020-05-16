package com.fnranked.ranked.api.entities;

public interface Result<T> {
    void invoke(T t);
}
