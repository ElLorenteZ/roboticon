package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.RegistrationCommand;
import io.lorentez.roboticon.model.Registration;
import io.lorentez.roboticon.model.RegistrationStatus;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Component
public class RegistrationToRegistrationCommandConverter implements Converter<Registration, RegistrationCommand> {

    private final RobotToRobotCommandConverter robotConverter;
    private final CompetitionToCompetitionCommandConverter competitionConverter;
    private final UserToBasicUserCommandConverter userConverter;

    @Synchronized
    @Nullable
    @Override
    public RegistrationCommand convert(Registration registration) {
        if (registration == null){
            return null;
        }
        RegistrationCommand command = new RegistrationCommand();
        command.setId(registration.getId());
        command.setCompetition(competitionConverter.convert(registration.getCompetition()));
        command.setRobot(robotConverter.convert(registration.getRobot()));
        registration.getUsers().forEach(user -> {
            command.getUserCommands().add(userConverter.convert(user));
        });
        command.setStatus(registration.getStatuses()
                .stream()
                .filter(registrationStatus -> registrationStatus.getTimeTo() == null ||
                        registrationStatus.getTimeTo().isAfter(LocalDateTime.now()))
                .findFirst()
                .map(RegistrationStatus::getStatus)
                .orElseThrow());
        return command;
    }
}
