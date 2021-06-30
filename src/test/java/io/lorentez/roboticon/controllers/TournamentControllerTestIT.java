package io.lorentez.roboticon.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class TournamentControllerTestIT extends BaseIT{

    @Test
    void testFindAllTournamentsAnonymous() throws Exception {
        mockMvc.perform(get("/api/v1/tournaments").with(anonymous()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testFindTournamentByIdAnonymous() throws Exception {
        mockMvc.perform(get("/api/v1/tournaments/1").with(anonymous()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
