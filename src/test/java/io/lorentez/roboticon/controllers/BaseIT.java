package io.lorentez.roboticon.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.lorentez.roboticon.model.security.User;
import io.lorentez.roboticon.security.commands.LoginCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public abstract class BaseIT {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    protected String getSampleToken() throws Exception {
        return getToken("janusz.iksinski@test.pl", "testtest");
    }

    protected String getToken(String email, String password) throws Exception {
        LoginCredentials credentials = new LoginCredentials(email, password);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonCredentials =ow.writeValueAsString(credentials);
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login").content(jsonCredentials))
                .andExpect(status().isOk())
                .andReturn();
        return result.getResponse().getHeader(AUTHORIZATION_HEADER);
    }
}
