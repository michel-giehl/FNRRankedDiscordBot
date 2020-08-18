package com.fnranked.ranked.util;

import com.fnranked.ranked.api.entities.Region;
import com.fnranked.ranked.elo.EloCalculator;
import com.fnranked.ranked.jpa.entities.MatchType;
import com.fnranked.ranked.jpa.entities.RankedMatch;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.MatchTypeRepository;
import com.fnranked.ranked.jpa.repo.RankedMatchRepository;
import com.fnranked.ranked.teams.TeamUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ImportOldMatches {

    @Autowired
    RankedMatchRepository rankedMatchRepository;
    @Autowired
    MatchTypeRepository matchTypeRepository;
    @Autowired
    EloCalculator eloCalculator;
    @Autowired
    TeamUtils teamUtils;

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
            long leader = Long.parseLong(obj.getString("leader"));
            long pleb = Long.parseLong(obj.getString("pleb"));
            long created = Long.parseLong(obj.getString("duo_created"));
            Quadruple<Long, Long, Instant, Boolean> duo = new Quadruple<>(leader, pleb, Instant.ofEpochMilli(created), obj.getString("active").equals("1"));
            int id = obj.getInt("id");
            duos.put(id, duo);
        }
    }

    public void importMatches() {
        loadDuoData();
        JSONArray matches = getMatchData();
        List<MatchType> matchTypeList = (ArrayList<MatchType>)matchTypeRepository.findAll();
        int failed = 0;
        for(int i = 0; i < matches.length(); i++) {
            try {
                if (i % 50 == 0) {
                    System.out.println("Processing match " + i + "/" + matches.length());
                }
                JSONObject match = matches.getJSONObject(i);
                long winnerId = Long.parseLong(match.getString("winner"));
                long loserId = Long.parseLong(match.getString("loser"));
                String timestamp = match.getString("match_id");
                long time;
                if (timestamp.startsWith("k")) {
                    time = Long.parseLong(timestamp, 36);
                } else {
                    time = Long.parseLong(timestamp);
                }
                Region region = Region.parseRegion(match.getString("region"));
                MatchType matchType = matchTypeList.stream().filter(m -> m.getName().equals(match.getString("match_type"))).findFirst().get();
                Team teamA = getTeam(matchType, winnerId);
                Team teamB = getTeam(matchType, loserId);
                if (teamA != null && teamB != null) {
                    RankedMatch rankedMatch = new RankedMatch(teamA, teamB, matchType, region, Instant.ofEpochMilli(time));
                    rankedMatchRepository.save(eloCalculator.updateRatings(rankedMatch));
                }
            }catch(Exception e) {
                System.out.println("FAILED TO IMPORT A MATCH. SKIPPING");
                failed++;
            }
        }
        System.out.println("FINISHED IMPORTING MATCHES. FAILS: " + failed);
    }

    private Team getTeam(MatchType matchType, long id) {
        if(matchType.getTeamSize() == 1) {
            return teamUtils.getTeam(matchType, id);
        }
        if(duos.containsKey((int)id)) {
            var duo = duos.get((int)id);
            long captain = duo.getA();
            long pleb = duo.getB();
            Timestamp created = Timestamp.from(duo.getC());
            boolean active = duo.getD();
            Team team = teamUtils.getOldTeam(matchType, duo.getA(), duo.getB());
            if(team == null) {
                team = teamUtils.createDuo(captain, pleb, created, active);
            }
            return team;
        }
        System.out.println("NO TEAM WITH ID " + id + " FOUND.");
        return null;
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
