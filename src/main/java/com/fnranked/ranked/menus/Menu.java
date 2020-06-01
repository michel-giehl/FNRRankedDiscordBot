package com.fnranked.ranked.menus;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.util.Collection;

public interface Menu {

    String getDisplayName();

    MessageEmbed getEmbed(User user);

    Collection<Emote> getOptionEmotes(User user);
}
