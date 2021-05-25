package io.lorentez.roboticon.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RobotTest {

    public static final Long ID = 1L;
    public static final String NAME = "Test3R Mk1";
    public static final LocalDateTime TIME_ADDED = LocalDateTime.now();

    @Test
    void builder() {
        //given

        //when
        Robot robot = Robot.builder()
                .id(ID)
                .name(NAME)
                .timeAdded(TIME_ADDED)
                .build();

        //then
        assertEquals(ID, robot.getId());
        assertEquals(NAME, robot.getName());
        assertEquals(TIME_ADDED, robot.getTimeAdded());
        assertNotNull(robot.getRegistrations());
        assertThat(robot.getRegistrations()).hasSize(0);
        assertNotNull(robot.getTeams());
        assertThat(robot.getTeams()).hasSize(0);
    }
}