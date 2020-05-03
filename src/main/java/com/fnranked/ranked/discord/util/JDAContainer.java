package com.fnranked.ranked.discord.util;

import net.dv8tion.jda.api.JDA;
import org.springframework.stereotype.Component;

@Component
public class JDAContainer {

    JDA jda;

    public JDA getJda() {
        return jda;
    }

    public void setJda(JDA jda) {
        this.jda = jda;
    }
}
