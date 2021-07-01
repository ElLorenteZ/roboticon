package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.security.commands.LoginCredentials;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class TeamControllerTestIT extends BaseIT{

    @Test
    void findTeamsOfAnonymousUser() throws Exception {
        mockMvc.perform(get("/api/v1/teams/user").with(anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findTeamsOfUser() throws Exception {
        String token = getSampleToken();
        mockMvc.perform(get("/api/v1/teams/user").header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
