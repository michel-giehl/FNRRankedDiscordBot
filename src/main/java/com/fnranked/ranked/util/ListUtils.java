package com.fnranked.ranked.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    public static <E> List<E> merge(final List<? extends E>... lists) {
        ArrayList<E> result = new ArrayList<>();
        for (int i = 0; i < lists.length; i++) {
            result.addAll(lists[i]);
        }
        return result;
    }
}
