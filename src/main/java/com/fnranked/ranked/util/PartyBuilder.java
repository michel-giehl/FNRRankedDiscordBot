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
            Optional<Party> partyOptional = partyRepository.findByPlayerListContaining(playerOptional.get().getId());
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

    public void attemptKick(long kickerId, long toKickId) {
        Party party = getPartyWithLeader(kickerId);
        if (party != null) {
            boolean success = false;
            Optional<Player> playerOptional = playerRepository.findById(toKickId);
            if (playerOptional.isPresent() && party.getPlayerList().contains(playerOptional.get()) && party.getId() != toKickId) {
                party.removePlayer(playerOptional.get());
                partyRepository.save(party);
                success = true;
            } else {
                messageUtils.sendErrorMessage(kickerId, "You are not able to kick this user as they are not in your party.");
            }
            if (success) {
                messageUtils.sendSuccessMessage(kickerId, "Player removed from your party!");
            } else {
                messageUtils.sendErrorMessage(kickerId, "<:PepeHands:712672036178362418> You can't kick this user.");
            }
        }
    }

    public boolean isUserInviteable(Long user, Party party) {
        Optional<Player> playerOptional = playerRepository.findById(user);
        if (playerOptional.isPresent()) {
            if (!party.getPlayerList().contains(playerOptional.get())) {
                return playerOptional.get().isDuoInvitesEnabled();
            }
        }
        logger.error(String.format("User <@%s> was attempted to be invited to a party, but was not found as a registered player.", user));
        return false;
    }

    public void attemptPromote(long promoterId, long toPromoteId) {
        Party party = getPartyWithLeader(promoterId);
        if (party != null) {
            Optional<Player> playerOptional = playerRepository.findById(toPromoteId);
            if (playerOptional.isPresent() && party.getPlayerList().contains(playerOptional.get()) && party.getId() == promoterId) {
                partyRepository.delete(party);
                Party newParty = new Party(playerOptional.get());
                party.getPlayerList().forEach(newParty::addPlayer);
                messageUtils.sendSuccessMessage(promoterId, "Player promoted to party leader!");
            } else {
                messageUtils.sendErrorMessage(promoterId, "<:PepeHands:712672036178362418> You are not able to promote this user as they are not in your party.");
            }
        }
    }

    private Party getPartyWithLeader(long playerId) {
        Optional<Party> partyOptional = partyRepository.findById(playerId);
        if (partyOptional.isEmpty()) {
            messageUtils.sendErrorMessage(playerId, "You are not the leader of a party.");
            return null;
        }
        return partyOptional.get();
    }
}
