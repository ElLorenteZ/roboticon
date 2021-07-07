package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.BasicTeamCommand;
import io.lorentez.roboticon.model.Team;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamToBasicTeamCommandConverter implements Converter<Team, BasicTeamCommand> {

    private final UniversityToUniversityCommandConverter universityConverter;

    @Synchronized
    @Nullable
    @Override
    public BasicTeamCommand convert(Team team) {
        if (team == null){
            return null;
        }
        return BasicTeamCommand.builder()
                .id(team.getId())
                .name(team.getName())
                .timeCreated(team.getTimeCreated())
                .universityCommand(universityConverter.convert(team.getUniversity()))
                .build();
    }
}
