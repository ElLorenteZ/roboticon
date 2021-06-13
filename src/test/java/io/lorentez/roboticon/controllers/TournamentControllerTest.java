package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.CompetitionCommand;
import io.lorentez.roboticon.commands.CompetitionTypeCommand;
import io.lorentez.roboticon.commands.TournamentCommand;
import io.lorentez.roboticon.model.ScoreType;
import io.lorentez.roboticon.services.TournamentService;
import io.lorentez.roboticon.services.TournamentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class TournamentControllerTest {

    @Mock
    TournamentService service;

    @InjectMocks
    TournamentController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void findAll() throws Exception {
        given(service.findAll()).willReturn(mockTournaments());

        mockMvc.perform(get("/api/v1/tournaments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    private List<TournamentCommand> mockTournaments() {
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
        CompetitionTypeCommand micromouseType = CompetitionTypeCommand.builder()
                .id(3L)
                .type("Micromouse")
                .scoreType(ScoreType.MIN_TIME)
                .build();
        List<TournamentCommand> tournamentCommandList = new ArrayList<>();
        TournamentCommand tournament1 = TournamentCommand.builder()
                .id(1L)
                .name("Tournament 1 Name")
                .dateStart(LocalDate.now().plusMonths(1))
                .dateEnd(LocalDate.now().plusMonths(1).plusDays(1))
                .build();
        tournament1.getCompetitions().add(
                CompetitionCommand.builder()
                        .id(1L)
                        .name("Competition 1")
                        .description("Test competition")
                        .competitionType(lineFollowerType)
                        .build());
        tournament1.getCompetitions().add(
                CompetitionCommand.builder()
                        .id(2L)
                        .name("Competition 2")
                        .description("Test competition")
                        .competitionType(sumoType)
                        .build());
        TournamentCommand tournament2 = TournamentCommand.builder()
                .id(2L)
                .name("Tournament 2 Name")
                .dateStart(LocalDate.now().plusMonths(1))
                .dateEnd(LocalDate.now().plusMonths(1).plusDays(1))
                .build();
        tournament1.getCompetitions().add(
                CompetitionCommand.builder()
                        .id(3L)
                        .name("Competition 3")
                        .competitionType(lineFollowerType)
                        .description("Test competition")
                        .build());
        tournament1.getCompetitions().add(
                CompetitionCommand.builder()
                        .id(4L)
                        .name("Competition 4")
                        .competitionType(sumoType)
                        .description("Test competition")
                        .build());
        tournament1.getCompetitions().add(
                CompetitionCommand.builder()
                        .id(5L)
                        .name("Competition 5")
                        .competitionType(micromouseType)
                        .build());
        return tournamentCommandList;
    }
}