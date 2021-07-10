package io.lorentez.roboticon.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class RobotControllerTestIT extends BaseIT {

    private static final String TEAM1_ADMIN_EMAIL = "antoni.spawacz@test.pl"; //User ID: 45
    private static final String TEAM1_MEMBER_EMAIL = "krystian.barka@test.pl"; //User ID: 43
    private static final String TEAM1_OWNER_EMAIL = "janusz.iksinski@test.pl"; //User ID: 1
    public static final String TEAM1_INVITED_EMAIL = "karol.zurawski@test.pl";
    public static final String TEAM1_NOT_IN_TEAM_EMAIL = "klaudia.fasada@test.pl";

    public static final String ROBOT_NAME = "New robot name";

    public static final String ROBOT_UPDATE_NAME_JSON = "{ " +
            "\"name\" : \"" + ROBOT_NAME + "\" " +
            "}";

    @Test
    void testChangeRobotNameUnauthorized() throws Exception {
        mockMvc.perform(put("/api/v1/robots/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ROBOT_UPDATE_NAME_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testChangeRobotNameGlobalAdmin() throws Exception {
        String token = getGlobalAdminToken();
        mockMvc.perform(put("/api/v1/robots/1")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ROBOT_UPDATE_NAME_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(ROBOT_NAME)))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void testChangeRobotNameTeamAdmin() throws Exception {
        String token = getToken(TEAM1_ADMIN_EMAIL, "testtest");
        mockMvc.perform(put("/api/v1/robots/1")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ROBOT_UPDATE_NAME_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(ROBOT_NAME)))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void testChangeRobotNameTeamOwner() throws Exception {
        String token = getToken(TEAM1_OWNER_EMAIL, "testtest");
        mockMvc.perform(put("/api/v1/robots/1")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ROBOT_UPDATE_NAME_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(ROBOT_NAME)))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void testChangeRobotNameTeamMember() throws Exception {
        String token = getToken(TEAM1_MEMBER_EMAIL, "testtest");
        mockMvc.perform(put("/api/v1/robots/1")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ROBOT_UPDATE_NAME_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void testChangeRobotNameTeamInvited() throws Exception {
        String token = getToken(TEAM1_INVITED_EMAIL, "testtest");
        mockMvc.perform(put("/api/v1/robots/1")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ROBOT_UPDATE_NAME_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void testChangeRobotNameNotInTeam() throws Exception {
        String token = getToken(TEAM1_NOT_IN_TEAM_EMAIL, "testtest");
        mockMvc.perform(put("/api/v1/robots/1")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ROBOT_UPDATE_NAME_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void testListRobotsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/robots")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testListRobotsGlobalAdmin() throws Exception {
        String token = getGlobalAdminToken();
        mockMvc.perform(get("/api/v1/robots")
                .header(AUTHORIZATION_HEADER, token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    void testListRobotsUserRole() throws Exception {
        String token = getToken(TEAM1_OWNER_EMAIL, "testtest");
        mockMvc.perform(get("/api/v1/robots")
                .header(AUTHORIZATION_HEADER, token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}
