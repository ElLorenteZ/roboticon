package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.RobotCommand;
import io.lorentez.roboticon.converters.RobotCommandToRobotConverter;
import io.lorentez.roboticon.converters.RobotToRobotCommandConverter;
import io.lorentez.roboticon.model.Robot;
import io.lorentez.roboticon.repositories.RobotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class RobotServiceImpl implements RobotService{

    private final RobotToRobotCommandConverter robotToCommandConverter;
    private final RobotRepository robotRepository;

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
}
