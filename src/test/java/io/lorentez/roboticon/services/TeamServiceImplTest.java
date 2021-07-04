package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.CurrentTeamUserCommand;
import io.lorentez.roboticon.converters.TeamToCurrentTeamUserCommandConverter;
import io.lorentez.roboticon.model.Team;
import io.lorentez.roboticon.model.UserTeam;
import io.lorentez.roboticon.model.UserTeamStatus;
import io.lorentez.roboticon.repositories.TeamRepository;
import io.lorentez.roboticon.repositories.UserTeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Mock
    UserTeamRepository userTeamRepository;

    @Mock
    TeamToCurrentTeamUserCommandConverter converter;

    @Mock
    TeamRepository teamRepository;

    @InjectMocks
    TeamServiceImpl teamService;

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
}