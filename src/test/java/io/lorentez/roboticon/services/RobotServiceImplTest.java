package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.RobotCommand;
import io.lorentez.roboticon.converters.RobotToRobotCommandConverter;
import io.lorentez.roboticon.model.Robot;
import io.lorentez.roboticon.model.RobotTeam;
import io.lorentez.roboticon.model.RobotTeamStatus;
import io.lorentez.roboticon.model.Team;
import io.lorentez.roboticon.repositories.RobotRepository;
import io.lorentez.roboticon.repositories.RobotTeamRepository;
import io.lorentez.roboticon.repositories.TeamRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RobotServiceImplTest {

    @Mock
    RobotToRobotCommandConverter robotToCommandConverter;

    @Mock
    TeamRepository teamRepository;

    @Mock
    RobotRepository robotRepository;

    @Mock
    RobotTeamRepository robotTeamRepository;

    @InjectMocks
    RobotServiceImpl service;

    public static final Long OTHER_ID = 600L;
    public static final Long ROBOT_ID = 150L;
    public static final String ROBOT_NAME = "Test name";
    public static final String ROBOT_NAME_OLD = "Previous name";

    @Test
    void robotUpdateNotFound() {
        given(robotRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            service.update(RobotCommand.builder().name(ROBOT_NAME).build(), 1L);
        });
    }

    @Test
    void robotUpdateNameBlankFound() {
        given(robotRepository.findById(ROBOT_ID))
                .willReturn(Optional.of(Robot.builder().id(ROBOT_ID).build()));
        assertThrows(IllegalArgumentException.class, () -> {
            service.update(RobotCommand.builder().name("").build(), ROBOT_ID);
        });
    }

    @Test
    void robotUpdateSuccess() {
        given(robotRepository.findById(ROBOT_ID))
                .willReturn(Optional.of(Robot.builder()
                        .id(ROBOT_ID)
                        .name(ROBOT_NAME_OLD)
                        .build()));
        given(robotRepository.save(any(Robot.class))).willReturn(Robot.builder()
                .id(ROBOT_ID)
                .name(ROBOT_NAME)
                .build());

        RobotCommand command = service.update(RobotCommand.builder()
                .id(OTHER_ID)
                .name(ROBOT_NAME)
                .build(), ROBOT_ID);

        verify(robotRepository).save(any());
        verify(robotToCommandConverter).convert(any());
        verify(robotRepository).findById(anyLong());
        verifyNoMoreInteractions(robotRepository);
        verifyNoMoreInteractions(robotToCommandConverter);
    }

    @Test
    void testUpdateData() {
        //given
        Robot existingRobot = Robot.builder()
                .id(ROBOT_ID)
                .name(ROBOT_NAME_OLD)
                .build();
        RobotCommand command = RobotCommand.builder()
                .id(600L)
                .name(ROBOT_NAME)
                .build();

        //when
        service.updateRobotData(existingRobot, command);

        //then
        assertNotNull(existingRobot);
        assertEquals(ROBOT_ID, existingRobot.getId());
        assertEquals(ROBOT_NAME, existingRobot.getName());
    }

    @Test
    void testListAll() {
        //given
        given(robotRepository.findAll()).willReturn(
                List.of(Robot.builder().id(1L).build(),
                        Robot.builder().id(2L).build())
        );

        //when
        List<RobotCommand> commandList = service.list();

        //then
        assertNotNull(commandList);
        verify(robotRepository).findAll();
        verify(robotToCommandConverter, times(2)).convert(any());
        verifyNoMoreInteractions(robotRepository);
        verifyNoMoreInteractions(robotToCommandConverter);
    }

    @Test
    void testTransferToTeam() {
        //given
        given(robotRepository.getRobotByActualStatus(anyLong(), any()))
                .willReturn(Optional.of(
                        RobotTeam.builder()
                                .id(11L)
                                .robot(Robot.builder().id(100L).build())
                                .team(Team.builder().id(11L).build())
                                .status(RobotTeamStatus.OWNED)
                                .build()));
        given(teamRepository.findById(any())).willReturn(Optional.of(Team.builder().build()));

        //when
        service.transferToTeam(100L, 10L);

        //then
        verify(robotRepository).getRobotByActualStatus(any(), any());
        verify(robotTeamRepository, times(1)).save(any());
        verify(robotTeamRepository, times(1)).saveAll(any());
        verify(teamRepository).findById(anyLong());
        verifyNoMoreInteractions(robotRepository);
        verifyNoMoreInteractions(teamRepository);
        verifyNoInteractions(robotToCommandConverter);
    }

    @Test
    void testTransferToSameTeam() {
        //given
        given(robotRepository.getRobotByActualStatus(anyLong(), any()))
                .willReturn(Optional.of(
                        RobotTeam.builder()
                                .id(11L)
                                .robot(Robot.builder().id(100L).build())
                                .team(Team.builder().id(10L).build())
                                .status(RobotTeamStatus.OWNED)
                                .build()));

        //when
        service.transferToTeam(100L, 10L);

        //then
        verify(robotRepository).getRobotByActualStatus(any(), any());
        verifyNoMoreInteractions(robotRepository);
        verifyNoInteractions(teamRepository);
        verifyNoInteractions(robotToCommandConverter);
        verifyNoInteractions(robotTeamRepository);
    }

    @Test
    void testTransferAcceptRequested() {
        LocalDateTime timestamp = LocalDateTime.now().minusDays(1);
        given(robotRepository.getRobotActualStatuses(anyLong()))
                .willReturn(List.of(
                        RobotTeam.builder()
                                .robot(Robot.builder().id(15L).build())
                                .team(Team.builder().id(20L).build())
                                .timeAdded(timestamp)
                                .status(RobotTeamStatus.SENT)
                                .build(),
                        RobotTeam.builder()
                                .robot(Robot.builder().id(15L).build())
                                .team(Team.builder().id(21L).build())
                                .timeAdded(timestamp)
                                .status(RobotTeamStatus.PENDING)
                                .build()
                ));

        service.transferAcceptRobot(10L);

        verify(robotRepository).getRobotActualStatuses(10L);
        verify(robotTeamRepository).save(any());
        verify(robotTeamRepository).saveAll(any());
        verifyNoMoreInteractions(robotRepository);
        verifyNoMoreInteractions(robotTeamRepository);
        verifyNoInteractions(teamRepository);
    }

    @Test
    void testTransferAcceptNotRequested() {
        given(robotRepository.getRobotActualStatuses(anyLong()))
                .willReturn(List.of(
                        RobotTeam.builder()
                                .robot(Robot.builder().id(15L).build())
                                .team(Team.builder().id(20L).build())
                                .timeAdded(LocalDateTime.now().minusDays(1))
                                .status(RobotTeamStatus.OWNED)
                                .build()
                ));

        Assertions.assertThrows(IllegalStateException.class, () -> {
            service.transferAcceptRobot(10L);
        });

        verify(robotRepository).getRobotActualStatuses(10L);
        verifyNoMoreInteractions(robotRepository);
        verifyNoInteractions(robotTeamRepository);
        verifyNoInteractions(teamRepository);
    }
}