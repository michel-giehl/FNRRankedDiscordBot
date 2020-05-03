package com.fnranked.ranked.discord.messages;

import com.fnranked.ranked.data.MatchVote;
import com.fnranked.ranked.discord.util.JDAContainer;
import com.fnranked.ranked.jpa.entities.MatchTemp;
import com.fnranked.ranked.jpa.entities.Team;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class MatchMessages {

    @Autowired
    JDAContainer jdaContainer;

    @Value("${emote.error}")
    long errorEmoteId;
    @Value("${emote.loading}")
    long loadingEmoteId;
    @Value("${emote.success}")
    long successEmoteId;

    public MessageEmbed getMatchInformationEmbed(MatchTemp matchTemp) {
        EmbedBuilder embed = new EmbedBuilder();
        //ex EU Boxfight 1v1 Match
        String title = String.format("**%s %s Match**", matchTemp.getRegion(), matchTemp.getMatchType());
        embed.setTitle(title);
        embed.setDescription(String.format("**Have fun and good luck! Remember: First to win %d rounds takes the W**", matchTemp.getMatchType().getRequiredRoundsToWin()));
        embed.addField("**Map Code**", String.format("```glsl\n%s```", matchTemp.getMap().getMapCode()), false);
        return embed.build();
    }


    public MessageEmbed getVoteMessage(MatchTemp matchTemp) {
        EmbedBuilder winnerSelect = new EmbedBuilder();
        winnerSelect.setColor(Color.magenta);
        winnerSelect.setTitle("**Please select the winner**");
        String team1Status = getVoteStatus(matchTemp.getTeamA(), matchTemp.getTeamAVote());
        String team2Status = getVoteStatus(matchTemp.getTeamB(), matchTemp.getTeamBVote());
        String team1Emote = encodeEmote(errorEmoteId);
        String team2Emote = encodeEmote(errorEmoteId);
        if(!isVoteError(matchTemp.getTeamAVote(), matchTemp.getTeamBVote())) {
            team1Emote = getVoteEmote(matchTemp.getTeamAVote());
            team2Emote = getVoteEmote(matchTemp.getTeamBVote());
        }
        winnerSelect.setDescription(
                ":trophy: if you **won**"
                        + "\n:skull_crossbones: If you **lost**"
                        + "\n:flag_white: to cancel the match"
                        + "\n\n:telephone: if you want to call staff. Abusing this feature will get you banned!"
                        + "\n:warning: Selecting the wrong winner on purpose will get you banned!"
                        + "\n\n**Vote Status**"
                        + "\n" + team1Emote + " **Team 1** " + team1Status
                        + "\n" + team2Emote + " **Team 2** " + team2Status
        );
        return winnerSelect.build();
    }

    private String getVoteStatus(Team team, MatchVote teamVote) {
        switch (teamVote) {
            case CANCEL:
                return "**Canceling the match**";
            case TEAM_A:
                return "**Team A**";
            case TEAM_B:
                return "**Team B**";
            default:
                return "*Vote pending*";
        }
    }

    private String encodeEmote(long id) {
        var emote = jdaContainer.getJda().getEmoteById(id);
        if(emote == null) {
            return "";
        }
        return emote.getAsMention();
    }

    private String getVoteEmote(MatchVote teamVote) {
        return teamVote == null ? encodeEmote(loadingEmoteId) : encodeEmote(successEmoteId);
    }

    private boolean isVoteError(MatchVote teamAVote, MatchVote teamBVote) {
        return (teamAVote != teamBVote) && teamAVote != MatchVote.PENDING;
    }
}
