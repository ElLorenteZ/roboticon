package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.CompetitionCommand;
import io.lorentez.roboticon.commands.CompetitionTypeCommand;
import io.lorentez.roboticon.model.ScoreType;
import io.lorentez.roboticon.services.CompetitionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CompetitionControllerTest {

    public static final Long COMPETITION_ID = 1L;
    public static final String COMPETITION_NAME = "Competition name";
    public static final String COMPETITION_DESCRIPTION = "Competition description";
    public static final Integer COMPETITION_REGISTRATIONS_COUNTER = 0;

    public static final Long COMPETITION_TYPE_ID = 100L;
    public static final ScoreType COMPETITION_TYPE_SCORE_TYPE = ScoreType.MIN_POINTS;
    public static final String COMPETITION_TYPE_NAME = "Test competition";


    @Mock
    CompetitionService service;

    @InjectMocks
    CompetitionController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void findById() throws Exception {
        given(service.findById(anyLong())).willReturn(
                CompetitionCommand.builder()
                        .id(COMPETITION_ID)
                        .name(COMPETITION_NAME)
                        .competitionType(CompetitionTypeCommand.builder()
                                .id(COMPETITION_TYPE_ID)
                                .scoreType(COMPETITION_TYPE_SCORE_TYPE)
                                .type(COMPETITION_TYPE_NAME)
                                .build())
                        .description(COMPETITION_DESCRIPTION)
                        .registrationsCounter(COMPETITION_REGISTRATIONS_COUNTER)
                        .build());

        mockMvc.perform(get("/api/v1/competitions/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(COMPETITION_ID), Long.class))
                .andExpect(jsonPath("$.name", is(COMPETITION_NAME)))
                .andExpect(jsonPath("$.competitionType.id", is(COMPETITION_TYPE_ID), Long.class))
                .andExpect(jsonPath("$.competitionType.type", is(COMPETITION_TYPE_NAME)))
                .andExpect(jsonPath("$.competitionType.scoreType", is(COMPETITION_TYPE_SCORE_TYPE.toString())))
                .andExpect(jsonPath("$.description", is(COMPETITION_DESCRIPTION)))
                .andExpect(jsonPath("$.registrationsCounter", is(0)));
    }
}