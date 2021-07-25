package io.lorentez.roboticon.security.managers;

import io.lorentez.roboticon.commands.RobotCommand;
import io.lorentez.roboticon.model.RobotTeam;
import io.lorentez.roboticon.model.RobotTeamStatus;
import io.lorentez.roboticon.model.UserTeam;
import io.lorentez.roboticon.model.UserTeamStatus;
import io.lorentez.roboticon.repositories.RobotRepository;
import io.lorentez.roboticon.repositories.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RobotsAuthenticationManager extends BasicAuthenticationManager{

    public RobotsAuthenticationManager(TeamRepository teamRepository, RobotRepository robotRepository) {
        super(teamRepository);
        this.robotRepository = robotRepository;
    }

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


    public boolean canUserViewRobot(Authentication authentication, Long robotId) {
        String email = (String) authentication.getPrincipal();
        if (email == null){
            return false;
        }
        log.info("User: " + email + " attempted to view robot with id: " + robotId.toString());
        List<Long> robotTeamIds = robotRepository.getRobotActualStatuses(robotId)
                .stream()
                .map(robotTeam -> robotTeam.getTeam().getId())
                .collect(Collectors.toList());
        List<UserTeam> userTeams = new ArrayList<>();
        robotTeamIds.forEach(teamId -> {
            teamRepository.findActualMembersByTeamId(teamId, email)
                    .ifPresent(userTeams::add);
        });
        return userTeams.stream().filter(userTeam -> userTeam.getStatus().equals(UserTeamStatus.OWNER) ||
                userTeam.getStatus().equals(UserTeamStatus.ADMIN) ||
                userTeam.getStatus().equals(UserTeamStatus.MEMBER))
                .findFirst()
                .map(userTeam -> Boolean.TRUE)
                .orElse(Boolean.FALSE);
    }

    public boolean canUserAddRobot(Authentication authentication, RobotCommand robotCommand){
        String email = (String) authentication.getPrincipal();
        if (email == null ||
                robotCommand == null ||
                robotCommand.getTeamCommand() == null ||
                robotCommand.getTeamCommand().getId() == null ||
                robotCommand.getTeamCommand().getId().equals(0L)){
            return false;
        }
        log.info("User: " + email + " attempted to add robot to team: " + robotCommand.getTeamCommand().getId());
        return isUserAdminOrOwner(email, robotCommand.getTeamCommand().getId());
    }
}
