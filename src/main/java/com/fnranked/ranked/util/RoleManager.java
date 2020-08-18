package com.fnranked.ranked.util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.TreeMap;

@Component
public class RoleManager {

    @Autowired
    JDAContainer jdaContainer;

    @Value("${roles.bronze}")
    long bronzeId;
    @Value("${roles.silver}")
    long silverId;
    @Value("${roles.gold}")
    long goldId;
    @Value("${roles.platinum}")
    long platinumId;
    @Value("${roles.diamond}")
    long diamondId;
    @Value("${roles.champion}")
    long championId;
    @Value("${roles.grand_champion}")
    long grandChampionId;
    @Value("${roles.divine}")
    long divineId;

    private Guild guild;
    private TreeMap<Double, Long> roles;

    public void init() {
        this.roles = new TreeMap<>(Comparator.reverseOrder());
        roles.put(0.0D, bronzeId);
        roles.put(100.0D, silverId);
        roles.put(300.0D, goldId);
        roles.put(500.0D, platinumId);
        roles.put(800.0D, diamondId);
        roles.put(1000.0D, championId);
        roles.put(1200.0D, grandChampionId);
        roles.put(1500.0D, divineId);
    }

    public boolean updateUser(long userId) {
        final double elo = 0.0D;
        Member member = guild.getMemberById(userId);
        if(member == null) return false;
        long roleId = roles.entrySet().stream().filter(e -> e.getKey() >= elo).findFirst().get().getValue();
        Role role = guild.getRoleById(roleId);
        if(role == null) return false;
        if(!member.getRoles().contains(role)) {
            for(Role memberRole : member.getRoles()) {
                if(roles.containsValue(memberRole.getIdLong())) {
                    guild.removeRoleFromMember(member, role).queue();
                }
            }
            guild.addRoleToMember(member, role).queue();
            return true;
        }
        return false;
    }
}
