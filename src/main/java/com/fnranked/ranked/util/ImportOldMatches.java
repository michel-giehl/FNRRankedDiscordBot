package com.fnranked.ranked.util;

import com.fnranked.ranked.api.entities.MatchStatus;
import com.fnranked.ranked.jpa.entities.MatchType;
import com.fnranked.ranked.jpa.repo.RankedMatchRepository;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ImportOldMatches {

    @Autowired
    RankedMatchRepository rankedMatchRepository;

    //This provides an easy way to retrieve duos by their ID
    public static ConcurrentHashMap<Integer, Quadruple<Long, Long, Instant, Boolean>> duos = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        loadDuoData();
    }

    public static void loadDuoData() {
        JSONArray data = new JSONArray(readFileFromResources("duo.json"));
        data = data.getJSONObject(2).getJSONArray("data");
        for(int i = 0; i < data.length(); i++) {
            JSONObject obj = data.getJSONObject(i);
            Quadruple<Long, Long, Instant, Boolean> duo = new Quadruple<>(obj.getLong("leader"), obj.getLong("pleb"), Instant.ofEpochMilli(obj.getLong("duo_created")), obj.getInt("active") == 1);
            int id = obj.getInt("id");
            duos.put(id, duo);
        }
        System.out.println("DUOS: " + duos.size());
    }

    public void importMatches() {
        JSONArray matches = getMatchData();
        MatchStatus status = MatchStatus.FINISHED;
        List<MatchType> matchTypeList;

    }

    public static JSONArray getMatchData() {
        JSONArray data = new JSONArray(readFileFromResources("match_history.json"));
        data = data.getJSONObject(2).getJSONArray("data");
        return data;
    }

    private static String readFileFromResources(String fileName) {
        try {
            return IOUtils.resourceToString(fileName, StandardCharsets.UTF_8, ImportOldMatches.class.getClassLoader());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Can't read";
    }
}
