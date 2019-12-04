package commands;

import CommandSystem.Command;
import PermissionSystem.Permissions;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.BotSettings;
import util.TournamentSettings;

import java.util.ArrayList;
import java.util.List;

public class WinnerCommand implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(Permissions.getPermissionLevel(event.getMember()) >= 3) {
            TextChannel textChannel = event.getTextChannel();
            if(textChannel.getParent().getName().toLowerCase().startsWith(TournamentSettings.categoryNameStart)) {
                if(SendVerifyStillThereCommand.matches.containsKey(textChannel)) {
                    if(event.getMessage().getMentionedMembers().size() == 1) {
                        User winner = event.getMessage().getMentionedMembers().get(0).getUser();
                        User loser = null;
                        if(winner == SendVerifyStillThereCommand.matches.get(textChannel).getKey())
                            loser = SendVerifyStillThereCommand.matches.get(textChannel).getValue();
                        else if(winner == SendVerifyStillThereCommand.matches.get(textChannel).getValue())
                            loser = SendVerifyStillThereCommand.matches.get(textChannel).getKey();
                        else
                            System.out.println("NO LOSER FOUND! ERROR!");

                        int currentRound = TournamentSettings.userRounds.get(winner);

                        // remove loser from tournament, put winner into next match
                        TournamentSettings.joinedUsers.remove(loser);
                        TournamentSettings.userRounds.remove(loser);
                        TournamentSettings.rounds.get(currentRound).remove(loser);

                        TournamentSettings.userRounds.replace(winner, (currentRound + 1));
                        TournamentSettings.rounds.get(currentRound).remove(winner);
                        TournamentSettings.rounds.get(currentRound + 1).add(winner);
                        TournamentSettings.waitingForNextMatch.get(currentRound + 1).add(winner);

                        if(TournamentSettings.waitingForNextMatch.get(currentRound + 1).size() > 1) {
                            // put those people together into one match
                            User player1 = TournamentSettings.waitingForNextMatch.get(currentRound + 1).get(0);
                            User player2 = TournamentSettings.waitingForNextMatch.get(currentRound + 1).get(1);

                            Guild guild = event.getGuild();

                            Category tournamentChannelCategory1 = guild.getCategoryById(TournamentSettings.tournamentChannelCategory1);
                            Category tournamentChannelCategory2 = guild.getCategoryById(TournamentSettings.tournamentChannelCategory2);

                            List<Permission> perms = new ArrayList<>();
                            perms.add(Permission.MESSAGE_READ);
                            perms.add(Permission.MESSAGE_WRITE);


                            Category category = null;
                            int category1size = tournamentChannelCategory1.getTextChannels().size();
                            int category2size = tournamentChannelCategory2.getTextChannels().size();

                            if(category1size < 50) {
                                // use category 1
                                category = tournamentChannelCategory1;
                            } else if(category2size < 50) {
                                // use category 2
                                category = tournamentChannelCategory2;
                            } else {
                                System.out.println("THERE ARE ALREADY 100 MATCH CHANNELS, CATEGORIES ARE FULL");
                                // todo WORK ON THIS HERE
                            }

                                String matchnumber = opponentsRoundOne.get(i).getValue();
                                TextChannel channel = (TextChannel) category.createTextChannel("match-" + matchnumber).complete();

                                matches.put(channel, opponentsRoundOne.get(i).getKey());

                                User player1 = opponentsRoundOne.get(i).getKey().getKey();
                                User player2 = opponentsRoundOne.get(i).getKey().getValue();
                                channel.createPermissionOverride(guild.getMember(player1)).setAllow(perms).queue();
                                channel.createPermissionOverride(guild.getMember(player2)).setAllow(perms).queue();

                                String map_code = TournamentSettings.map_code;
                                EmbedBuilder eb = new EmbedBuilder()
                                        .setColor(BotSettings.fnrColor)
                                        .setTitle("TOURNAMENT | Match " + matchnumber)
                                        .setDescription("```asciidoc\n" +
                                                "= MATCH INFORMATION =\n" +
                                                "Match ID      :: #" + matchnumber + "\n" +
                                                "Match region  :: " + TournamentSettings.region + "```")
                                        .addField("**MAP CODE**", "Use the following map: ```glsl\n" + map_code + "```\n" +
                                                ":information_source: <#" + TournamentSettings.rulesTextChannelId + ">\n" +
                                                ":information_source: Use **!VC** to create a voice channel for your match\n" +
                                                ":warning: @ your opponent before contacting staff.\n" +
                                                ":information_source: If your opponent is afk/not responding for **at least 5 minutes**"
                                                + "please tag staff in this channel.\n", false)
                                        .setFooter(BotSettings.getEmbedFooter(), BotSettings.queueFooterUrl);

                                channel.sendMessage(new MessageBuilder(eb).append(player1.getAsMention() + " | " + player2.getAsMention()).build()).queue();

                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        System.out.println("[INFO] Der winner-Command wurde ausgeführt!");
    }

    @Override
    public String help() {
        return null;
    }
}
