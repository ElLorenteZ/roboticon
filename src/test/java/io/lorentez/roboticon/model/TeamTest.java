package io.lorentez.roboticon.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    public static final Long ID = 1L;
    public static final String NAME = "Sample Team";
    public static final LocalDateTime TIME_CREATED = LocalDateTime.now();

    @Test
    void builder() {
        //given

        //when
        Team team = Team.builder()
                .id(ID)
                .name(NAME)
                .timeCreated(TIME_CREATED)
                .build();

        //then
        assertNotNull(team.getRobots());
        assertThat(team.getRobots()).hasSize(0);

        assertNotNull(team.getUserTeams());
        assertThat(team.getUserTeams()).hasSize(0);
    }
}