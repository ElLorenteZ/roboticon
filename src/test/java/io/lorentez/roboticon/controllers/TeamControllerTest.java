package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.CurrentTeamUserCommand;
import io.lorentez.roboticon.model.UserTeamStatus;
import io.lorentez.roboticon.services.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// it was used before implementation of spring security
// can be safely deleted
@Disabled
@ExtendWith(MockitoExtension.class)
class TeamControllerTest {

    public static final String TEAM_NAME = "Mock team name";
    public static final LocalDateTime TIME_CREATED = LocalDateTime.now().minusMonths(1);
    public static final String UNIVERSITY_NAME = "Mock university";

    @Mock
    TeamService teamService;

    @InjectMocks
    TeamController teamController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(teamController).build();
    }



    @Test
    void controllerTest() throws Exception {
        given(teamService.fetchCurrentUserTeams(anyString())).willReturn(mockTeams());

        mockMvc.perform(get("/api/v1/teams/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].teamId", is(1)))
                .andExpect(jsonPath("$[0].name", is(TEAM_NAME)))
                .andExpect(jsonPath("$[0].currentStatus", is(UserTeamStatus.ADMIN.toString())))
                .andExpect(jsonPath("$[0].universityName", is(UNIVERSITY_NAME)))
                .andExpect(jsonPath("$[1]", not(hasItem("universityName"))));
    }

    private List<CurrentTeamUserCommand> mockTeams(){
        List<CurrentTeamUserCommand> teams = new ArrayList<>();
        teams.add(CurrentTeamUserCommand.builder()
                .teamId(1L)
                .name(TEAM_NAME)
                .currentStatus(UserTeamStatus.ADMIN)
                .timeCreated(TIME_CREATED)
                .universityName(UNIVERSITY_NAME)
                .build());
        teams.add(CurrentTeamUserCommand.builder()
                .teamId(2L)
                .name("Mock team 2")
                .currentStatus(UserTeamStatus.MEMBER)
                .timeCreated(TIME_CREATED)
                .build());
        return teams;
    }
}