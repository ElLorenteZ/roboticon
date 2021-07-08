package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.RobotCommand;
import io.lorentez.roboticon.converters.RobotToRobotCommandConverter;
import io.lorentez.roboticon.model.Robot;
import io.lorentez.roboticon.repositories.RobotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class RobotServiceImplTest {

    @Mock
    RobotToRobotCommandConverter robotToCommandConverter;

    @Mock
    RobotRepository robotRepository;

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
}