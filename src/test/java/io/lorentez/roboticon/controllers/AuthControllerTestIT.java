package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthControllerTestIT extends BaseIT{

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
}