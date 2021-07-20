package io.lorentez.roboticon.security.managers;

import io.lorentez.roboticon.model.RobotTeam;
import io.lorentez.roboticon.model.UserTeam;
import io.lorentez.roboticon.model.UserTeamStatus;
import io.lorentez.roboticon.repositories.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import java.util.Optional;

@Slf4j
public abstract class BasicAuthenticationManager {

    public BasicAuthenticationManager(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    protected final TeamRepository teamRepository;

    public boolean isUserMemberOfTeam(String email, Long teamId){
        Optional<UserTeam> userTeamOptional = teamRepository.findActualMembersByTeamId(teamId, email);
        return userTeamOptional.map(userTeam -> userTeam.getStatus().equals(UserTeamStatus.OWNER)
                || userTeam.getStatus().equals(UserTeamStatus.ADMIN)
                || userTeam.getStatus().equals(UserTeamStatus.MEMBER)).orElse(Boolean.FALSE);
    }

    public boolean isUserAdminOrOwner(String email, Long teamId){
        Optional<UserTeam> userTeamOptional = teamRepository.findActualMembersByTeamId(teamId, email);
        return userTeamOptional.map(userTeam -> userTeam.getStatus().equals(UserTeamStatus.OWNER)
                || userTeam.getStatus().equals(UserTeamStatus.ADMIN)).orElse(Boolean.FALSE);
    }

    public boolean isUserOwnerOfRobotTeam(String email, Optional<RobotTeam> robotTeamOptional) {
        if(robotTeamOptional.isPresent()){
            RobotTeam robotTeam = robotTeamOptional.get();
            Optional<UserTeam> userTeamOptional = teamRepository
                    .findActualMembersByTeamId(robotTeam.getTeam().getId(), email);
            return userTeamOptional.map(userTeam -> userTeam.getStatus().equals(UserTeamStatus.OWNER))
                    .orElse(Boolean.FALSE);
        }
        return false;
    }


    public boolean isUserInTeam(Authentication authentication, Long teamId){
        String email = (String) authentication.getPrincipal();
        if (email == null){
            email = "";
        }
        log.info("User: " + email + " attempted to get information of team: " + teamId.toString());
        Optional<UserTeam> userTeamOptional = teamRepository.findActualMembersByTeamId(teamId, email);
        return userTeamOptional.filter(userTeam -> !userTeam.getStatus().equals(UserTeamStatus.INVITED)
                && !userTeam.getStatus().equals(UserTeamStatus.REQUEST_JOIN)).isPresent();
    }
}
