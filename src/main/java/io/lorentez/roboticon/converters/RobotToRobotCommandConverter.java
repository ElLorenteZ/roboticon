package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.RobotCommand;
import io.lorentez.roboticon.model.Robot;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RobotToRobotCommandConverter implements Converter<Robot, RobotCommand> {

    @Synchronized
    @Nullable
    @Override
    public RobotCommand convert(Robot robot) {
        if(robot == null){
            return null;
        }
        return RobotCommand.builder()
                .id(robot.getId())
                .name(robot.getName())
                .timeAdded(robot.getTimeAdded())
                .build();
    }
}
