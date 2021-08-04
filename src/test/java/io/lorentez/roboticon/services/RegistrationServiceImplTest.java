package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.BasicUserCommand;
import io.lorentez.roboticon.commands.CompetitionCommand;
import io.lorentez.roboticon.commands.RegistrationCommand;
import io.lorentez.roboticon.commands.RobotCommand;
import io.lorentez.roboticon.converters.RegistrationToRegistrationCommandConverter;
import io.lorentez.roboticon.model.*;
import io.lorentez.roboticon.model.security.User;
import io.lorentez.roboticon.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {

    public static final Long REGISTRATION_ID = 60L;

    public static final Long ROBOT_ID = 120L;

    public static final Long COMPETITION_ID = 210L;

    public static final Long USER_ID = 50L;

    @Captor
    ArgumentCaptor<Registration> registrationArgumentCaptor;

    @Mock
    UserRepository userRepository;

    @Mock
    RobotRepository robotRepository;

    @Mock
    RegistrationRepository registrationRepository;

    @Mock
    RegistrationToRegistrationCommandConverter registrationConverter;

    @Mock
    RegistrationStatusRepository registrationStatusRepository;

    @Mock
    RobotTeamRepository robotTeamRepository;

    @Mock
    CompetitionRepository competitionRepository;

    @InjectMocks
    RegistrationServiceImpl registrationsService;

    @Test
    void testGetTeamsRegistrationsForTournament() {
        registrationsService.getTeamsRegistrationsForTournament(1L, 1L);

        verify(registrationRepository).getTeamsRegistrationsInTournament(any(), any());
        verifyNoMoreInteractions(registrationRepository);
    }

    @Test
    void testGetTeamsRegistrationsForCompetitions() {
        registrationsService.getRegistrationsForCompetition(1L);

        verify(registrationRepository).getRegistrationsForCompetition(any());
        verifyNoMoreInteractions(registrationRepository);
    }

    @Test
    void testUpdateRegistrationNoRobotUpdate() {
        BasicUserCommand userCommand = new BasicUserCommand(
                50L,
                "John",
                "Doe",
                "mail@test.pl");
        RegistrationCommand newDataCommand = RegistrationCommand.builder()
                .id(REGISTRATION_ID)
                .robot(RobotCommand.builder().id(ROBOT_ID).build())
                .competition(CompetitionCommand.builder().id(COMPETITION_ID).build())
                .userCommands(List.of(userCommand))
                .build();
        Registration registration = Registration.builder()
                .id(REGISTRATION_ID)
                .robot(Robot.builder().id(ROBOT_ID).build())
                .competition(Competition.builder().id(COMPETITION_ID).build())
                .build();
        given(registrationRepository.findFetchAllInfoById(anyLong())).willReturn(Optional.of(registration));
        given(registrationConverter.convert(any())).willReturn(RegistrationCommand.builder().id(REGISTRATION_ID).build());

        registrationsService.updateRegistration(REGISTRATION_ID, newDataCommand);

        verifyNoInteractions(robotRepository);
        verify(registrationRepository).save(any());
        verify(registrationRepository).findFetchAllInfoById(any());
        verify(userRepository).findAllById(any());
        verifyNoMoreInteractions(registrationRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testUpdateRegistrationWithRobotUpdate() {
        BasicUserCommand userCommand = new BasicUserCommand(
                50L,
                "John",
                "Doe",
                "mail@test.pl");
        RegistrationCommand newDataCommand = RegistrationCommand.builder()
                .id(REGISTRATION_ID)
                .robot(RobotCommand.builder().id(ROBOT_ID).build())
                .competition(CompetitionCommand.builder().id(COMPETITION_ID).build())
                .userCommands(List.of(userCommand))
                .build();
        Registration registration = Registration.builder()
                .id(REGISTRATION_ID)
                .robot(Robot.builder().id(11111L).build())
                .competition(Competition.builder().id(COMPETITION_ID).build())
                .build();
        given(registrationRepository.findFetchAllInfoById(anyLong())).willReturn(Optional.of(registration));
        given(registrationConverter.convert(any())).willReturn(RegistrationCommand.builder().id(REGISTRATION_ID).build());
        given(robotRepository.findById(anyLong())).willReturn(Optional.of(Robot.builder().id(ROBOT_ID).build()));

        registrationsService.updateRegistration(REGISTRATION_ID, newDataCommand);

        verify(robotRepository).findById(anyLong());
        verifyNoMoreInteractions(robotRepository);
        verify(registrationRepository).save(any());
        verify(registrationRepository).findFetchAllInfoById(any());
        verify(userRepository).findAllById(any());
        verify(registrationRepository).getRegistrationByRobotIdAndCompetitionId(anyLong(), anyLong());
        verifyNoMoreInteractions(registrationRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testSetNewRegistrationStatus() {
        //given
        Set<RegistrationStatus> statuses = new HashSet<>();
        statuses.add(RegistrationStatus.builder()
                .timeFrom(LocalDateTime.now().minusDays(5))
                .status(RegistrationCurrentStatus.APPLIED)
                .build());
        Registration registration = Registration.builder()
                .id(REGISTRATION_ID)
                .robot(Robot.builder().id(ROBOT_ID).build())
                .competition(Competition.builder().id(COMPETITION_ID).build())
                .statuses(statuses)
                .build();
        given(registrationRepository.findFetchAllInfoById(anyLong())).willReturn(Optional.of(registration));

        //when
        registrationsService.setNewRegistrationStatus(REGISTRATION_ID, RegistrationCurrentStatus.CONFIRMED);

        //then
        verify(registrationRepository).findFetchAllInfoById(anyLong());
        verify(registrationStatusRepository).saveAll(any());
        verifyNoMoreInteractions(registrationRepository);
    }

    @Test
    void testSetNewRegistrationStatusSameStatus() {
        //given
        Registration registration = Registration.builder()
                .id(REGISTRATION_ID)
                .robot(Robot.builder()
                        .id(ROBOT_ID)
                        .robotTeams(Set.of(RobotTeam.builder()
                                .team(Team.builder()
                                        .id(1L)
                                        .build())
                                .build()))
                        .build())
                .competition(Competition.builder().id(COMPETITION_ID).build())
                .statuses(Set.of(
                        RegistrationStatus.builder()
                        .timeFrom(LocalDateTime.now().minusDays(5))
                        .status(RegistrationCurrentStatus.APPLIED)
                        .build())
                )
                .build();
        given(registrationRepository.findFetchAllInfoById(anyLong())).willReturn(Optional.of(registration));

        //when
        registrationsService.setNewRegistrationStatus(REGISTRATION_ID, RegistrationCurrentStatus.APPLIED);

        //then
        verify(registrationRepository).findFetchAllInfoById(anyLong());
        verifyNoMoreInteractions(registrationRepository);
    }

    @Test
    void testCreateRegistration() {
        //given
        BasicUserCommand userCommand = new BasicUserCommand(
                USER_ID,
                "John",
                "Doe",
                "mail@test.pl");
        RegistrationCommand newRegistration = RegistrationCommand.builder()
                .robot(RobotCommand.builder().id(ROBOT_ID).build())
                .competition(CompetitionCommand.builder().id(COMPETITION_ID).build())
                .userCommands(List.of(userCommand))
                .build();
        given(competitionRepository.findById(anyLong()))
                .willReturn(Optional.of(Competition.builder().id(COMPETITION_ID).build()));
        given(robotRepository.findById(anyLong()))
                .willReturn(Optional.of(Robot.builder().id(ROBOT_ID).build()));
        given(userRepository.findAllById(any()))
                .willReturn(Set.of(User.builder().id(USER_ID).build()));

        //when
        RegistrationCommand command = registrationsService.createRegistration(newRegistration);

        //then
        verify(registrationRepository).save(registrationArgumentCaptor.capture());
        verify(competitionRepository).findById(anyLong());
        verify(robotRepository).findById(anyLong());
        verify(userRepository).findAllById(any());
        verify(registrationRepository).getRegistrationByRobotIdAndCompetitionId(anyLong(), anyLong());
        verify(registrationConverter).convert(any());
        verifyNoMoreInteractions(registrationRepository);
        Registration registration = registrationArgumentCaptor.getValue();
        assertNotNull(registration);
        assertNotNull(registration.getCompetition());
        assertEquals(COMPETITION_ID, registration.getCompetition().getId());
        assertNotNull(registration.getRobot());
        assertEquals(ROBOT_ID, registration.getRobot().getId());
        assertThat(registration.getUsers()).hasSize(1);
    }

    @Test
    void testGetUserRegistrations() {
        //given
        given(registrationRepository.getRegistrationsByUserId(anyLong()))
                .willReturn(List.of(Registration.builder().id(1L).build()));

        //when
        List<RegistrationCommand> registrationCommands = registrationsService.getUserRegistrations(1L);

        //then
        verify(registrationRepository).getRegistrationsByUserId(anyLong());
        verify(registrationConverter).convert(any());
        verifyNoMoreInteractions(registrationRepository);
        verifyNoMoreInteractions(registrationConverter);
    }
}