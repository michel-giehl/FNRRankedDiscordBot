package com.fnranked.ranked.creator;

import com.fnranked.ranked.data.TournamentRepository;
import com.fnranked.ranked.data.UserRepository;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Map;

@Component
public class ReactionListener implements EventListener {


    private Map<String,String> emoteCommandMap = Map.of(":emote1:","cancel",":emote2:","");

    TournamentRepository tournamentRepository;

    UserRepository userRepository;

    private static Logger logger = LoggerFactory.getLogger(ReactionListener.class);

    @Autowired
    private void TournamentRepository(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Autowired
    private void UserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onEvent(@Nonnull GenericEvent genericEvent) {
        if (genericEvent instanceof PrivateMessageReactionAddEvent && !((PrivateMessageReactionAddEvent) genericEvent).getUser().isBot()) {
            //If one is in progress, respond/edit it with the reaction requested
            if (!tournamentRepository.findAllByHostDiscordIDIsAndCompletedIsFalseAndPublishedFalse(((PrivateMessageReactionAddEvent) genericEvent).getUser().getIdLong()).isEmpty()){
                ((PrivateMessageReactionAddEvent) genericEvent).getReaction().toString();


            }
        }
    }
}
