package io.lorentez.roboticon.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RobotTest {

    public static final Long ID = 1L;
    public static final String NAME = "Test3R Mk1";
    public static final LocalDateTime TIME_ADDED = LocalDateTime.now();

    @Test
    void builderEmptyCollections() {
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

    @Test
    void builderAndPopulateCollections() {
        //given
        Registration registration1 = Registration.builder()
                .id(1L)
                .build();
        Registration registration2 = Registration.builder()
                .id(2L)
                .build();
        Team team1 = Team.builder()
                .id(1L)
                .build();
        Team team2 = Team.builder()
                .id(2L)
                .build();

        //when
        Robot robot = Robot.builder()
                .id(ID)
                .name(NAME)
                .timeAdded(TIME_ADDED)
                .build();
        robot.getTeams().addAll(Set.of(team1, team2));
        robot.getRegistrations().addAll(Set.of(registration1, registration2));

        //then
        assertEquals(ID, robot.getId());
        assertEquals(NAME, robot.getName());
        assertEquals(TIME_ADDED, robot.getTimeAdded());
        assertNotNull(robot.getRegistrations());
        assertThat(robot.getRegistrations()).hasSize(2);
        assertNotNull(robot.getTeams());
        assertThat(robot.getTeams()).hasSize(2);
    }
}