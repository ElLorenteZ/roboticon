package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.RobotCommand;
import io.lorentez.roboticon.model.Robot;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RobotCommandToRobotConverter implements Converter<RobotCommand, Robot> {

    @Synchronized
    @Nullable
    @Override
    public Robot convert(RobotCommand robotCommand) {
        if (robotCommand == null){
            return null;
        }
        return Robot.builder()
                .id(robotCommand.getId())
                .name(robotCommand.getName())
                .timeAdded(robotCommand.getTimeAdded())
                .build();
    }
}
