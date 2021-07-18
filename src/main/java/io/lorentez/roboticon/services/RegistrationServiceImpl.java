package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.BasicUserCommand;
import io.lorentez.roboticon.commands.RegistrationCommand;
import io.lorentez.roboticon.converters.RegistrationToRegistrationCommandConverter;
import io.lorentez.roboticon.model.Registration;
import io.lorentez.roboticon.model.RegistrationCurrentStatus;
import io.lorentez.roboticon.model.RegistrationStatus;
import io.lorentez.roboticon.model.Robot;
import io.lorentez.roboticon.model.security.User;
import io.lorentez.roboticon.repositories.RegistrationRepository;
import io.lorentez.roboticon.repositories.RobotRepository;
import io.lorentez.roboticon.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationToRegistrationCommandConverter registrationConverter;

    private final RegistrationRepository registrationRepository;
    private final RobotRepository robotRepository;
    private final UserRepository userRepository;

    @Override
    public List<RegistrationCommand> getTeamsRegistrationsForTournament(Long tournamentId, Long teamId) {
        List<Registration> registrations = registrationRepository.getTeamsRegistrationsInTournament(tournamentId, teamId);
        return registrations.stream()
                .map(registrationConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegistrationCommand> getRegistrationsForCompetition(Long competitionId) {
        List<Registration> registrations = registrationRepository.getRegistrationsForCompetition(competitionId);
        return registrations.stream().map(registrationConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public RegistrationCommand updateRegistration(Long registrationId, RegistrationCommand newRegistrationData) {
        Optional<Registration> registrationOptional = registrationRepository.findFetchAllInfoById(registrationId);
        return registrationOptional.map(registration -> {
            if (!newRegistrationData.getRobot().getId().equals(registration.getRobot().getId())) {
                Robot robot = robotRepository.findById(newRegistrationData.getRobot().getId()).orElseThrow();
                registration.setRobot(robot);
            }
            Iterable<User> usersIterable = userRepository.findAllById(
                    newRegistrationData.getUserCommands()
                            .stream()
                            .map(BasicUserCommand::getId)
                            .collect(Collectors.toList()));
            Set<User> users = new HashSet<>();
            usersIterable.forEach(users::add);
            registration.setUsers(users);
            registration = registrationRepository.save(registration);
            return registrationConverter.convert(registration);
        }).orElseThrow();
    }

    @Override
    public void setNewRegistrationStatus(Long registrationId, RegistrationCurrentStatus status) {
        LocalDateTime timestamp = LocalDateTime.now();
        Registration registration = registrationRepository.findFetchAllInfoById(registrationId).orElseThrow();
        RegistrationStatus currentStatus = registration.getStatuses()
                .stream()
                        .filter(registrationStatus ->
                                registrationStatus.getTimeTo() == null || registrationStatus.getTimeTo().isAfter(LocalDateTime.now()))
                        .findFirst()
                        .orElseThrow();
        if(!currentStatus.getStatus().equals(status)){
            currentStatus.setTimeTo(timestamp);
            RegistrationStatus newStatus = RegistrationStatus.builder()
                    .timeFrom(timestamp)
                    .status(status)
                    .build();
            registration.getStatuses().add(newStatus);
            registrationRepository.save(registration);
        }
    }


}
