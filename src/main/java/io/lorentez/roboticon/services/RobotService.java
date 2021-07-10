package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.RobotCommand;

import java.util.List;

public interface RobotService {

    RobotCommand update(RobotCommand command, Long robotId);

    List<RobotCommand> list();

}
