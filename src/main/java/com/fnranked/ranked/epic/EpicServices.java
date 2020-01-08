package com.fnranked.ranked.epic;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public enum EpicServices {
    UP,
    DOWN;

    public static void retrieve(CompletableFuture<EpicServices> OnlineState) {
        OkHttpClient client = new OkHttpClient();

        String statusURL = "https://lightswitch-public-service-prod.ol.epicgames.com/"
                + "lightswitch/api/service/bulk/status?serviceId=Fortnite";
        Request req = new Request.Builder().url(statusURL).build();
        client.newCall(req).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                OnlineState.complete(DOWN);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body() != null) {
                    var jsonArray = new JSONArray(response.body().string());
                    var up = jsonArray.getJSONObject(0).getString("status");
                    OnlineState.complete(EpicServices.valueOf(up));
                }
            }
        });
    }
}
