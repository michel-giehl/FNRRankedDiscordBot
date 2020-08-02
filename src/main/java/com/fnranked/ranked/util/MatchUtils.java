package com.fnranked.ranked.util;

import com.fnranked.ranked.api.entities.MatchStatus;
import com.fnranked.ranked.elo.EloCalculator;
import com.fnranked.ranked.jpa.entities.MatchServer;
import com.fnranked.ranked.jpa.entities.MatchTemp;
import com.fnranked.ranked.jpa.entities.RankedMatch;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.*;
import com.fnranked.ranked.messages.MessageUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class MatchUtils {

    Logger logger = LoggerFactory.getLogger(MatchUtils.class);

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
    @Autowired
    MessageUtils messageUtils;
    @Autowired
    MatchTypeRepository matchTypeRepository;

    @Transactional
    public List<MatchTemp> findAllMatchesByUserIdInMatchServer(long userId, long matchServerId) {
        List<MatchTemp> matches = new ArrayList<>();
        var playerOpt = playerRepository.findById(userId);
        if (playerOpt.isEmpty()) return matches;
        var teams = teamRepository.findAllByPlayerListContaining(playerOpt.get());
        MatchServer matchServer = matchServerRepo.findById(matchServerId).get();
        for (Team team : teams) {
            var match = matchTempRepository.findByTeamAOrTeamBAndMatchServer(team, team, matchServer);
            match.ifPresent(matches::add);
        }
        return matches;
    }

    public void dMCaptains(MatchTemp matchTemp, MessageEmbed messageEmbed) {
        long captainA = matchTemp.getTeamA().getCaptain().getId();
        long captainB = matchTemp.getTeamB().getCaptain().getId();
        JDA jda = jdaContainer.getJda();
        var users = List.of(jda.getUserById(captainA), jda.getUserById(captainB));
        for (User u : users) {
            u.openPrivateChannel().queue(pc -> pc.sendMessage(messageEmbed).queue(msg -> {
            }, e -> {
            }));
        }
    }

    public Team getTeamByUserId(long userId, MatchTemp matchTemp) {
        if (matchTemp.getTeamA().getPlayerList().stream().anyMatch(p -> p.getId() == userId)) {
            return matchTemp.getTeamA();
        } else {
            return matchTemp.getTeamB();
        }
    }

    public void endMatch(MatchTemp matchTemp, @Nullable Team winner) {
        final MatchStatus matchStatus;
        if (winner == null) {
            matchStatus = MatchStatus.CANCELED;
        } else {
            matchStatus = MatchStatus.FINISHED;
        }
        logger.info(String.format("Match ended. Status:%s", matchStatus.toString()));
        //Calculate elo, update teams, save match
        RankedMatch rankedMatch = new RankedMatch(matchTemp, winner, matchStatus);
        if (matchStatus == MatchStatus.FINISHED)
            rankedMatch = eloCalculator.updateRatings(rankedMatch);
        rankedMatchRepository.save(rankedMatch);
        //Get rid of all the trash
        teamRepository.delete(matchTemp.getTeamA());
        teamRepository.delete(matchTemp.getTeamB());
        matchTempRepository.delete(matchTemp);
        userUtils.kickMembersAfterMatch(matchTemp);
        channelCreator.deleteChannel(matchTemp);
        final RankedMatch finalRankedMatch = rankedMatch;
        //DM users
        //TODO refactor maybe
        for (User user : userUtils.getUsersInMatch(matchTemp)) {
            if (matchStatus == MatchStatus.FINISHED) {
                boolean isWinner = rankedMatch.getWinner().getPlayerList().stream().anyMatch(p -> p.getId() == user.getIdLong());
                user.openPrivateChannel().flatMap(pc -> pc.sendMessage(messageUtils.getMatchHistoryEmbed(finalRankedMatch, isWinner))).queue();
            } else {
                user.openPrivateChannel().flatMap(pc -> pc.sendMessage(messageUtils.getMatchCanceledEmbed())).queue();
            }
            user.openPrivateChannel().queue(pc -> messageUtils.sendQueueMessage(user, pc));
        }
    }
}
