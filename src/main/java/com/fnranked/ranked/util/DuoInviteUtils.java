package com.fnranked.ranked.util;

import com.fnranked.ranked.api.entities.Result;
import com.fnranked.ranked.jpa.entities.DuoInvite;
import com.fnranked.ranked.jpa.repo.DuoInviteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class DuoInviteUtils {

    @Autowired
    DuoInviteRepository duoInviteRepository;
    @Autowired
    TeamUtils teamUtils;

    private final long ttl = 120_000L;

    @Transactional
    public boolean canCreateDuoInvite(long inviterId, long inviteeId) {
        return duoInviteRepository.findByInviteeId(inviteeId).isEmpty() &&
                duoInviteRepository.findByInviterId(inviterId).isEmpty() &&
                duoInviteRepository.findByInviterId(inviteeId).isEmpty() &&
                duoInviteRepository.findByInviteeId(inviterId).isEmpty();
    }

    @Transactional
    public void createInvite(long inviterId, long inviteeId, Result<Boolean> accepted) {
        DuoInvite duoInvite = new DuoInvite(inviterId, inviteeId);
        DuoInvite finalInvite = duoInviteRepository.save(duoInvite);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(duoInviteRepository.existsById(finalInvite.getId())) {
                    declineInvite(inviteeId);
                    accepted.invoke(false);
                } else {
                    accepted.invoke(true);
                }
            }
        }, ttl);
    }

    @Transactional
    public void declineInvite(long inviteeId) {
        duoInviteRepository.deleteByInviteeId(inviteeId);
    }

    @Transactional
    public void acceptInvite(long inviteeId) {
        duoInviteRepository.findByInviteeId(inviteeId).ifPresent(duoInvite -> teamUtils.createTeam(duoInvite.getInviterId(), duoInvite.getInviteeId()));
    }
}
