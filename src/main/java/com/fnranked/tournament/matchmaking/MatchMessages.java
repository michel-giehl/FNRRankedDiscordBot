package com.fnranked.tournament.matchmaking;

import com.fnranked.tournament.matchmaking.structures.Match;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class MatchMessages {

    public static MessageEmbed getEmbed(Match match) {
        return new EmbedBuilder().build();
    }
}