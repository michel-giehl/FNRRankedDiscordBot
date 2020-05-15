package com.fnranked.ranked.discord.listener;

import com.fnranked.ranked.discord.util.ChannelManager;
import com.fnranked.ranked.jpa.entities.Team;
import com.fnranked.ranked.jpa.repo.MatchMessagRepo;
import com.fnranked.ranked.jpa.repo.MatchTempRepository;
import com.fnranked.ranked.jpa.util.MatchUtils;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;

@Component
public class MatchAcceptListener extends ListenerAdapter {

    @Autowired
    MatchMessagRepo matchMessagRepo;
    @Autowired
    MatchTempRepository matchTempRepository;
    @Autowired
    MatchUtils matchUtils;
    @Autowired
    ChannelManager channelManager;

    @Override
    @Transactional
    public void onPrivateMessageReactionAdd(@Nonnull PrivateMessageReactionAddEvent event) {
        if(event.getUser().isBot()) return;
        long msgId = event.getMessageIdLong();
        matchMessagRepo.findFirstByMessageId(msgId).ifPresent(matchMessages ->
            matchTempRepository.findByMatchMessageId(matchMessages.getId()).ifPresent(matchTemp -> {
                Team userTeam = matchUtils.getTeamByUserId(event.getUserIdLong(), matchTemp);
                boolean isTeamA = userTeam.equals(matchTemp.getTeamA());
                if(isTeamA) {
                    matchTemp.setTeamAAccepted(true);
                } else {
                    matchTemp.setTeamBAccepted(true);
                }
                matchTempRepository.save(matchTemp);
                if(matchTemp.isTeamAAccepted() && matchTemp.isTeamBAccepted()) {
                    channelManager.createMatchChannel(matchTemp);
                }
            })
        );
    }
}
