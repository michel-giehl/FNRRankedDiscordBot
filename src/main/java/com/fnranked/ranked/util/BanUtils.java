package com.fnranked.ranked.util;

import com.fnranked.ranked.jpa.entities.MatchmakingBan;
import com.fnranked.ranked.jpa.entities.Player;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.MatchmakingBanRepository;
import com.fnranked.ranked.teams.TeamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Component
public class BanUtils {

    @Autowired
    UserUtils userUtils;
    @Autowired
    MatchmakingBanRepository matchmakingBanRepository;
    @Autowired
    TeamUtils teamUtils;

    @Transactional
    public boolean isBanned(long userId) {
        Player target = teamUtils.getPlayer(userId);
        Optional<MatchmakingBan> latestBanOpt = matchmakingBanRepository.findFirstByPlayerOrderByTimeOfBanDesc(target);
        if(latestBanOpt.isEmpty()) return false;
        MatchmakingBan latestBan = latestBanOpt.get();
        return latestBan.getTimeOfBan().toInstant().plusMillis(latestBan.getDurationMillis()).isAfter(Instant.now());
    }

    @Transactional
    public boolean isBanned(Team team) {
        return team.getPlayerList().stream().anyMatch(p -> isBanned(p.getId()));
    }

    @Transactional
    public void ban(long userId, long staffMemberId, long duration, String reason) {

    }

    @Transactional
    public void unban(long userId) {

    }

    @Transactional
    public void autoBan(long userId) {

    }
}
