package com.fnranked.ranked;

import com.fnranked.ranked.creator.TournamentEditor;
import com.fnranked.ranked.data.Tournament;
import com.fnranked.ranked.data.TournamentRepository;
import com.fnranked.ranked.data.User;
import com.fnranked.ranked.data.UserRepository;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

@Component
public class RegistrationMessageListener implements EventListener {

    TournamentRepository tournamentRepository;

    UserRepository userRepository;

    private static Logger logger = LoggerFactory.getLogger(RegistrationMessageListener.class);

    @Autowired
    private void TournamentRepository(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Autowired
    private void UserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${tournament.create.channel}")
    private long tournamentChannelID;

    @Override
    public void onEvent(@Nonnull GenericEvent genericEvent) {
        if (genericEvent instanceof GuildMessageReactionAddEvent &&
                ((GuildMessageReactionAddEvent) genericEvent).getChannel().getIdLong() == tournamentChannelID) {

            //If no currently unpublished/configured tournaments exist then create a new one.
            List<Tournament> tournamentList = tournamentRepository.findAllByHostDiscordIDIsAndCompletedIsFalseAndPublishedFalse(((GuildMessageReactionAddEvent) genericEvent).getUser().getIdLong());

            if (tournamentList.isEmpty()) {
                // Create a new tournament for this user.
                NewTournament(genericEvent);
            } else {
                //Unpublished tournament exists so don't create a new one, just send them the old one.
                CreateTournamentEditor(((GuildMessageReactionAddEvent) genericEvent).getUser(), tournamentList.get(0));
            }

        }
    }

    private void NewTournament(GenericEvent genericEvent) {
        

        Optional<User> user = userRepository.findByDiscordID(((GuildMessageReactionAddEvent) genericEvent).getUser().getIdLong());

        if (user.isPresent()) {
            Tournament tournament = new Tournament(user.get());
            tournamentRepository.save(tournament);
            CreateTournamentEditor(((GuildMessageReactionAddEvent) genericEvent).getUser(), tournament);

        } else if (((GuildMessageReactionAddEvent) genericEvent).getUser().getIdLong() == 138341918298996736L) {
            //Used in testing, do we just want to keep our own user database?
            User killermandan = new User(138341918298996736L);
            userRepository.save(killermandan);
            Tournament tournament = new Tournament(killermandan);
            tournamentRepository.save(tournament);
            CreateTournamentEditor(((GuildMessageReactionAddEvent) genericEvent).getUser(), tournament);

        } else {
            logger.warn(String.format("%s (<@%s>) attempted to begin tournament creation, however was not found in the User database.", ((GuildMessageReactionAddEvent) genericEvent).getUser().getAsTag(),
                    ((GuildMessageReactionAddEvent) genericEvent).getUser().getIdLong()));
            //DM/Inform user?
        }
    }

    private void CreateTournamentEditor(net.dv8tion.jda.api.entities.User user, Tournament tournament) {

        TournamentEditor tournamentEditor = new TournamentEditor(user, tournament);
        Thread createTournament = new Thread(tournamentEditor);
        createTournament.start();
    }

}
