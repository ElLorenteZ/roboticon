package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.CompetitionCommand;
import io.lorentez.roboticon.commands.CompetitionTypeCommand;
import io.lorentez.roboticon.commands.TournamentCommand;
import io.lorentez.roboticon.model.ScoreType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class TournamentControllerTestIT extends BaseIT{

    public static final String TOURNAMENT1_NAME = "Tournament 1 Name";
    public static final LocalDate TOURNAMENT1_DATESTART = LocalDate.now().plusMonths(1);
    public static final LocalDate TOURNAMENT1_DATEEND = LocalDate.now().plusMonths(1).plusDays(1);

    private static final String TEAM1_MEMBER_EMAIL = "krystian.barka@test.pl"; //User ID: 43

    @Test
    void testFindAllTournamentsAnonymous() throws Exception {
        mockMvc.perform(get("/api/v1/tournaments").with(anonymous()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testFindAllTournamentsAnonymousSlash() throws Exception {
        mockMvc.perform(get("/api/v1/tournaments/").with(anonymous()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testFindAllTournamentsAnonymousKeyword() throws Exception {
        mockMvc.perform(get("/api/v1/tournaments?keyword=robocomp").with(anonymous()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testFindAllTournamentsAnonymousKeywordEmpty() throws Exception {
        mockMvc.perform(get("/api/v1/tournaments?keyword=").with(anonymous()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testFindTournamentByIdAnonymous() throws Exception {
        mockMvc.perform(get("/api/v1/tournaments/1").with(anonymous()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testFindTournamentByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/tournaments/10000000000"))
                .andExpect(status().isNotFound());
    }



    @Test
    void testTournamentSaveUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/tournaments"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testTournamentSaveForbidden() throws Exception {
        TournamentCommand tournamentCommand = getSampleTournament();
        String token = getToken(TEAM1_MEMBER_EMAIL,"testtest");

        mockMvc.perform(post("/api/v1/tournaments")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tournamentCommand)))
                .andExpect(status().isForbidden());

    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testTournamentSaveAccepted() throws Exception {
        TournamentCommand tournamentCommand = getSampleTournament();
        String token = getGlobalAdminToken();

        mockMvc.perform(post("/api/v1/tournaments")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tournamentCommand)))
                .andExpect(status().isCreated());
    }

    @Test
    void testTournamentEditUnauthorized() throws Exception {
        mockMvc.perform(put("/api/v1/tournament/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testTournamentEditForbidden() throws Exception {
        TournamentCommand tournament = getSampleTournament();
        String token = getToken(TEAM1_MEMBER_EMAIL, "testtest");
        mockMvc.perform(put("/api/v1/tournaments/1")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tournament)))
                .andExpect(status().isForbidden());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testTournamentEditSuccess() throws Exception {
        TournamentCommand tournament = getSampleTournament();
        tournament.setId(1L);
        String token = getGlobalAdminToken();
        String content = objectMapper.writeValueAsString(tournament);
        mockMvc.perform(put("/api/v1/tournaments/1")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isNoContent());
    }

    private TournamentCommand getSampleTournament(){
        CompetitionTypeCommand lineFollowerType = CompetitionTypeCommand.builder()
                .id(1L)
                .type("Line Follower Standard")
                .scoreType(ScoreType.MIN_TIME)
                .build();
        CompetitionTypeCommand sumoType = CompetitionTypeCommand.builder()
                .id(2L)
                .type("Sumo Standard")
                .scoreType(ScoreType.MAX_POINTS)
                .build();
        TournamentCommand tournamentCommand = TournamentCommand.builder()
                .name(TOURNAMENT1_NAME)
                .dateStart(TOURNAMENT1_DATESTART)
                .dateEnd(TOURNAMENT1_DATEEND)
                .build();
        tournamentCommand.getCompetitions().add(
                CompetitionCommand.builder()
                        .name("Competition 1")
                        .description("Test competition")
                        .competitionType(lineFollowerType)
                        .build());
        tournamentCommand.getCompetitions().add(
                CompetitionCommand.builder()
                        .name("Competition 2")
                        .description("Test competition")
                        .competitionType(sumoType)
                        .build());
        return tournamentCommand;
    }
}
