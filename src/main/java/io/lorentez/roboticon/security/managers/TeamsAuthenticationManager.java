package io.lorentez.roboticon.security.managers;

import io.lorentez.roboticon.commands.StatusCredentials;
import io.lorentez.roboticon.model.UserTeam;
import io.lorentez.roboticon.model.UserTeamStatus;
import io.lorentez.roboticon.repositories.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;

@Slf4j
@Component
public class TeamsAuthenticationManager extends BasicAuthenticationManager {

    public TeamsAuthenticationManager(TeamRepository teamRepository) {
        super(teamRepository);
    }

    public boolean canUserUpdateTeamData(Authentication authentication, Long teamId){
        String email = (String) authentication.getPrincipal();
        if (email == null){
            email = "";
        }
        log.info("User: " + email + " attempted to change details of team with id: " + teamId.toString());
        return isUserAdminOrOwner(email, teamId);
    }

    public boolean userCanInvite(Authentication authentication, Long teamId){
        String email = (String) authentication.getPrincipal();
        if (email == null){
            email = "";
        }
        log.info("User: " + email + " attempted to invite user to team with id: " + teamId.toString());
        return isUserAdminOrOwner(email, teamId);
    }

    public boolean userCanChangeStatus(Authentication authentication,
                                       Long teamId,
                                       StatusCredentials credentials){
        UserTeamStatus status = UserTeamStatus.valueOf(credentials.getStatus().toUpperCase(Locale.ROOT));
        String email = (String) authentication.getPrincipal();
        if (email == null){
            email = "";
        }
        log.info("User: " + email + " attempted to change status of user: " + credentials.getEmail() + " in team with ID: " + teamId.toString());
        Optional<UserTeam> requesterUserTeamOptional = teamRepository.findActualMembersByTeamId(teamId, email);
        if (requesterUserTeamOptional.isPresent()){
            UserTeam requesterUserTeam = requesterUserTeamOptional.get();
            Optional<UserTeam> updatedUserTeamOptional = teamRepository
                    .findActualMembersByTeamId(teamId, credentials.getEmail());
            if(updatedUserTeamOptional.isEmpty()){
                return false;
            }
            UserTeam updatedUserTeam = updatedUserTeamOptional.get();
            if (email.equals(credentials.getEmail())
                    && requesterUserTeam.getStatus().equals(UserTeamStatus.INVITED)
                    && status.equals(UserTeamStatus.MEMBER)){
                return true;
            }
            else if (!requesterUserTeam.getStatus().equals(UserTeamStatus.INVITED)
                    && (!requesterUserTeam.getStatus().equals(UserTeamStatus.REQUEST_JOIN))) {
                boolean statusMemberOrAdmin = status.equals(UserTeamStatus.MEMBER) || status.equals(UserTeamStatus.ADMIN);
                if (requesterUserTeam.getStatus().equals(UserTeamStatus.OWNER)
                        && statusMemberOrAdmin){
                    return true;
                }
                else return requesterUserTeam.getStatus().equals(UserTeamStatus.ADMIN)
                        && (!updatedUserTeam.getStatus().equals(UserTeamStatus.OWNER))
                        && statusMemberOrAdmin;
            }
        }
        return false;
    }

}
