package io.lorentez.roboticon.model.security.managers;

import io.lorentez.roboticon.commands.StatusCredentials;
import io.lorentez.roboticon.model.UserTeam;
import io.lorentez.roboticon.model.UserTeamStatus;
import io.lorentez.roboticon.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class TeamsAuthenticationManager {

    private final TeamRepository teamRepository;

    public boolean userCanInvite(Authentication authentication, Long teamId){
        String email = (String) authentication.getPrincipal();
        log.info("User: " + email + " attempted to invite user to team with id: " + teamId.toString());
        Optional<UserTeam> userTeamOptional = teamRepository.findActualMembersByTeamId(teamId, email);
        return userTeamOptional.map(userTeam -> userTeam.getStatus().equals(UserTeamStatus.ADMIN)
                || userTeam.getStatus().equals(UserTeamStatus.OWNER))
                .orElse(Boolean.FALSE);
    }

    public boolean userCanChangeStatus(Authentication authentication,
                                       Long teamId,
                                       String userEmail,
                                       String statusString){
        String email = (String) authentication.getPrincipal();
        log.info("User: " + email + " attempted to change status of user: " + userEmail + " in team with ID: " + teamId.toString());
        Optional<UserTeam> userTeamOptional = teamRepository.findActualMembersByTeamId(teamId, email);
        if (userTeamOptional.isPresent()){
            UserTeam requestUserTeam = userTeamOptional.get();
            Optional<UserTeam> userUserTeam = teamRepository
                    .findActualMembersByTeamId(teamId, userEmail);
            if(userUserTeam.isEmpty()){
                return false;
            }
            else return (requestUserTeam.getStatus() == UserTeamStatus.OWNER) ||
                    (requestUserTeam.getStatus() == UserTeamStatus.ADMIN
                            && userUserTeam.get().getStatus() != UserTeamStatus.OWNER) ||
                    (requestUserTeam.getStatus() == UserTeamStatus.INVITED
                            && userUserTeam.get().getUser().getEmail().equals(userEmail));
        }
        return false;
    }
}
