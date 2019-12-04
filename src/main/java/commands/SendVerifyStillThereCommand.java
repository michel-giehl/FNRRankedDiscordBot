package commands;

import CommandSystem.Command;
import PermissionSystem.Permissions;
import javafx.util.Pair;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.impl.UserImpl;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.BotSettings;
import util.TournamentSettings;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SendVerifyStillThereCommand implements Command {

    public static HashMap<User, String> verifyMessageIds = new HashMap<>();
    public static HashMap<TextChannel, Pair<User, User>> matches = new HashMap<>();
    public static ArrayList<Pair<Pair<User, User>, String>> opponentsRoundOne = new ArrayList<>();

    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        Member member = event.getMember();
        if(Permissions.getPermissionLevel(member) >= 4) {
            TournamentSettings.isJoinable = false;
            TournamentSettings.joinMessage.editMessage(new EmbedBuilder()
                    .setColor(Color.red)
                    .setTitle(TournamentSettings.tournamentName)
                    .setDescription("This tournament started already.")
                    .setFooter(BotSettings.getEmbedFooter(), BotSettings.queueFooterUrl)
                    .build()).queue();
            TournamentSettings.joinMessage.clearReactions().queue();

            Guild guild = BotSettings.guild;
            for(int i = 0; i < TournamentSettings.joinedUsers.size(); i++) {
                User user = TournamentSettings.joinedUsers.get(i);

                if(guild.getMember(user) != null) {
                    if(!user.hasPrivateChannel()) user.openPrivateChannel().complete();
                    Message message = ((UserImpl) user).getPrivateChannel().sendMessage(new EmbedBuilder()
                            .setColor(BotSettings.fnrColor)
                            .setTitle("Tournament Verify System")
                            .setDescription("Hello! The tournament starts soon.\nAre you there? (Please react with the emote if you are there and able to play the tournament)")
                            .setFooter(BotSettings.getEmbedFooter(), BotSettings.queueFooterUrl)
                            .build()).complete();

                    message.addReaction(BotSettings.fnrEmote).queue();

                    verifyMessageIds.put(user, message.getId());
                }
            }

            TextChannel textChannel = event.getTextChannel();
            textChannel.sendMessage("Successfully DMed everyone who joined the tournament").queue();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    int removed = 0;

                    for(int i = 0; i < TournamentSettings.joinedUsers.size(); i++) {
                        User user = TournamentSettings.joinedUsers.get(i);
                        if(verifyMessageIds.containsKey(user)) {
                            // time over, user probably not there
                            TournamentSettings.joinedUsers.remove(user);
                            verifyMessageIds.remove(user);

                            removed++;

                            if(!user.hasPrivateChannel()) user.openPrivateChannel().complete();
                            ((UserImpl) user).getPrivateChannel().sendMessage("You got removed from the list of the players participating in the tournament because you didn't react to the message.").queue();
                        }
                    }

                    textChannel.sendMessage(removed + " users got removed from the list of players participating in the tournament. There are " + TournamentSettings.joinedUsers.size() + " players left.").queue();


                    // start tournament
                    int size = TournamentSettings.tournamentSize;
                    if(size %2 == 0) {
                        //HashMap<Pair<User, User>, String> opponentsRoundOne = new HashMap<>();
                        Collections.shuffle(TournamentSettings.joinedUsers);

                        for(int i = 0; i < TournamentSettings.joinedUsers.size(); i++) {
                            if(i %2 == 0 || i == 0) {
                                User player1 = TournamentSettings.joinedUsers.get(i);
                                User player2 = TournamentSettings.joinedUsers.get(i + 1);

                                opponentsRoundOne.add(new Pair<>(new Pair<>(player1, player2), String.valueOf((i + 1))));
                            }
                        }

                        Category tournamentChannelCategory1 = guild.getCategoryById(TournamentSettings.tournamentChannelCategory1);
                        Category tournamentChannelCategory2 = guild.getCategoryById(TournamentSettings.tournamentChannelCategory2);

                        List<Permission> perms = new ArrayList<>();
                        perms.add(Permission.MESSAGE_READ);
                        perms.add(Permission.MESSAGE_WRITE);

                        for(int i = 0; i < opponentsRoundOne.size(); i++) {
                            Category category = null;
                            if(i < 50) {
                                // use category 1
                                category = tournamentChannelCategory1;
                            } else if(i > 49 && i < 98) {
                                // use category 2
                                category = tournamentChannelCategory2;
                            } else if(i > 99) {
                                System.out.println("THERE ARE ALREADY 100 MATCH CHANNELS, CATEGORIES ARE FULL");
                                break;
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

                    } else {
                        User lastuser = TournamentSettings.joinedUsers.get(TournamentSettings.joinedUsers.size() - 1);
                        if(!lastuser.hasPrivateChannel()) lastuser.openPrivateChannel().queue(channel -> {
                            channel.sendMessage("You got removed from the participating players because the number of participating players was odd and you were the last one who joined.").queue();
                        });
                        TournamentSettings.joinedUsers.remove(TournamentSettings.joinedUsers.size() - 1);
                    }
                }
            }, 20 * 60000);
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        System.out.println("[INFO] Der verifyonlinestate-Command wurde ausgeführt!");
    }

    @Override
    public String help() {
        return null;
    }
}
