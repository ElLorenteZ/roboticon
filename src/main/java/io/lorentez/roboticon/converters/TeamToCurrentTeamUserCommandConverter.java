package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.CurrentTeamUserCommand;
import io.lorentez.roboticon.model.Team;
import io.lorentez.roboticon.model.UserTeam;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TeamToCurrentTeamUserCommandConverter implements Converter<Team, CurrentTeamUserCommand> {

    @Synchronized
    @Nullable
    @Override
    public CurrentTeamUserCommand convert(Team team) {
        if (team == null){
            return  null;
        }
        CurrentTeamUserCommand command = new CurrentTeamUserCommand();
        command.setTeamId(team.getId());
        command.setName(team.getName());
        command.setTimeCreated(team.getTimeCreated());
        if (team.getUniversity() != null) {
            command.setUniversityName(team.getUniversity().getName());
        }
        Optional<UserTeam> statusOptional = team.getUserTeams()
                .stream()
                .filter(status -> status.getTimeRemoved() == null)
                .findFirst();
        statusOptional.ifPresent(userTeam -> command.setCurrentStatus(userTeam.getStatus()));
        return command;
    }
}
