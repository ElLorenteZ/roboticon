package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.RobotCommand;
import io.lorentez.roboticon.model.RobotTeam;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RobotTeamToRobotCommandConverter implements Converter<RobotTeam, RobotCommand> {

    @Synchronized
    @Nullable
    @Override
    public RobotCommand convert(RobotTeam robotTeam) {
        if (robotTeam == null || robotTeam.getRobot() == null){
            return null;
        }
        RobotCommand robotCommand = RobotCommand.builder()
                .id(robotTeam.getRobot().getId())
                .name(robotTeam.getRobot().getName())
                .timeAdded(robotTeam.getRobot().getTimeAdded())
                .status(robotTeam.getStatus())
                .build();
        return robotCommand;
    }
}
