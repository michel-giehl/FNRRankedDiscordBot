package com.fnranked.ranked.util;

import com.fnranked.ranked.jpa.entities.Party;
import com.fnranked.ranked.jpa.entities.Player;
import com.fnranked.ranked.jpa.repo.PartyRepository;
import com.fnranked.ranked.jpa.repo.PlayerRepository;
import com.fnranked.ranked.messages.MessageUtils;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PartyBuilder {

    @Autowired
    PartyRepository partyRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    MessageUtils messageUtils;

    Logger logger = LoggerFactory.getLogger(PartyBuilder.class);

    public Party getOrCreatePartyWithPlayer(User captain) {
        Optional<Player> playerOptional = playerRepository.findById(captain.getIdLong());
        if (playerOptional.isPresent()) {
            Optional<Party> partyOptional = partyRepository.findByPlayerListContaining(playerOptional.get());
            if (partyOptional.isPresent()) {
                return partyOptional.get();
            } else {
                Party party = new Party(playerOptional.get());
                partyRepository.save(party);
                return party;
            }
        }
        logger.error(String.format("User <@%s> tried to create a party, but was not found as a registered player.", captain.getIdLong()));
        messageUtils.sendErrorMessage(captain.getIdLong(), "Unable to create party due to not being found as a registered player.");
        return null;
    }

    public boolean isUserInviteable(Long user) {
        Optional<Player> playerOptional = playerRepository.findById(user);
        if (playerOptional.isPresent()) {
            return playerOptional.get().isDuoInvitesEnabled();
        } else {
            logger.error(String.format("User <@%s> was attempted to be invited to a party, but was not found as a registered player.", user));
            return false;
        }
    }

}
