package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.BasicUserCommand;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class UserControllerTestIT extends BaseIT{

    private static final String EMAIL_USER_ID1 = "janusz.iksinski@test.pl";
    private static final String EMAIL_USER_ID3 = "tomasz.chomik@test.pl";

    public static final Long USER_ID = 1L;
    public static final String USER_NAME = "Jerzy";
    public static final String USER_SURNAME = "Kozak";
    public static final String USER_EMAIL = "jerzy.kozak@test.pl";

    @Test
    void testChangeUserDetailsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/users/1/update"))
                .andExpect(status().isUnauthorized());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testChangeUserDetailsGlobalAdmin() throws Exception {
        String token = getGlobalAdminToken();
        BasicUserCommand userCommand = new BasicUserCommand(USER_ID, USER_NAME, USER_SURNAME, USER_EMAIL);


        mockMvc.perform(put("/api/v1/users/1")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCommand))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(USER_ID.intValue())))
                .andExpect(jsonPath("$.name", is(USER_NAME)))
                .andExpect(jsonPath("$.surname", is(USER_SURNAME)))
                .andExpect(jsonPath("$.email", is(USER_EMAIL)));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testChangeDetailsUserSelf() throws Exception {
        String token = getToken(EMAIL_USER_ID1, "testtest");
        BasicUserCommand userCommand = new BasicUserCommand(USER_ID, USER_NAME, USER_SURNAME, USER_EMAIL);

        mockMvc.perform(put("/api/v1/users/1")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCommand))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(USER_ID.intValue())))
                .andExpect(jsonPath("$.name", is(USER_NAME)))
                .andExpect(jsonPath("$.surname", is(USER_SURNAME)))
                .andExpect(jsonPath("$.email", is(USER_EMAIL)));
    }

    @Test
    void testChangeUserDetailsOtherUser() throws Exception {
        String token = getToken(EMAIL_USER_ID3, "testtest");
        BasicUserCommand userCommand = new BasicUserCommand(USER_ID, USER_NAME, USER_SURNAME, USER_EMAIL);

        mockMvc.perform(put("/api/v1/users/1")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCommand))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void listUsersUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void listUsersAdmin() throws Exception {
        String token = getGlobalAdminToken();
        mockMvc.perform(get("/api/v1/users")
                .header(AUTHORIZATION_HEADER, token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void listUsersForbidden() throws Exception {
        String token = getToken(EMAIL_USER_ID1, "testtest");
        mockMvc.perform(get("/api/v1/users")
                .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isForbidden());
    }

}