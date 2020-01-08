package com.fnranked.tournament.creator;

import com.fnranked.tournament.data.Tournament;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TournamentEditor implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(TournamentEditor.class);
    private User user;
    private Message currentMessage;
    private Tournament tournament;

    /**
     * opens tournament creator options
     *
     * @param user user
     */
    public TournamentEditor(User user, Tournament tournament) {
        this.user = user;
        this.tournament = tournament;
    }

    @Override
    public void run() {
        //DM the user to start configuring/creating a tournament.
        if (!user.isBot()) {
            logger.info(user.getAsTag() + " entered tournament creator");
            user.openPrivateChannel().submit();
            EmbedBuilder tournamentEmbedBuilder = new EmbedBuilder();
            tournamentEmbedBuilder.setTitle("Tournament Creator");
            tournamentEmbedBuilder.setDescription(tournamentStringBuilder());
            tournamentEmbedBuilder.setFooter("Tournament Bot Created by KillerManDan#3850 with help from GiM and Beatz");
            user.openPrivateChannel().queue((channel) ->
                    channel.sendMessage(tournamentEmbedBuilder.build()).complete());
        }
    }

    private String tournamentStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format(":two: Set Prize pool (currently disabled)- [$%s]\n", tournament.getPrizePool()));
        stringBuilder.append(String.format(":three: Set max players - [%s]\n", tournament.getMaxPlayers()));
        stringBuilder.append(String.format(":four: Set starting date [%s]\n", tournament.getStartTime()));
        stringBuilder.append(String.format(":five: Set tournament mode (K.O / Rounds / SWISS / Ladder) [%s]\n", tournament.getTournamentMode()));
        stringBuilder.append(String.format(":earth_americas: Set tournament region [%s]\n", tournament.getRegion()));
        stringBuilder.append(String.format(":map: Set map code [%s]\n\n", tournament.getMapCode()));
        if (tournament.getPrivacy()) {
            stringBuilder.append("Join requirements/Privacy [PRIVATE]\n");
            stringBuilder.append(":unlock: Set privacy to: OPEN\n");
            stringBuilder.append(String.format(":key: Set password (for PRIVATE tournaments only) [%s]\n\n", tournament.getPassword()));
        } else {
            stringBuilder.append("Join requirements/Privacy [OPEN]\n");
            stringBuilder.append(":lock: Set privacy to: PRIVATE\n\n");
        }
        //TODO: implement multiple joining mechanisms.
        //stringBuilder.append(String.format(":six: Toggle tournament joining mode (FIRST COME FIRST SERVE/ RANDOM) [%s]\n\n"));
        stringBuilder.append(String.format(":chart_with_upwards_trend:  Set min. Elo [%s]\n", tournament.getMinElo()));
        stringBuilder.append(String.format(":chart_with_downwards_trend: Set max. Elo [%s]\n\n", tournament.getMaxElo()));
        stringBuilder.append(":red_circle: delete\n");
        stringBuilder.append(":green_circle: publish\n");

        return stringBuilder.toString();
    }
}
