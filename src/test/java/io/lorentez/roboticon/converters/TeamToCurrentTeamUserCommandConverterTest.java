package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.CurrentTeamUserCommand;
import io.lorentez.roboticon.model.Team;
import io.lorentez.roboticon.model.University;
import io.lorentez.roboticon.model.UserTeam;
import io.lorentez.roboticon.model.UserTeamStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TeamToCurrentTeamUserCommandConverterTest {

    private static final LocalDateTime TIME_FIRST = LocalDateTime.now().minusMonths(1);
    public static final LocalDateTime TIME_SECOND = LocalDateTime.now().minusDays(1);
    private static final Long ID = 150L;
    public static final String TEAM_NAME = "Test team name";
    public static final String UNIVERSITY_NAME = "Test university";


    TeamToCurrentTeamUserCommandConverter converter;

    @BeforeEach
    void setUp() {
        converter = new TeamToCurrentTeamUserCommandConverter();
    }

    @Test
    void testNull() {
        //given
        Team team = null;

        //when
        CurrentTeamUserCommand command = converter.convert(team);

        //then
        assertNull(command);
    }

    @Test
    void testEmpty() {
        //given
        Team team = new Team();

        //when
        CurrentTeamUserCommand command = converter.convert(team);

        //then
        assertNotNull(command);
    }

    @Test
    void testFullObject() {
        //given
        Set<UserTeam> statuses = Set.of(
                UserTeam.builder()
                        .id(1L)
                        .status(UserTeamStatus.MEMBER)
                        .timeAdded(TIME_FIRST)
                        .timeRemoved(TIME_SECOND)
                        .build(),
                UserTeam.builder()
                        .id(2L)
                        .status(UserTeamStatus.ADMIN)
                        .timeAdded(TIME_SECOND)
                        .build()
        );
        Team team = Team.builder()
                .id(ID)
                .name(TEAM_NAME)
                .university(University.builder().id(1L).name(UNIVERSITY_NAME).build())
                .userTeams(statuses)
                .timeCreated(TIME_FIRST)
                .build();

        //when
        CurrentTeamUserCommand command = converter.convert(team);

        //then
        assertNotNull(command);
        assertEquals(ID, command.getTeamId());
        assertEquals(TEAM_NAME, command.getName());
        assertEquals(UNIVERSITY_NAME, command.getUniversityName());
        assertEquals(TIME_FIRST, command.getTimeCreated());
        assertEquals(UserTeamStatus.ADMIN, command.getCurrentStatus());
    }
}