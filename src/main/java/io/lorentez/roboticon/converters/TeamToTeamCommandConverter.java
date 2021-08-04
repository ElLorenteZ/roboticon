package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.TeamCommand;
import io.lorentez.roboticon.model.Team;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TeamToTeamCommandConverter implements Converter<Team, TeamCommand> {

    private final UserTeamToUserInTeamCommandConverter userConverter;

    private final RobotTeamToRobotCommandConverter robotTeamToRobotCommandConverter;

    private final UniversityToUniversityCommandConverter universityConverter;

    @Synchronized
    @Nullable
    @Override
    public TeamCommand convert(Team team) {
        if (team == null){
            return null;
        }
        TeamCommand command = new TeamCommand();
        command.setId(team.getId());
        command.setName(team.getName());
        command.setTimeCreated(team.getTimeCreated());
        command.setUniversity(universityConverter.convert(team.getUniversity()));
        command.setRobots(team.getRobotTeams()
                .stream()
                .filter(robotTeam -> robotTeam.getTimeRemoved() == null ||
                        (robotTeam.getTimeAdded().isBefore(LocalDateTime.now())
                                && robotTeam.getTimeRemoved().isAfter(LocalDateTime.now())))
                .map(robotTeamToRobotCommandConverter::convert)
                .collect(Collectors.toList()));
        command.setUsers(team.getUserTeams()
                .stream()
                .filter(userTeam -> userTeam.getTimeRemoved() == null ||
                        (userTeam.getTimeAdded().isBefore(LocalDateTime.now())
                                && userTeam.getTimeRemoved().isAfter(LocalDateTime.now())))
                .map(userConverter::convert)
                .collect(Collectors.toList()));
        return command;
    }
}
