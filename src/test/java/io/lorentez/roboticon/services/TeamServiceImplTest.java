package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.BasicTeamCommand;
import io.lorentez.roboticon.commands.CurrentTeamUserCommand;
import io.lorentez.roboticon.commands.TeamCommand;
import io.lorentez.roboticon.commands.UniversityCommand;
import io.lorentez.roboticon.converters.TeamToBasicTeamCommandConverter;
import io.lorentez.roboticon.converters.TeamToCurrentTeamUserCommandConverter;
import io.lorentez.roboticon.converters.TeamToTeamCommandConverter;
import io.lorentez.roboticon.converters.UniversityCommandToUniversityConverter;
import io.lorentez.roboticon.model.Team;
import io.lorentez.roboticon.model.University;
import io.lorentez.roboticon.model.UserTeam;
import io.lorentez.roboticon.model.UserTeamStatus;
import io.lorentez.roboticon.model.security.User;
import io.lorentez.roboticon.repositories.TeamRepository;
import io.lorentez.roboticon.repositories.UserRepository;
import io.lorentez.roboticon.repositories.UserTeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceImplTest {

    public static final Long TEAM_ID = 8L;
    public static final String TEAM_NAME = "Test team name";
    public static final String TEAM_UPDATED_NAME = "New team name";
    public static final String USER_EMAIL = "email@test.com";

    @Mock
    TeamToBasicTeamCommandConverter basicTeamConverter;

    @Mock
    TeamToTeamCommandConverter teamConverter;

    @Mock
    UserTeamRepository userTeamRepository;

    @Mock
    TeamToCurrentTeamUserCommandConverter converter;

    @Mock
    TeamRepository teamRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UniversityCommandToUniversityConverter universityConverter;

    @InjectMocks
    TeamServiceImpl teamService;

    @Captor
    ArgumentCaptor<Team> teamArgumentCaptor;

    @Test
    void fetchCurrentUserTeams() {
        //given
        List<Team> teams = List.of(
            Team.builder().id(TEAM_ID).name(TEAM_NAME).build()
        );
        given(converter.convert(any(Team.class)))
                .willReturn(CurrentTeamUserCommand.builder()
                        .teamId(TEAM_ID)
                        .name(TEAM_NAME)
                        .build());
        given(teamRepository.findUserTeams(anyString())).willReturn(teams);

        //when
        List<CurrentTeamUserCommand> currentTeamCommands = teamService.fetchCurrentUserTeams("johndoe@at.com");

        //then
        assertNotNull(currentTeamCommands);
        assertThat(currentTeamCommands).hasSize(1);
        verify(converter, times(1)).convert(any(Team.class));
        verify(teamRepository, times(1)).findUserTeams(anyString());
        verifyNoMoreInteractions(converter, teamRepository);
    }

    @Test
    void isUserInTeamActiveFalse() {
        //given
        
        //when
        boolean isActive = teamService.isUserInTeamActive(1L, "johny@bravo.eu");

        //then
        assertFalse(isActive);
    }

    @Test
    void isUserInTeamActiveTrue() {
        //given
        Optional<UserTeam> teamOptional = Optional.of(UserTeam.builder().id(TEAM_ID).build());
        given(teamRepository.findActualMembersByTeamId(anyLong(), anyString())).willReturn(teamOptional);

        //when
        boolean isActive = teamService.isUserInTeamActive(1L, "johny@bravo.eu");

        //then
        assertTrue(isActive);
    }

    @Test
    void testFindByIdFound() {
        //given
        given(teamRepository.findById(any())).willReturn(Optional.of(Team.builder()
                .id(TEAM_ID)
                .name(TEAM_NAME)
                .build()));

        //when
        Team team = teamService.findById(TEAM_ID);

        //then
        assertNotNull(team);
        assertEquals(TEAM_ID, team.getId());
        assertEquals(TEAM_NAME, team.getName());
    }

    @Test
    void testFindByIdNotFound() {
        //given
        given(teamRepository.findById(any())).willReturn(Optional.empty());

        //when
        Team team = teamService.findById(10000L);

        //then
        assertNull(team);
    }

    @Test
    void changeUserStatusNewStatus() {
        //given
        given(teamRepository.findActualMembersByTeamId(anyLong(), anyString()))
                .willReturn(Optional.of(UserTeam.builder().status(UserTeamStatus.MEMBER).build()));

        //when
        teamService.changeUserStatus(1L, "test@test.com", UserTeamStatus.ADMIN);

        //then
        verify(userTeamRepository, times(2)).save(any());
        verify(teamRepository).findActualMembersByTeamId(anyLong(), anyString());
        verifyNoMoreInteractions(teamRepository);
        verifyNoMoreInteractions(userTeamRepository);
    }

    @Test
    void changeUserStatusSame() {
        //given
        given(teamRepository.findActualMembersByTeamId(anyLong(), anyString()))
                .willReturn(Optional.of(UserTeam.builder().status(UserTeamStatus.MEMBER).build()));

        //when
        teamService.changeUserStatus(1L, "test@test.com", UserTeamStatus.MEMBER);

        //then
        verify(teamRepository).findActualMembersByTeamId(anyLong(), anyString());
        verifyNoMoreInteractions(teamRepository);
        verifyNoInteractions(userTeamRepository);
    }

    @Test
    void testFindCommandByIdEmpty() {
        //given
        given(teamRepository.fetchSingleTeamInformation(any())).willReturn(Optional.empty());

        //when
        TeamCommand teamCommand = teamService.findCommandById(1L);

        //then
        assertNull(teamCommand);
        verify(teamRepository).fetchSingleTeamInformation(any());
        verifyNoMoreInteractions(teamRepository);
        verifyNoInteractions(teamConverter);
    }

    @Test
    void testFindCommandByIdFound() {
        //given
        given(teamRepository.fetchSingleTeamInformation(any()))
                .willReturn(Optional.of(Team.builder().id(150L).build()));
        TeamCommand teamCommand = new TeamCommand();
        teamCommand.setId(150L);
        given(teamConverter.convert(any())).willReturn(teamCommand);

        //when
        TeamCommand returnedCommand = teamService.findCommandById(150L);

        //then
        assertNotNull(returnedCommand);
        verify(teamRepository).fetchSingleTeamInformation(any());
        verify(teamConverter).convert(any());
        verifyNoMoreInteractions(teamRepository);
        verifyNoMoreInteractions(teamConverter);
    }

    void testExistByTeamIdFound() {
        given(teamRepository.findById(any()))
                .willReturn(Optional.of(Team.builder().id(10L).build()));

        boolean exists = teamService.existByTeamId(10L);

        assertTrue(exists);
    }

    void testExistByTeamIdNotFound() {
        given(teamRepository.findById(any()))
                .willReturn(Optional.empty());

        boolean exists = teamService.existByTeamId(10L);

        assertFalse(exists);
    }

    @Test
    void teamUpdateThrowsException() {
        given(teamRepository.findById(any())).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> teamService.update(10L, TeamCommand.builder().build()));
    }

    @Test
    void testUpdateSuccess() {
        LocalDateTime timestamp = LocalDateTime.now();
        given(teamRepository.findById(anyLong()))
                .willReturn(Optional.of(
                        Team.builder()
                                .id(10L)
                                .name(TEAM_NAME)
                                .timeCreated(timestamp)
                                .build()));
        given(teamRepository.save(any()))
                .willReturn(Team.builder()
                        .id(10L)
                        .name(TEAM_UPDATED_NAME)
                        .timeCreated(timestamp)
                        .build());
        given(basicTeamConverter.convert(any()))
                .willReturn(TeamCommand.builder()
                        .id(10L)
                        .name(TEAM_UPDATED_NAME)
                        .build());

        BasicTeamCommand command = teamService.update(10L, TeamCommand.builder()
                .name(TEAM_UPDATED_NAME)
                .universityCommand(UniversityCommand.builder().build())
                .build());

        verify(teamRepository).findById(any());
        verify(teamRepository).save(teamArgumentCaptor.capture());
        Team team = teamArgumentCaptor.getValue();
        assertNotNull(team);
        assertEquals(TEAM_UPDATED_NAME, team.getName());
        verifyNoMoreInteractions(teamRepository);
        verify(basicTeamConverter).convert(any());
        verifyNoMoreInteractions(basicTeamConverter);
        verify(universityConverter).convert(any());
        verifyNoMoreInteractions(universityConverter);
    }

    @Test
    void testCreate() {
        LocalDateTime timestamp = LocalDateTime.now();
        User user = User.builder()
                .id(100L)
                .name("Test name")
                .surname("Test surname")
                .email(USER_EMAIL)
                .build();
        BasicTeamCommand teamCommand = BasicTeamCommand.builder()
                .name(TEAM_NAME)
                .timeCreated(timestamp)
                .universityCommand(UniversityCommand.builder().id(10L).build())
                .build();
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(universityConverter.convert(any())).willReturn(University.builder().id(10L).build());

        BasicTeamCommand basicTeamCommand = teamService.createTeam(teamCommand, USER_EMAIL);

        verify(userRepository).findByEmail(anyString());
        verify(teamRepository).save(teamArgumentCaptor.capture());
        verify(basicTeamConverter).convert(any());
        verify(universityConverter).convert(any());
        Team team = teamArgumentCaptor.getValue();
        assertNotNull(team);
        assertEquals(TEAM_NAME, team.getName());
        verify(userTeamRepository).save(any());
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(teamRepository);
        verifyNoMoreInteractions(userTeamRepository);
    }
}