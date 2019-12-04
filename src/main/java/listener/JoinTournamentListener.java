package listener;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.impl.UserImpl;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import util.BotSettings;
import util.TournamentSettings;

public class JoinTournamentListener extends ListenerAdapter {
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if(event.getUser().isBot())
            return;

        Emote emote = event.getReactionEmote().getEmote();

        if(emote == null)
            return;

        if(emote.equals(BotSettings.joinTourneyEmote)) {
            String messageId = event.getMessageId();
            if(messageId.equals(TournamentSettings.joinMessageId)) {
                // right emote & message
                if(TournamentSettings.isJoinable) {
                    System.out.println("test");
                    User user = event.getUser();

                    if (!TournamentSettings.joinedUsers.contains(user)) {
                        TournamentSettings.joinedUsers.add(user);

                        Message message = event.getChannel().getMessageById(messageId).complete();
                        message.editMessage(TournamentSettings.joinMessageEB.setDescription("**Starting: " + TournamentSettings.start_time + "\nPlayers: " + TournamentSettings.joinedUsers.size() + " / " + TournamentSettings.tournamentSize + "**\nCreator: <@" + TournamentSettings.creatorId + ">\n\nReact to join the tournament.").build()).queue();

                        if (!user.hasPrivateChannel()) user.openPrivateChannel().complete();
                        ((UserImpl) user).getPrivateChannel().sendMessage(new EmbedBuilder()
                                .setColor(BotSettings.fnrColor)
                                .setTitle("FNR Tournament System")
                                .setDescription("**You successfully joined the tournament.**\nYou will receive further information before the tournament starts.")
                                .setFooter(BotSettings.getEmbedFooter(), BotSettings.queueFooterUrl)
                                .build()).queue();
                    }
                }
            }
        }
    }
}
