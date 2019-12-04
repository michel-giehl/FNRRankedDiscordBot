package util;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TournamentSettings {
    public static int tournamentSize = 100;
    public static List<User> joinedUsers = new ArrayList<>();
    public static Message joinMessage = null;
    public static String joinMessageId = null;
    public static String tournamentName = null;
    public static String start_time = null;
    public static String creatorId = null;
    public static EmbedBuilder joinMessageEB = null;
    public static boolean isJoinable = true;
    public static String tournamentChannelCategory1 = "648137966585118770";
    public static String tournamentChannelCategory2 = "648138819232727041";
    public static String region = null;
    public static String map_code = "select one";
    public static String rulesTextChannelId = "select one";
    public static String categoryNameStart = "tournament match";

    public static HashMap<Integer, ArrayList<User>> rounds = new HashMap<>();
    public static HashMap<User, Integer> userRounds = new HashMap<>();
    public static HashMap<Integer, ArrayList<User>> waitingForNextMatch = new HashMap<>();
}
