package com.fnranked.ranked.menus;

import com.fnranked.ranked.data.Region;
import com.fnranked.ranked.discord.util.JDAContainer;
import com.fnranked.ranked.jpa.entities.Queue;
import com.fnranked.ranked.jpa.repo.QueueRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.entities.EmoteImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueueMenu implements Menu {

    @Value("${fnranked.embed.footer}")
    String embedFooter;

    @Autowired
    QueueRepository queueRepository;

    @Autowired
    JDAContainer jdaContainer;

    String name = "Queue Menu";

    EmbedBuilder queueMenuEmbedBuilder;

    @PostConstruct
    public void init() {
        queueMenuEmbedBuilder = new EmbedBuilder();
        queueMenuEmbedBuilder.setFooter(embedFooter);
        queueMenuEmbedBuilder.setTitle(name);
        queueMenuEmbedBuilder.setColor(Color.ORANGE);
    }

    public String generateMenuText(Region region) {
        List<Queue> queueList = queueRepository.findAllByRegionIs(region);
        if (queueList.isEmpty()) {
            return "No queues are currently enabled for your region.";
        }
        StringBuilder menuTextBuilder = new StringBuilder(String.format("Available Queues in %s Region: %n", region.name()));
        for (Queue queue : queueList) {
            menuTextBuilder.append(String.format(":%s: ", queue.getMatchType().getDisplayEmote()));
            menuTextBuilder.append(queue.getMatchType().getName());
            menuTextBuilder.append(String.format(" (%d in queue, %s estimated queue time)", queue.getQueueing().size(), queue.getEstimatedQueueTime()));
        }
        return menuTextBuilder.toString();
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public MessageEmbed getEmbed(User user) {
        return getEmbed(Region.NA_EAST);
    }

    public MessageEmbed getEmbed(Region region) {
        queueMenuEmbedBuilder.setDescription(generateMenuText(region));
        return queueMenuEmbedBuilder.build();
    }

    @Override
    public Collection<Emote> getOptionEmotes(User user) {
        //TODO get region from user's current preference

        List<Queue> queueList = queueRepository.findAllByRegionIs(Region.NA_EAST);
        Collection<Emote> emotes = new ArrayList<>();
        for (Queue queue : queueList) {

           // emotes.add(new EmoteImpl(queue.getMatchType().getDisplayEmote(), (JDAImpl) jdaContainer.getJda()));

        }
        return emotes;
    }
}
