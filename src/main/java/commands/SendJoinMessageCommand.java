package commands;

import CommandSystem.Command;
import PermissionSystem.Permissions;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.BotSettings;
import util.TournamentSettings;

public class SendJoinMessageCommand implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        Member member = event.getMember();
        if(Permissions.getPermissionLevel(member) >= 4) {
            event.getMessage().delete().queue();

            // !newtourney NAME MAX_PLAYERS START_TIME

            int players = Integer.parseInt(args[1]);
            String tournamentName = args[0];
            String start_time;
            if(args.length == 3) {
                start_time = args[2];
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                for(int i = 2; i < args.length; i++) {
                    stringBuilder.append(args[i] + " ");
                }

                start_time = stringBuilder.toString();
            }

            TournamentSettings.tournamentName = tournamentName;
            TournamentSettings.tournamentSize = players;
            TournamentSettings.start_time = start_time;
            TournamentSettings.creatorId = member.getUser().getId();

            String desc = "**Starting: " + start_time + "\nPlayers: 0 / " + TournamentSettings.tournamentSize + "**\nCreator: " + member.getAsMention() + "\n\nReact to join the tournament.";

            EmbedBuilder eb = new  EmbedBuilder()
                    .setColor(BotSettings.fnrColor)
                    .setTitle(tournamentName)
                    .setDescription(desc)
                    .setFooter(BotSettings.getEmbedFooter(), BotSettings.queueFooterUrl);

            TournamentSettings.joinMessageEB = eb;

            Message message = event.getTextChannel().sendMessage(eb.build()).complete();

            TournamentSettings.joinMessage = message;
            String messageId = message.getId();
            TournamentSettings.joinMessageId = messageId;

            message.addReaction(BotSettings.joinTourneyEmote).queue();
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        System.out.println("[INFO] Der sendjoinmessage-Command wurde ausgeführt!");
    }

    @Override
    public String help() {
        return null;
    }
}
