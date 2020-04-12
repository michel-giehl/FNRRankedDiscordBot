package com.fnranked.ranked.util;

import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class GetUser {

    private OkHttpClient client;

    private static String BASE_URL = "127.0.0.1:8091/user/";

    public GetUser() {
        client = new OkHttpClient();
    }

    public void getUser(String userId) {
        Request req = new Request.Builder().get().url(BASE_URL + userId).build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
    }
}
