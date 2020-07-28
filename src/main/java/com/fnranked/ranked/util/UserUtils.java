package com.fnranked.ranked.util;

import com.fnranked.ranked.api.entities.PermissionLevel;
import com.fnranked.ranked.api.entities.Result;
import com.fnranked.ranked.jpa.entities.MatchTemp;
import com.fnranked.ranked.jpa.entities.Player;
import com.fnranked.ranked.jpa.entities.Team;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserUtils {

    private OkHttpClient client;

    @Value("${registration.base_url}")
    private String baseUrl;

    public UserUtils() {
        this.client = new OkHttpClient();
    }

    @Autowired
    JDAContainer jdaContainer;
    @Autowired
    PermissionUtil permissionUtil;

    /**
     * Finds all members for a given match
     * @param matchTemp match object
     * @return list of members in that match who are also in the guild
     */
    public List<Member> getAllMembersInTempMatch(MatchTemp matchTemp) {
        List<Member> memberList = new ArrayList<>();
        Guild guild = jdaContainer.getJda().getGuildById(matchTemp.getMatchServer().getId());
        if(guild == null) return memberList;
        List<User> users = getUsersInMatch(matchTemp);
        for(User user : users) {
            Member m = guild.getMember(user);
            if(m != null)
                memberList.add(m);
        }
        return memberList;
    }

    public List<User> getUsersInMatch(MatchTemp matchTemp) {
        List<User> userList = new ArrayList<>();
        userList.addAll(getUsersInTeam(matchTemp.getTeamA()));
        userList.addAll(getUsersInTeam(matchTemp.getTeamB()));
        return userList;
    }

    public List<User> getUsersInTeam(Team team) {
        List<User> userList = new ArrayList<>();
        var players = team.getPlayerList();
        for(Player player : players) {
            long userId = player.getId();
            User u = jdaContainer.getJda().getUserById(userId);
            if(u != null)
                userList.add(u);
        }
        return userList;
    }

    public String getUsernamesInTeam(Team team) {
        List<User> users = getUsersInTeam(team);
        return users.stream().map(User::getName).collect(Collectors.joining(", "));
    }

    public void kickMembersAfterMatch(MatchTemp matchtemp) {
        List<Member> memberToKick = getAllMembersInTempMatch(matchtemp);
        for(Member m : memberToKick) {
            if(!permissionUtil.hasPermission(m.getUser(), PermissionLevel.STAFF)) {
                m.getGuild().kick(m).queue();
            }
        }
    }

    @Cacheable("epicUsers")
    public void retrieveRegistrationData(String userId, Result<JSONObject> result) {
        Request req = new Request.Builder().get().url(baseUrl + userId).build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //TODO implement this?

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body() != null) {
                    String res = response.body().string();
                    result.invoke(new JSONObject(res));
                }
                response.close();
            }
        });
    }

    public void updateRank() {
        //TODO implement this

    }
}
