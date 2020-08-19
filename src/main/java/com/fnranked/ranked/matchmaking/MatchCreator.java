package com.fnranked.ranked.matchmaking;

import com.fnranked.ranked.api.entities.Region;
import com.fnranked.ranked.jpa.entities.CreativeMap;
import com.fnranked.ranked.jpa.entities.MatchTemp;
import com.fnranked.ranked.jpa.entities.MatchType;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.CreativeMapRepository;
import com.fnranked.ranked.jpa.repo.MatchTempRepository;
import com.fnranked.ranked.messages.MessageUtils;
import com.fnranked.ranked.util.ChannelCreator;
import com.fnranked.ranked.util.MatchUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class MatchCreator {

    Logger logger = LoggerFactory.getLogger(MatchCreator.class);

    @Autowired
    MatchTempRepository matchTempRepository;
    @Autowired
    ChannelCreator channelCreator;
    @Autowired
    CreativeMapRepository creativeMapRepository;
    @Autowired
    MatchUtils matchUtils;
    @Autowired
    MessageUtils messageUtils;

    @Value("${match.match_accept_timer}")
    long ttl;

    @Transactional
    public void createMatch(MatchType matchType, Region region, Team teamA, Team teamB) {
        Optional<CreativeMap> map = creativeMapRepository.selectRandom(matchType);
        if (map.isPresent()) {
            var tmpMatch = new MatchTemp(matchType, map.get(), teamA, teamB);
            tmpMatch.setRegion(region);
            messageUtils.sendMatchAccept(matchTempRepository.save(tmpMatch).getId());
            messageUtils.deleteDMQueueMessages(teamA);
            messageUtils.deleteDMQueueMessages(teamB);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    matchTempRepository.findById(tmpMatch.getId()).ifPresent(matchTemp -> {
                        if (!matchTemp.isTeamAAccepted() || !matchTemp.isTeamBAccepted()) {
                            matchTempRepository.delete(matchTemp);
                            matchUtils.dMCaptains(matchTemp, messageUtils.getMatchNotAcceptedEmbed());
                        }
                    });
                }
            }, ttl);
            logger.info(String.format("New %s %s Match.", region.name(), matchType.getName()));
        } else {
            logger.error("Failed to create match: No map code present.");
        }
    }
}
