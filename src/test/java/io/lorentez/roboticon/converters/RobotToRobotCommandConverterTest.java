package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.RobotCommand;
import io.lorentez.roboticon.model.Robot;
import io.lorentez.roboticon.model.RobotTeamStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RobotToRobotCommandConverterTest {

    public static final Long ID = 5312L;
    public static final String NAME = "Sample Name";
    public static final LocalDateTime TIME_ADDED = LocalDateTime.now();


    RobotToRobotCommandConverter converter;

    @BeforeEach
    void setUp() {
        converter = new RobotToRobotCommandConverter();
    }

    @Test
    void testNullObject() {
        //given

        //when
        RobotCommand robotCommand = converter.convert(null);

        //then
        assertNull(robotCommand);
    }

    @Test
    void testEmptyObject() {
        //given
        Robot robot = Robot.builder().build();

        //when
        RobotCommand robotCommand = converter.convert(robot);

        //then
        assertNotNull(robotCommand);
    }

    @Test
    void testFullObject() {
        //given
        Robot robot = Robot.builder()
                .id(ID)
                .name(NAME)
                .timeAdded(TIME_ADDED)
                .build();

        //when
        RobotCommand robotCommand = converter.convert(robot);

        //then
        assertNotNull(robotCommand);
        assertEquals(ID, robotCommand.getId());
        assertEquals(NAME, robotCommand.getName());
        assertEquals(TIME_ADDED, robotCommand.getTimeAdded());
    }
}