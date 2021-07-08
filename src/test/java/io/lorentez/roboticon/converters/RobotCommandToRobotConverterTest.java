package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.RobotCommand;
import io.lorentez.roboticon.model.Robot;
import io.lorentez.roboticon.model.RobotTeamStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RobotCommandToRobotConverterTest {

    public static final Long ID = 5312L;
    public static final String NAME = "Sample Name";
    public static final LocalDateTime TIME_ADDED = LocalDateTime.now();

    RobotCommandToRobotConverter converter;

    @BeforeEach
    void setUp() {
        converter = new RobotCommandToRobotConverter();
    }

    @Test
    void testNullObject() {
        //given

        //when
        Robot robot = converter.convert(null);

        //then
        assertNull(robot);
    }

    @Test
    void testEmptyObject() {
        //given
        RobotCommand robotCommand = new RobotCommand();

        //when
        Robot robot = converter.convert(robotCommand);

        //then
        assertNotNull(robot);

    }

    @Test
    void testFullObject() {
        //given
        RobotCommand robotCommand = RobotCommand.builder()
                .id(ID)
                .name(NAME)
                .timeAdded(TIME_ADDED)
                .build();

        //when
        Robot robot = converter.convert(robotCommand);

        //then
        assertNotNull(robot);
        assertEquals(ID, robot.getId());
        assertEquals(NAME, robot.getName());
        assertEquals(TIME_ADDED, robot.getTimeAdded());
    }
}