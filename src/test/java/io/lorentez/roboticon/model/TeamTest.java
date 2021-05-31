package io.lorentez.roboticon.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    public static final Long ID = 1L;
    public static final String NAME = "Sample Team";
    public static final LocalDateTime TIME_CREATED = LocalDateTime.now();

    @Test
    void builderEmptyCollections() {
        //given

        //when
        Team team = Team.builder()
                .id(ID)
                .name(NAME)
                .timeCreated(TIME_CREATED)
                .build();

        //then
        assertNotNull(team.getRobotTeams());
        assertThat(team.getRobotTeams()).hasSize(0);

        assertNotNull(team.getUserTeams());
        assertThat(team.getUserTeams()).hasSize(0);
    }

    @Test
    void builderAndPopulateCollections() {
        //given
        RobotTeam robot1 = RobotTeam.builder().id(1L).build();
        RobotTeam robot2 = RobotTeam.builder().id(2L).build();

        UserTeam userTeam1 = UserTeam.builder().id(1L).build();
        UserTeam userTeam2 = UserTeam.builder().id(2L).build();

        //when
        Team team = Team.builder()
                .id(ID)
                .name(NAME)
                .timeCreated(TIME_CREATED)
                .build();
        team.getUserTeams().addAll(Set.of(userTeam1, userTeam2));
        team.getRobotTeams().addAll(Set.of(robot1, robot2));

        //then
        assertNotNull(team.getRobotTeams());
        assertThat(team.getRobotTeams()).hasSize(2);

        assertNotNull(team.getUserTeams());
        assertThat(team.getUserTeams()).hasSize(2);
    }
}