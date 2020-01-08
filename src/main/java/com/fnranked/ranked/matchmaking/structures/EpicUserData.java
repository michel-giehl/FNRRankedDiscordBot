package com.fnranked.ranked.matchmaking.structures;

import org.springframework.data.annotation.Id;

import java.util.Optional;

public interface EpicUserData {

    @Id
    String epicId();

    String displayName();

    Optional<String> getSessionId();

    Optional<String> partyId();

    Optional<String> getResponsibleBotId();

    void setSessionId(String sessionId);

    void setPartyId(String partyId);

    void setResponsibleBot(String botId);
}
