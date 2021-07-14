package io.lorentez.roboticon.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lorentez.roboticon.commands.UserRegisterCommand;
import io.lorentez.roboticon.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthControllerTestIT extends BaseIT{

    private static final String EMAIL_USER_ID1 = "janusz.iksinski@test.pl";
    private static final String EMAIL_USER_ID3 = "tomasz.chomik@test.pl";

    private static final String REGISTER_USER_NAME = "Johny";
    private static final String REGISTER_USER_SURNAME = "Montana";
    private static final String REGISTER_USER_EMAIL = "newuser@test.eu";
    private static final String REGISTER_USER_PASSWORD = "john1234";

    @Autowired
    UserRepository userRepository;

    @Test
    void testResetPassword() throws Exception {
        String noExistingEmail = "thisemaildoesntexistindb@nowhere.pl";
        mockMvc.perform(post("/api/v1/auth/resetPassword")
                .param("email", noExistingEmail)
                .with(anonymous()))
                .andExpect(status().isNotFound());
    }

    @Test
    @Rollback
    void testExistingPassword() throws Exception {
        String existingEmail = userRepository.findAll().iterator().next().getEmail();
        mockMvc.perform(post("/api/v1/auth/resetPassword")
                .param("email", existingEmail)
                .with(anonymous()))
                .andExpect(status().isNoContent());
    }


    @Test
    void testChangePasswordUnauthorized() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> credentials = new HashMap<>();
        credentials.put("currentPassword", "testtest");
        credentials.put("newPassword", "sedessedes");

        mockMvc.perform(post("/api/v1/auth/changePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isUnauthorized());
    }


    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testChangePasswordUser1() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String token = getToken(EMAIL_USER_ID1, "testtest");
        Map<String, String> credentials = new HashMap<>();
        credentials.put("currentPassword", "testtest");
        credentials.put("newPassword", "sedessedes");

        mockMvc.perform(post("/api/v1/auth/changePassword")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isNoContent());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testRegisterUser() throws Exception {
        UserRegisterCommand command = new UserRegisterCommand();
        command.setName(REGISTER_USER_NAME);
        command.setSurname(REGISTER_USER_SURNAME);
        command.setEmail(REGISTER_USER_EMAIL);
        command.setPassword(REGISTER_USER_PASSWORD);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNoContent());
    }
}