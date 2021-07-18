package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.BasicTeamCommand;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

import static org.hamcrest.core.Is.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class TeamControllerTestIT extends BaseIT{

    public static final String EMAIL_PARAM_NAME = "email";
    public static final String NEW_EMAIL = "thisissomenewemail@test.pl";
    public static final String TEAM_UPDATED_NAME = "Team updated name";

    private static final String TEAM1_ADMIN_EMAIL = "antoni.spawacz@test.pl"; //User ID: 45
    private static final String TEAM1_MEMBER_EMAIL = "krystian.barka@test.pl"; //User ID: 43
    private static final String TEAM1_OWNER_EMAIL = "janusz.iksinski@test.pl"; //User ID: 1
    public static final String TEAM1_INVITED_EMAIL = "karol.zurawski@test.pl";
    public static final String TEAM1_NOT_IN_TEAM_EMAIL = "klaudia.fasada@test.pl";

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

    @Test
    void testUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/teams/1/invite")
                .param(EMAIL_PARAM_NAME, NEW_EMAIL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testInviteUserNotInTeam() throws Exception {
        String token = getToken(TEAM1_NOT_IN_TEAM_EMAIL,"testtest");
        mockMvc.perform(post("/api/v1/teams/1/invite")
                .param(EMAIL_PARAM_NAME, NEW_EMAIL)
                .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isForbidden());
    }

    @Test
    void testInviteUserMemberInTeam() throws Exception {
        String token = getToken("krystian.barka@test.pl", "testtest");
        mockMvc.perform(post("/api/v1/teams/1/invite")
                .param(EMAIL_PARAM_NAME, NEW_EMAIL)
                .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isForbidden());
    }

    @Rollback
    @Test
    void testInviteGlobalAdmin() throws Exception {
        String token = this.getGlobalAdminToken();
        mockMvc.perform(post("/api/v1/teams/1/invite")
                .param(EMAIL_PARAM_NAME, "kacper.listkiewicz@test.pl")
                .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isNoContent());
    }

    @Rollback
    @Test
    void testOwnerInTeam() throws Exception {
        String token = getToken("janusz.iksinski@test.pl", "testtest");
        mockMvc.perform(post("/api/v1/teams/1/invite")
                .param(EMAIL_PARAM_NAME, TEAM1_NOT_IN_TEAM_EMAIL)
                .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isNoContent());
    }

    @Rollback
    @Test
    void testAdminInTeam() throws Exception {
        String token = getToken("antoni.spawacz@test.pl", "testtest");
        mockMvc.perform(post("/api/v1/teams/1/invite")
                .param(EMAIL_PARAM_NAME, "jacek.banderas@test.pl")
                .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isNoContent());
    }

    @Test
    void testAdminInTeamUserAlreadyInTeam() throws Exception {
        String token = getToken("antoni.spawacz@test.pl", "testtest");
        mockMvc.perform(post("/api/v1/teams/1/invite")
                .param(EMAIL_PARAM_NAME, "adam.spychacz@test.pl")
                .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testChangeStatusGlobalAdmin() throws Exception {
        String token = getGlobalAdminToken();
        String payload = "{ \"email\": \"adam.spychacz@test.pl\", \"status\": \"ADMIN\" }";
        mockMvc.perform(post("/api/v1/teams/1/status")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testChangeStatusAdminInTeamToOwner() throws Exception {
        String token = getToken(TEAM1_ADMIN_EMAIL, "testtest");
        String payload = "{ \"email\" : \"" + TEAM1_OWNER_EMAIL + "\", \"status\" : \"MEMBER\" }";
        mockMvc.perform(post("/api/v1/teams/1/status")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void testChangeStatusAdminInTeamToMember() throws Exception {
        String token = getToken(TEAM1_ADMIN_EMAIL, "testtest");
        String payload = "{ \"email\" : \"" + TEAM1_MEMBER_EMAIL + "\", \"status\" : \"MEMBER\" }";
        mockMvc.perform(post("/api/v1/teams/1/status")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testChangeStatusOwnerInTeamToAdmin() throws Exception {
        String token = getToken(TEAM1_OWNER_EMAIL, "testtest");
        String payload = "{ \"email\" : \"" + TEAM1_MEMBER_EMAIL + "\", \"status\" : \"MEMBER\" }";
        mockMvc.perform(post("/api/v1/teams/1/status")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testChangeStatusMemberDoAdmin() throws Exception {
        String token = getToken(TEAM1_MEMBER_EMAIL, "testtest");
        String payload = "{ \"email\" : \"" + TEAM1_MEMBER_EMAIL + "\", \"status\" : \"ADMIN\" }";
        mockMvc.perform(post("/api/v1/teams/1/status")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    void testChangeStatusMemberDoOwner() throws Exception {
        String token = getToken(TEAM1_MEMBER_EMAIL, "testtest");
        String payload = "{ \"email\" : \"" + TEAM1_MEMBER_EMAIL + "\", \"status\" : \"OWNER\" }";
        mockMvc.perform(post("/api/v1/teams/1/status")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    void testChangeStatusMemberAcceptInvitation() throws Exception {
        String token = getToken(TEAM1_INVITED_EMAIL, "testtest");
        String payload = "{ \"email\" : \"" + TEAM1_INVITED_EMAIL + "\", \"status\" : \"MEMBER\" }";
        mockMvc.perform(post("/api/v1/teams/1/status")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testChangeStatusMemberAcceptInvitationAdmin() throws Exception {
        String token = getToken(TEAM1_INVITED_EMAIL, "testtest");
        String payload = "{ \"email\" : \"" + TEAM1_MEMBER_EMAIL + "\", \"status\" : \"ADMIN\" }";
        mockMvc.perform(post("/api/v1/teams/1/status")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void testChangeStatusMemberAcceptInvitationOwner() throws Exception {
        String token = getToken(TEAM1_INVITED_EMAIL, "testtest");
        String payload = "{ \"email\" : \"" + TEAM1_MEMBER_EMAIL + "\", \"status\" : \"Owner\" }";
        mockMvc.perform(post("/api/v1/teams/1/status")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void testTeamDetailsViewUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/teams/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testTeamDetailsViewGlobalAdmin() throws Exception {
        String token = getGlobalAdminToken();
        mockMvc.perform(get("/api/v1/teams/1")
                .header(AUTHORIZATION_HEADER, token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testTeamDetailsViewTeamOwner() throws Exception {
        String token = getToken(TEAM1_OWNER_EMAIL, "testtest");
        mockMvc.perform(get("/api/v1/teams/1")
                .header(AUTHORIZATION_HEADER, token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testTeamDetailsViewTeamMember() throws Exception {
        String token = getToken(TEAM1_MEMBER_EMAIL, "testtest");
        mockMvc.perform(get("/api/v1/teams/1")
                .header(AUTHORIZATION_HEADER, token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testTeamDetailsViewTeamAdmin() throws Exception {
        String token = getToken(TEAM1_ADMIN_EMAIL, "testtest");
        mockMvc.perform(get("/api/v1/teams/1")
                .header(AUTHORIZATION_HEADER, token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testTeamDetailsViewTeamInvited() throws Exception {
        String token = getToken(TEAM1_INVITED_EMAIL, "testtest");
        mockMvc.perform(get("/api/v1/teams/1")
                .header(AUTHORIZATION_HEADER, token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void testTeamDetailsViewNotInTeam() throws Exception {
        String token = getToken(TEAM1_NOT_IN_TEAM_EMAIL, "testtest");
        mockMvc.perform(get("/api/v1/teams/1")
                .header(AUTHORIZATION_HEADER, token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void teamUpdateUnauthorized() throws Exception {
        mockMvc.perform(put("/api/v1/teams/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUpdatedBasicTeamCommand())))
                .andExpect(status().isUnauthorized());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void teamUpdateGlobalAdmin() throws Exception {
        String token = getGlobalAdminToken();
        mockMvc.perform(put("/api/v1/teams/1")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUpdatedBasicTeamCommand()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(TEAM_UPDATED_NAME)));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void teamUpdateTeamOwner() throws Exception {
        String token = getToken(TEAM1_OWNER_EMAIL, "testtest");
        mockMvc.perform(put("/api/v1/teams/1")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUpdatedBasicTeamCommand()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(TEAM_UPDATED_NAME)));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void teamUpdateTeamAdmin() throws Exception {
        String token = getToken(TEAM1_ADMIN_EMAIL, "testtest");
        mockMvc.perform(put("/api/v1/teams/1")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUpdatedBasicTeamCommand()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(TEAM_UPDATED_NAME)));
    }

    @Test
    void teamUpdateTeamMember() throws Exception {
        String token = getToken(TEAM1_MEMBER_EMAIL, "testtest");
        mockMvc.perform(put("/api/v1/teams/1")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUpdatedBasicTeamCommand())))
                .andExpect(status().isForbidden());
    }

    @Test
    void teamUpdateTeamInvited() throws Exception {
        String token = getToken(TEAM1_INVITED_EMAIL, "testtest");
        mockMvc.perform(put("/api/v1/teams/1")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUpdatedBasicTeamCommand())))
                .andExpect(status().isForbidden());
    }

    private BasicTeamCommand getUpdatedBasicTeamCommand(){
        return BasicTeamCommand.builder()
                .id(1L)
                .name(TEAM_UPDATED_NAME)
                .build();
    }
}
