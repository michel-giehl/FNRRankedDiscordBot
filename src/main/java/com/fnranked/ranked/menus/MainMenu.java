package com.fnranked.ranked.menus;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.util.Collection;
import java.util.Map;

@Component
public class MainMenu implements Menu {

    @Value("${fnranked.embed.footer}")
    String embedFooter;

    String name = "Main Menu";

    EmbedBuilder mainMenuEmbedBuilder;

    Map<Emote, Menu> emoteMap;

    @PostConstruct
    public void initMenu() {
        mainMenuEmbedBuilder = new EmbedBuilder();
        mainMenuEmbedBuilder.setFooter(embedFooter);
        mainMenuEmbedBuilder.setTitle(name);
        mainMenuEmbedBuilder.setColor(Color.BLACK);
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public MessageEmbed getEmbed(User user) {
        return null;
    }

    @Override
    public Collection<Emote> getOptionEmotes(User user) {
        return emoteMap.keySet();
    }
}
