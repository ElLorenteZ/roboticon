package io.lorentez.roboticon.model.security.managers;

import io.lorentez.roboticon.model.RobotTeam;
import io.lorentez.roboticon.model.RobotTeamStatus;
import io.lorentez.roboticon.model.UserTeam;
import io.lorentez.roboticon.model.UserTeamStatus;
import io.lorentez.roboticon.repositories.RobotRepository;
import io.lorentez.roboticon.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class RobotsAuthenticationManager {

    private final TeamRepository teamRepository;
    private final RobotRepository robotRepository;

    public boolean canUserEditRobot(Authentication authentication, Long robotId){
        String email = (String) authentication.getPrincipal();
        if (email == null){
            email = "";
        }
        log.info("User: " + email + " attempted to change details of robot: " + robotId.toString());
        Optional<RobotTeam> robotTeamOptional = robotRepository.getRobotByActualStatus(robotId, RobotTeamStatus.OWNED);
        if (robotTeamOptional.isPresent()){
            RobotTeam robotTeam = robotTeamOptional.get();
            Optional<UserTeam> userTeamOptional = teamRepository
                    .findActualMembersByTeamId(robotTeam.getTeam().getId(), email);
            return userTeamOptional.map(userTeam -> userTeam.getStatus().equals(UserTeamStatus.OWNER)
                    || userTeam.getStatus().equals(UserTeamStatus.ADMIN))
                    .orElse(Boolean.FALSE);
        }
        return false;
    }

    public boolean canUserTransferRobot(Authentication authentication, Long robotId) {
        String email = (String) authentication.getPrincipal();
        if (email == null){
            return false;
        }
        log.info("User: " + email + " attempted to transfer robot with id: " + robotId.toString());
        Optional<RobotTeam> robotTeamOptional = robotRepository.getRobotByActualStatus(robotId, RobotTeamStatus.OWNED);
        return isUserOwnerOfRobotTeam(email, robotTeamOptional);
    }

    public boolean canUserAcceptTransfer(Authentication authentication, Long robotId){
        String email = (String) authentication.getPrincipal();
        if (email == null){
            return false;
        }
        log.info("User with email: " + email
                + " attempted to accept transfer of robot: " + robotId.toString());
        Optional<RobotTeam> robotTeamOptional = robotRepository.getRobotByActualStatus(robotId, RobotTeamStatus.PENDING);
        return isUserOwnerOfRobotTeam(email, robotTeamOptional);
    }

    private boolean isUserOwnerOfRobotTeam(String email, Optional<RobotTeam> robotTeamOptional) {
        if(robotTeamOptional.isPresent()){
            RobotTeam robotTeam = robotTeamOptional.get();
            Optional<UserTeam> userTeamOptional = teamRepository
                    .findActualMembersByTeamId(robotTeam.getTeam().getId(), email);
            return userTeamOptional.map(userTeam -> userTeam.getStatus().equals(UserTeamStatus.OWNER))
                    .orElse(Boolean.FALSE);
        }
        return false;
    }
}
