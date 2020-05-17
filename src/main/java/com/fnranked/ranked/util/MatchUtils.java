package com.fnranked.ranked.util;

import com.fnranked.ranked.api.entities.MatchStatus;
import com.fnranked.ranked.elo.EloCalculator;
import com.fnranked.ranked.jpa.entities.RankedMatch;
import com.fnranked.ranked.jpa.repo.*;
import com.fnranked.ranked.util.JDAContainer;
import com.fnranked.ranked.util.UserUtils;
import com.fnranked.ranked.jpa.entities.MatchServer;
import com.fnranked.ranked.jpa.entities.MatchTemp;
import com.fnranked.ranked.jpa.entities.Team;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class MatchUtils {

    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    MatchTempRepository matchTempRepository;
    @Autowired
    RankedMatchRepository rankedMatchRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    MatchServerRepo matchServerRepo;
    @Autowired
    JDAContainer jdaContainer;
    @Autowired
    UserUtils userUtils;
    @Autowired
    ChannelCreator channelCreator;
    @Autowired
    EloCalculator eloCalculator;

    @Transactional
    public List<MatchTemp> findAllMatchesByUserIdInMatchServer(long userId, long matchServerId) {
        List<MatchTemp> matches = new ArrayList<>();
        var playerOpt = playerRepository.findById(userId);
        if(playerOpt.isEmpty()) return matches;
        var teams = teamRepository.findAllByPlayerListContaining(playerOpt.get());
        MatchServer matchServer = matchServerRepo.findById(matchServerId).get();
        for(Team team : teams) {
            var match = matchTempRepository.findByTeamAOrTeamBAndMatchServer(team, team, matchServer);
            match.ifPresent(matches::add);
        }
        return matches;
    }

    public void DMCaptains(MatchTemp matchTemp, MessageEmbed messageEmbed) {
        long captainA = matchTemp.getTeamA().getCaptain().getId();
        long captainB = matchTemp.getTeamB().getCaptain().getId();
        JDA jda = jdaContainer.getJda();
        var users = List.of(jda.getUserById(captainA), jda.getUserById(captainB));
        for(User u : users) {
            u.openPrivateChannel().queue(pc ->pc.sendMessage(messageEmbed).queue(msg -> {}, e-> {}));
        }
    }

    public Team getTeamByUserId(long userId, MatchTemp matchTemp) {
        if(matchTemp.getTeamA().getPlayerList().stream().anyMatch(p -> p.getId() == userId)) {
            return matchTemp.getTeamA();
        } else {
            return matchTemp.getTeamB();
        }
    }

    public void endMatch(MatchTemp matchTemp, @Nullable Team winner) {
        final MatchStatus matchStatus;
        if(winner == null) {
            matchStatus = MatchStatus.CANCELED;
        } else {
            matchStatus = MatchStatus.FINISHED;
        }
        //Calculate elo, update teams, save match
        RankedMatch rankedMatch = new RankedMatch(matchTemp, winner, matchStatus);
        rankedMatch = eloCalculator.updateRatings(rankedMatch);
        rankedMatchRepository.save(rankedMatch);
        //Get rid of all the trash
        matchTempRepository.delete(matchTemp);
        userUtils.kickMembersAfterMatch(matchTemp);
        channelCreator.deleteChannel(matchTemp);
    }
}
