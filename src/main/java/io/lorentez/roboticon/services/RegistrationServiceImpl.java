package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.BasicUserCommand;
import io.lorentez.roboticon.commands.RegistrationCommand;
import io.lorentez.roboticon.converters.RegistrationToRegistrationCommandConverter;
import io.lorentez.roboticon.model.*;
import io.lorentez.roboticon.model.security.User;
import io.lorentez.roboticon.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationToRegistrationCommandConverter registrationConverter;

    private final RegistrationStatusRepository registrationStatusRepository;
    private final RegistrationRepository registrationRepository;
    private final RobotRepository robotRepository;
    private final UserRepository userRepository;
    private final RobotTeamRepository robotTeamRepository;
    private final CompetitionRepository competitionRepository;

    @Override
    public List<RegistrationCommand> getTeamsRegistrationsForTournament(Long tournamentId, Long teamId) {
        log.info("Presenting registrations of team with ID: " + teamId.toString() + " to tournament with ID: " + tournamentId.toString());
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

    @Transactional
    @Override
    public RegistrationCommand updateRegistration(Long registrationId, RegistrationCommand newRegistrationData) {
        log.info("Updating registration form with ID: " + registrationId.toString());
        Optional<Registration> registrationOptional = registrationRepository.findFetchAllInfoById(registrationId);
        return registrationOptional.map(registration -> {
            if (!newRegistrationData.getRobot().getId().equals(registration.getRobot().getId())) {
                if (registrationRepository.getRegistrationByRobotIdAndCompetitionId(newRegistrationData.getRobot().getId(),
                        newRegistrationData.getCompetition().getId()).isPresent()) {
                    log.warn("Failure of attempt to register robot with ID: " + newRegistrationData.getRobot().getId().toString()
                            + " to competition with ID: " + newRegistrationData.getRobot().getId().toString() + ". " +
                            "Robot is already registered to competition!");
                    throw new IllegalArgumentException();
                }
                Robot robot = robotRepository.findById(newRegistrationData.getRobot().getId()).orElseThrow();
                registration.setRobot(robot);
            }
            Set<User> users = userRepository.findAllById(
                    newRegistrationData.getUserCommands()
                            .stream()
                            .map(BasicUserCommand::getId)
                            .collect(Collectors.toList()));
            registration.setUsers(users);
            registration = registrationRepository.save(registration);
            return registrationConverter.convert(registration);
        }).orElseThrow();
    }

    @Transactional
    @Override
    public RegistrationCommand createRegistration(RegistrationCommand newRegistrationData) {
        Registration registration = new Registration();
        Optional<Registration> currentRegistration = registrationRepository
                .getRegistrationByRobotIdAndCompetitionId(newRegistrationData.getRobot().getId(),
                        newRegistrationData.getCompetition().getId());
        if (currentRegistration.isPresent()){
            log.warn("Robot with id: " + newRegistrationData.getRobot().getId().toString()
                    + " is already registered for competition with id: "
                    + newRegistrationData.getCompetition().getId().toString());
            throw new IllegalArgumentException();
        }
        Robot robot = robotRepository.findById(newRegistrationData.getRobot().getId()).orElseThrow();
        registration.setRobot(robot);
        Set<User> users = userRepository.findAllById(
                newRegistrationData.getUserCommands()
                        .stream()
                        .map(BasicUserCommand::getId)
                        .collect(Collectors.toList()));
        registration.setUsers(users);
        Competition competition = competitionRepository.findById(
                newRegistrationData.getCompetition()
                        .getId()
        ).orElseThrow();
        registration.setCompetition(competition);
        RegistrationStatus status = RegistrationStatus.builder()
                .timeFrom(LocalDateTime.now())
                .registration(registration)
                .status(RegistrationCurrentStatus.APPLIED)
                .build();
        registration.setStatuses(Set.of(status));
        registration = registrationRepository.save(registration);
        return registrationConverter.convert(registration);
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
                    .registration(registration)
                    .build();
            registrationStatusRepository.saveAll(Set.of(currentStatus, newStatus));
        }
    }

    @Override
    public List<RegistrationCommand> getUserRegistrations(Long userId) {
        List<Registration> registrations = registrationRepository.getRegistrationsByUserId(userId);
        return registrations.stream()
                .map(registrationConverter::convert)
                .collect(Collectors.toList());
    }
}
