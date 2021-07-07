package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.RobotCommand;
import io.lorentez.roboticon.commands.TeamCommand;
import io.lorentez.roboticon.commands.UniversityCommand;
import io.lorentez.roboticon.commands.UserInTeamCommand;
import io.lorentez.roboticon.model.*;
import io.lorentez.roboticon.model.security.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamToTeamCommandConverterTest {

    public static final Long ID = 2341L;
    public static final String NAME = "Sample Name";
    public static final LocalDateTime TIME_CREATED = LocalDateTime.now().minusDays(1);

    @Mock
    UserTeamToUserInTeamCommandConverter userConverter;

    @Mock
    RobotToRobotCommandConverter robotConverter;

    @Mock
    UniversityToUniversityCommandConverter universityConverter;

    @InjectMocks
    TeamToTeamCommandConverter converter;

    @Test
    void testNullObject() {
        //given

        //when
        TeamCommand teamCommand = converter.convert(null);

        //then
        assertNull(teamCommand);
        verifyNoInteractions(universityConverter);
        verifyNoInteractions(robotConverter);
        verifyNoInteractions(userConverter);
    }

    @Test
    void testEmptyObject() {
        //given
        Team team = Team.builder()
                .build();

        //when
        TeamCommand teamCommand = converter.convert(team);

        //then
        assertNotNull(teamCommand);
        verify(universityConverter, times(1)).convert(any());
        verifyNoMoreInteractions(universityConverter);
        verifyNoInteractions(robotConverter);
        verifyNoInteractions(userConverter);
    }

    @Test
    void testFullObject() {
        //given
        Team team = Team.builder()
                .id(ID)
                .name(NAME)
                .timeCreated(TIME_CREATED)
                .university(University.builder()
                        .id(2L)
                        .build())
                .userTeams(Set.of(
                        UserTeam.builder()
                                .id(1L)
                                .timeAdded(TIME_CREATED.minusHours(20))
                                .user(User.builder()
                                        .id(2L)
                                        .name("Sample name")
                                        .surname("Sample surname")
                                        .email("Sample email")
                                        .build())
                                .status(UserTeamStatus.ADMIN)
                                .build(),
                        UserTeam.builder()
                                .id(10L)
                                .timeAdded(TIME_CREATED.minusHours(20))
                                .timeRemoved(LocalDateTime.now())
                                .user(User.builder()
                                        .id(3L)
                                        .name("Sample name")
                                        .surname("Sample surname")
                                        .email("Sample email")
                                        .build())
                                .status(UserTeamStatus.MEMBER)
                                .build()
                        ))
                .robotTeams(Set.of(
                        RobotTeam.builder()
                                .id(100L)
                                .timeAdded(TIME_CREATED)
                                .robot(Robot.builder()
                                        .id(100L)
                                        .name("Sample robot 1")
                                        .timeAdded(TIME_CREATED)
                                        .build())
                                .build(),
                        RobotTeam.builder()
                                .id(101L)
                                .timeAdded(TIME_CREATED)
                                .timeRemoved(LocalDateTime.now().minusSeconds(10))
                                .robot(Robot.builder()
                                        .id(101L)
                                        .name("Sample robot 2")
                                        .timeAdded(TIME_CREATED)
                                        .build())
                                .build()
                ))
                .build();
        given(universityConverter.convert(any())).willReturn(UniversityCommand.builder().id(2L).build());
        given(robotConverter.convert(any())).willReturn(RobotCommand.builder()
                .id(100L)
                .name("Sample robot 1")
                .timeAdded(TIME_CREATED)
                .build());
        UserInTeamCommand userInTeamCommand = new UserInTeamCommand();
        userInTeamCommand.setId(2L);
        userInTeamCommand.setName("Sample name");
        userInTeamCommand.setSurname("Sample surname");
        userInTeamCommand.setEmail("Sample email");
        userInTeamCommand.setStatus(UserTeamStatus.ADMIN);
        given(userConverter.convert(any())).willReturn(userInTeamCommand);

        //when
        TeamCommand teamCommand = converter.convert(team);

        //then
        assertNotNull(teamCommand);
        assertEquals(ID, teamCommand.getId());
        assertEquals(NAME, teamCommand.getName());
        assertEquals(TIME_CREATED, teamCommand.getTimeCreated());
        assertNotNull(teamCommand.getUniversityCommand());
        verify(universityConverter, times(1)).convert(any());
        verifyNoMoreInteractions(universityConverter);
        verify(robotConverter, times(1)).convert(any());
        verifyNoMoreInteractions(robotConverter);
        verify(userConverter, times(1)).convert(any());
        verifyNoMoreInteractions(universityConverter);
    }
}