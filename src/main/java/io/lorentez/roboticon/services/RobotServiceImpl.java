package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.RobotCommand;
import io.lorentez.roboticon.converters.RobotToRobotCommandConverter;
import io.lorentez.roboticon.model.Robot;
import io.lorentez.roboticon.model.RobotTeam;
import io.lorentez.roboticon.model.RobotTeamStatus;
import io.lorentez.roboticon.model.Team;
import io.lorentez.roboticon.repositories.RobotRepository;
import io.lorentez.roboticon.repositories.RobotTeamRepository;
import io.lorentez.roboticon.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class RobotServiceImpl implements RobotService{

    private final RobotToRobotCommandConverter robotToCommandConverter;
    private final RobotRepository robotRepository;
    private final RobotTeamRepository robotTeamRepository;
    private final TeamRepository teamRepository;

    @Override
    public RobotCommand update(RobotCommand command, Long robotId) {
        Robot existingRobot = robotRepository.findById(robotId)
                .orElseThrow(NoSuchElementException::new);
        this.updateRobotData(existingRobot, command);
        existingRobot = robotRepository.save(existingRobot);
        return robotToCommandConverter.convert(existingRobot);
    }

    void updateRobotData(Robot existingRobot, RobotCommand command){
        if (command.getName() == null || command.getName().isBlank()){
            throw new IllegalArgumentException("Updated robot name cannot be blank!");
        }
        existingRobot.setName(command.getName());
    }

    @Override
    public List<RobotCommand> list() {
        return robotRepository.findAll()
                .stream()
                .map(robotToCommandConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void transferToTeam(Long robotId, Long teamId) {
        Optional<RobotTeam> currentRobotTeamOptional = robotRepository.getRobotByActualStatus(robotId,
                RobotTeamStatus.OWNED);
        if(currentRobotTeamOptional.isEmpty()){
            log.warn("Cannot find current robot team of robot: " + robotId.toString());
            throw new NoSuchElementException("Cannot find current robot team of robot: " + robotId.toString());
        }
        RobotTeam currentRobotTeam = currentRobotTeamOptional.get();
        if (currentRobotTeam.getTeam().getId().equals(teamId)){
            return;
        }
        Optional<Team> newTeamOptional = teamRepository.findById(teamId);
        if (newTeamOptional.isEmpty()){
            log.error("Attempt of transfer robot with id: " + robotId.toString()
                    + " to not existing team with id: " + teamId.toString());
            return;
        }
        LocalDateTime timestamp = LocalDateTime.now();
        currentRobotTeam.setTimeRemoved(timestamp);
        RobotTeam robotTeamSent = RobotTeam.builder()
                .timeAdded(timestamp)
                .robot(currentRobotTeam.getRobot())
                .team(currentRobotTeam.getTeam())
                .status(RobotTeamStatus.SENT)
                .build();
        RobotTeam robotTeamPending = RobotTeam.builder()
                .robot(currentRobotTeam.getRobot())
                .team(newTeamOptional.get())
                .timeAdded(timestamp)
                .status(RobotTeamStatus.PENDING)
                .build();
        robotTeamRepository.save(currentRobotTeam);
        robotTeamRepository.saveAll(Set.of(robotTeamSent, robotTeamPending));
    }

    @Override
    public void transferAcceptRobot(Long robotId) {
        List<RobotTeam> robotTeams = robotRepository.getRobotActualStatuses(robotId);
        if (robotTeams.size() == 2){
            LocalDateTime timestamp = LocalDateTime.now();
            robotTeams.forEach(robotTeam -> robotTeam.setTimeRemoved(timestamp));
            Optional<RobotTeam> pendingRobotTeamOptional = robotTeams.stream()
                    .filter(robotTeam -> robotTeam.getStatus().equals(RobotTeamStatus.PENDING))
                    .findFirst();
            if (pendingRobotTeamOptional.isEmpty()){
                log.info("Robot with ID: " + robotId.toString() + " has more than one status and none of them is \'PENDING\'.");
                throw new IllegalStateException("Robot has more than one status and none of them is \'PENDING\'");
            }
            RobotTeam robotTeam = RobotTeam.builder()
                    .team(pendingRobotTeamOptional.get().getTeam())
                    .robot(pendingRobotTeamOptional.get().getRobot())
                    .status(RobotTeamStatus.OWNED)
                    .timeAdded(timestamp)
                    .build();
            robotTeamRepository.saveAll(robotTeams);
            robotTeamRepository.save(robotTeam);

        }
        else {
            log.info("Attempt of transfer accept robot with ID: " + robotId.toString() + " which was never sent");
            throw new IllegalStateException("robot was not sent");
        }
    }
}
