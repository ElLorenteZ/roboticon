package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.RobotCommand;
import io.lorentez.roboticon.model.Robot;
import io.lorentez.roboticon.model.RobotTeam;
import io.lorentez.roboticon.model.RobotTeamStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RobotTeamToRobotCommandConverterTest {

    public static final Long ROBOT_ID = 2L;
    public static final String ROBOT_NAME = "Robot sample name";
    public static final LocalDateTime ROBOT_TIME_ADDED = LocalDateTime.now().minusDays(1);
    public static final RobotTeamStatus ROBOT_STATUS = RobotTeamStatus.OWNED;

    RobotTeamToRobotCommandConverter converter;

    @BeforeEach
    void setUp() {
        converter = new RobotTeamToRobotCommandConverter();
    }

    @Test
    void testNullObject() {
        //given

        //when
        RobotCommand command = converter.convert(null);

        //then
        assertNull(command);
    }

    @Test
    void testNoRobot() {
        //given

        //when
        RobotCommand command = converter.convert(RobotTeam.builder().build());

        //then
        assertNull(command);
    }

    @Test
    void testEmptyRobot() {
        //given

        //when
        RobotCommand command = converter.convert(RobotTeam.builder()
                .robot(Robot.builder().build())
                .build());

        //then
        assertNotNull(command);
    }

    @Test
    void testFullObject() {
        //given
        Robot robot = Robot.builder()
                .id(ROBOT_ID)
                .name(ROBOT_NAME)
                .timeAdded(ROBOT_TIME_ADDED)
                .build();
        RobotTeam robotTeam = RobotTeam.builder()
                .id(10L)
                .robot(robot)
                .timeAdded(ROBOT_TIME_ADDED)
                .status(ROBOT_STATUS)
                .build();

        //when
        RobotCommand robotCommand = converter.convert(robotTeam);

        //then
        assertNotNull(robotCommand);
        assertEquals(ROBOT_ID, robotCommand.getId());
        assertEquals(ROBOT_NAME, robotCommand.getName());
        assertEquals(ROBOT_TIME_ADDED, robotCommand.getTimeAdded());
        assertEquals(ROBOT_STATUS, robotCommand.getStatus());
    }
}