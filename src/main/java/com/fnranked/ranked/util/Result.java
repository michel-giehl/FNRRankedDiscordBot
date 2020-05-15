package com.fnranked.ranked.util;

public interface Result<T> {
    void invoke(T t);
}
