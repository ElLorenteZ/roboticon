package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.RobotCommand;

public interface RobotService {
    RobotCommand update(RobotCommand command, Long robotId);
}
