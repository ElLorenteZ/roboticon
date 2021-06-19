package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.CurrentTeamUserCommand;
import io.lorentez.roboticon.converters.TeamToCurrentTeamUserCommandConverter;
import io.lorentez.roboticon.model.Team;
import io.lorentez.roboticon.repositories.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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
        given(teamRepository.findUserTeams(anyLong())).willReturn(teams);

        //when
        List<CurrentTeamUserCommand> currentTeamCommands = teamService.fetchCurrentUserTeams(1L);

        //then
        assertNotNull(currentTeamCommands);
        assertThat(currentTeamCommands).hasSize(1);
        verify(converter, times(1)).convert(any(Team.class));
        verify(teamRepository, times(1)).findUserTeams(anyLong());
        verifyNoMoreInteractions(converter, teamRepository);
    }
}