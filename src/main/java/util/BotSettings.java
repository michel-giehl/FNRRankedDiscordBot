package util;

import core.Main;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;

import java.awt.*;

public class BotSettings {
    public static final String token = Secret.token;

    public static String prefix = "!";
    public static String guildId = "577957081076989962";

    public static Color fnrColor = new Color(240, 136, 0);

    public static String queueFooterUrl = "https://cdn.discordapp.com/avatars/600797207423352832/d7258b5e94e5a2b571704c4976df2c9c.png";
    public static String getEmbedFooter() {
        return "Fortnite Ranked Tournaments \u2022 Made by Beatz";
    }

    public static String[] ownerperms = {"462263905842888714", "171614473797238784"};

    public static Guild guild = Main.getJDA().getGuildById(guildId);

    public static Emote joinTourneyEmote = guild.getEmoteById("602235946263379989");
    public static Emote fnrEmote = guild.getEmoteById("602235946263379989");
}
