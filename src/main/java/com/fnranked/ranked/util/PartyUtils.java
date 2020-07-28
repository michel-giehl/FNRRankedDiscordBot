package com.fnranked.ranked.util;

import com.fnranked.ranked.jpa.entities.Party;
import com.fnranked.ranked.jpa.entities.PartyInvite;
import com.fnranked.ranked.jpa.entities.Player;
import com.fnranked.ranked.jpa.repo.PartyInviteRepository;
import com.fnranked.ranked.jpa.repo.PartyRepository;
import com.fnranked.ranked.messages.MessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class PartyUtils {

    @Autowired
    JDAContainer jdaContainer;

    @Autowired
    MessageUtils messageUtils;

    @Autowired
    PartyRepository partyRepository;

    @Autowired
    PartyInviteRepository partyInviteRepository;


    public void displayPartyForPlayer(Player player) {
        Optional<Party> partyOptional = partyRepository.findByPlayerListContaining(player);

        if (partyOptional.isPresent()) {
            Optional<Party> partyWithInvite = partyRepository.findById(partyOptional.get().getId());
            partyWithInvite.ifPresent(party -> messageUtils.sendMessageEmbed(player.getId(), displayParty(party)));
        } else {
            messageUtils.sendErrorMessage(player.getId(), "You are not currently in a party.");
        }
    }


    public MessageEmbed displayParty(Party party) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Your Party");
        embedBuilder.setColor(messageUtils.COLOR_FNRANKED);
        StringBuilder descriptionBuilder = new StringBuilder();
        JDA jda = jdaContainer.getJda();
        descriptionBuilder.append("Captain:\n");
        descriptionBuilder.append(Objects.requireNonNull(jda.getUserById(party.getCaptain().getId())).getAsTag());
        descriptionBuilder.append("\n\nPlayers");
        descriptionBuilder.append(String.format(" (%s):%n", party.getPlayerList().size()));

        for (Player partyMember : party.getPlayerList()) {
            descriptionBuilder.append(Objects.requireNonNull(jda.getUserById(partyMember.getId())).getAsTag());
            descriptionBuilder.append("\n");
        }
        List<PartyInvite> partyInvites = partyInviteRepository.findPartyInvitesByParty(party);
        if (!partyInvites.isEmpty()) {
            descriptionBuilder.append("\nActive Invites:\n");
            for (PartyInvite invite : partyInvites) {
                descriptionBuilder.append(Objects.requireNonNull(jda.getUserById(invite.getInviteeId())).getAsTag());
                descriptionBuilder.append("\n");
            }
        }

        embedBuilder.setDescription(descriptionBuilder.toString());
        embedBuilder.setFooter(messageUtils.fnrankedFooter);
        return embedBuilder.build();
    }
}
