package com.fnranked.ranked.util;

import com.fnranked.ranked.api.entities.PermissionLevel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PermissionUtil {

    @Autowired
    JDAContainer jdaContainer;

    @Value("${fnranked.guild.id}")
    long guildId;
    @Value("${fnranked.roles.staff}")
    long staffRoleId;
    @Value("${fnranked.roles.management}")
    long managementRoleId;

    public boolean hasPermission(User user, PermissionLevel minPermissionsLevel) {
        Member m = jdaContainer.getJda().getGuildById(guildId).getMember(user);
        List<Long> memberRoles = m.getRoles().stream().map(Role::getIdLong).collect(Collectors.toList());
        switch (minPermissionsLevel) {
            case STAFF:
            case PREMIUM:
                return memberRoles.contains(staffRoleId) || memberRoles.contains(managementRoleId);
            case MANAGEMENT:
                return memberRoles.contains(managementRoleId);
        }
        return false;
    }
}
