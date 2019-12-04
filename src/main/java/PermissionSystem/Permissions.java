package PermissionSystem;

import core.Main;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import util.BotSettings;

import java.util.Arrays;
import java.util.List;

public class Permissions {
    public static int getPermissionLevel(Member member) {
        Guild guild = Main.getJDA().getGuildById(BotSettings.guildId);
        String userId = member.getUser().getId();

        if(Arrays.stream(BotSettings.ownerperms).parallel().anyMatch(userId ::contains))
            return 4;

        List<Role> roles = member.getRoles();
        Role staff = guild.getRoleById("601061193993682954");
        if(roles.contains(staff))
            return 3;
        else
            return 0;
    }
}
