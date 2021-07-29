package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.*;
import io.lorentez.roboticon.model.RegistrationCurrentStatus;
import io.lorentez.roboticon.model.RobotTeamStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
class RegistrationControllerTestIT extends BaseIT{

    private static final String TEAM1_ADMIN_EMAIL = "antoni.spawacz@test.pl"; //User ID: 45
    private static final String TEAM1_MEMBER_EMAIL = "krystian.barka@test.pl"; //User ID: 43
    private static final String TEAM1_OWNER_EMAIL = "janusz.iksinski@test.pl"; //User ID: 1
    public static final String TEAM1_INVITED_EMAIL = "karol.zurawski@test.pl";
    public static final String TEAM1_NOT_IN_TEAM_EMAIL = "klaudia.fasada@test.pl";

    RegistrationCommand registrationCommand;


    @Test
    void testListRegistrationsOfTeamForTournamentUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/registrations/tournaments/1/team/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testListRegistrationsOfTeamForTournamentGlobalAdmin() throws Exception {
        String token = getGlobalAdminToken();
        mockMvc.perform(get("/api/v1/registrations/tournaments/1/team/1")
                .header(AUTHORIZATION_HEADER, token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testListRegistrationsOfTeamForTournamentTeamOwner() throws Exception {
        String token = getToken(TEAM1_OWNER_EMAIL, "testtest");
        mockMvc.perform(get("/api/v1/registrations/tournaments/1/team/1")
                .header(AUTHORIZATION_HEADER, token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testListRegistrationsOfTeamForTournamentTeamAdmin() throws Exception {
        String token = getToken(TEAM1_ADMIN_EMAIL, "testtest");
        mockMvc.perform(get("/api/v1/registrations/tournaments/1/team/1")
                .header(AUTHORIZATION_HEADER, token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testListRegistrationsOfTeamForTournamentTeamMember() throws Exception {
        String token = getToken(TEAM1_MEMBER_EMAIL, "testtest");
        mockMvc.perform(get("/api/v1/registrations/tournaments/1/team/1")
                .header(AUTHORIZATION_HEADER, token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testListRegistrationsOfTeamForTournamentTeamInvited() throws Exception {
        String token = getToken(TEAM1_INVITED_EMAIL, "testtest");
        mockMvc.perform(get("/api/v1/registrations/tournaments/1/team/1")
                .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isForbidden());
    }


    @Test
    void testListRegistrationsOfTeamForTournamentNotInTeam() throws Exception {
        String token = getToken(TEAM1_NOT_IN_TEAM_EMAIL, "testtest");
        mockMvc.perform(get("/api/v1/registrations/tournaments/1/team/1")
                .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isForbidden());
    }

    @Test
    void testSetNewStatusRegistrationsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/registrations/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"status\" : \"CONFIRMED\" }"))
                .andExpect(status().isUnauthorized());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testSetNewStatusGlobalAdmin() throws Exception {
        String token = getGlobalAdminToken();
        mockMvc.perform(post("/api/v1/registrations/1/status")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"status\" : \"CONFIRMED\" }"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testSetNewStatusTeamOwner() throws Exception {
        String token = getToken(TEAM1_OWNER_EMAIL, "testtest");
        mockMvc.perform(post("/api/v1/registrations/1/status")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"status\" : \"CONFIRMED\" }"))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateRegistrationsNotAuthorized() throws Exception {
        RegistrationCommand registrationCommand = RegistrationCommand.builder()
                .robot(RobotCommand.builder().id(2L).build())
                .userCommands(List.of(
                        new BasicUserCommand(3L, "", "", ""),
                        new BasicUserCommand(43L, "", "", "")
                ))
                .build();
        mockMvc.perform(put("/api/v1/registrations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationCommand)))
                .andExpect(status().isUnauthorized());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void updateRegistrationGlobalAdmin() throws Exception {
        String token = getGlobalAdminToken();
        RegistrationCommand registrationCommand = RegistrationCommand.builder()
                .robot(RobotCommand.builder().id(2L).build())
                .userCommands(List.of(
                        new BasicUserCommand(3L, "", "", ""),
                        new BasicUserCommand(43L, "", "", "")
                ))
                .build();
        mockMvc.perform(put("/api/v1/registrations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationCommand))
                .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isNoContent());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void updateRegistrationTeamOwner() throws Exception {
        String token = getToken(TEAM1_OWNER_EMAIL, "testtest");
        RegistrationCommand registrationCommand = RegistrationCommand.builder()
                .id(1L)
                .competition(CompetitionCommand.builder().id(2L).build())
                .robot(RobotCommand.builder()
                        .id(2L)
                        .name("")
                        .teamCommand(new BasicTeamCommand(1L, "Test", null,null))
                        .build())
                .userCommands(List.of(
                        new BasicUserCommand(3L, "", "", ""),
                        new BasicUserCommand(43L, "", "", "")
                ))
                .status(RegistrationCurrentStatus.CONFIRMED)
                .build();
        String commandString = objectMapper.writeValueAsString(registrationCommand);
        mockMvc.perform(put("/api/v1/registrations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(commandString)
                .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isNoContent());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void updateRegistrationTeamAdmin() throws Exception {
        String token = getToken(TEAM1_ADMIN_EMAIL, "testtest");
        RegistrationCommand registrationCommand = RegistrationCommand.builder()
                .robot(RobotCommand.builder()
                        .id(2L)
                        .teamCommand(new BasicTeamCommand(1L, "Test", null,null))
                        .build())
                .userCommands(List.of(
                        new BasicUserCommand(3L, "", "", ""),
                        new BasicUserCommand(43L, "", "", "")
                ))
                .status(RegistrationCurrentStatus.CONFIRMED)
                .build();
        mockMvc.perform(put("/api/v1/registrations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationCommand))
                .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateRegistrationTeamOMember() throws Exception {
        String token = getToken(TEAM1_MEMBER_EMAIL, "testtest");
        RegistrationCommand registrationCommand = RegistrationCommand.builder()
                .robot(RobotCommand.builder().id(2L).build())
                .userCommands(List.of(
                        new BasicUserCommand(3L, "", "", ""),
                        new BasicUserCommand(43L, "", "", "")
                ))
                .status(RegistrationCurrentStatus.CONFIRMED)
                .build();
        mockMvc.perform(put("/api/v1/registrations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationCommand))
                .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isForbidden());
    }

    @Test
    void createRegistrationUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/registrations"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createRegistrationForbidden() throws Exception {
        String token = getToken("pawel.kuwert@test.pl", "testtest");
        mockMvc.perform(post("/api/v1/registrations")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getCreatedRegistrationCommand()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void createRegistrationNotInTeam() throws Exception {
        String token = getToken("lucjan.anatolinski@test.pl", "testtest");
        String content = objectMapper.writeValueAsString(getCreatedRegistrationCommand());
        mockMvc.perform(post("/api/v1/registrations")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void createRegistrationAdmin() throws Exception {
        String token = getToken("bartlomiej.wertowski@test.pl", "testtest");
        mockMvc.perform(post("/api/v1/registrations")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getCreatedRegistrationCommand()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void createRegistrationOwner() throws Exception {
        String token = getToken("bartosz.czarny@test.pl", "testtest");
        mockMvc.perform(post("/api/v1/registrations")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getCreatedRegistrationCommand()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void createRegistrationGlobalAdmin() throws Exception {
        String token = getGlobalAdminToken();
        mockMvc.perform(post("/api/v1/registrations")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getCreatedRegistrationCommand())))
                .andExpect(status().isCreated());
    }

    private RegistrationCommand getCreatedRegistrationCommand(){
        return RegistrationCommand.builder()
                .robot(RobotCommand.builder()
                        .id(16L)
                        .name("Dzik")
                        .status(RobotTeamStatus.OWNED)
                        .timeAdded(LocalDateTime.now())
                        .teamCommand(BasicTeamCommand.builder()
                                .id(6L)
                                .name("Kanto")
                                .timeCreated(LocalDateTime.now())
                                .universityCommand(UniversityCommand.builder()
                                        .id(3L)
                                        .name("Politechnika Wrocławska")
                                        .addressLine1("Wybrzeże Stanisława Wyspiańskiego 27")
                                        .addressLine2("")
                                        .zipCode("50-370")
                                        .city("Wrocław")
                                        .province("dolnośląskie")
                                        .country("Polska")
                                        .build())
                                .build())
                        .build())
                .competition(CompetitionCommand.builder()
                        .id(4L)
                        .name("Sumo Standard")
                        .description("Sumo - dwa roboty próbują zepchnąć się nawzajem z ringu (dohyo). " +
                                "Podczas walki nie można sterować robotem (poza włączeniem i wyłączeniem). " +
                                "Wielkość robota nie może przekroczyć 20 x 20cm (wysokość bez ograniczeń), " +
                                "waga do 3kg. Ring ma średnicę 149cm.")
                        .build())
                .userCommands(List.of(
                        new BasicUserCommand(16L, "Bartosz", "Czarny", "bartosz.czarny@test.pl"),
                        new BasicUserCommand(17L, "Bartłomiej", "Wertowski", "bartlomiej.wertowski@test.pl")
                ))
                .build();
    }

    @Test
    void testGetUserRegistrationsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/registrations/users/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetUserRegistrationsSelf() throws Exception {
        String token = getToken(TEAM1_OWNER_EMAIL, "testtest");
        mockMvc.perform(get("/api/v1/registrations/users/1")
                .header(AUTHORIZATION_HEADER, token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetUserRegistrationOther() throws Exception {
        String token = getToken(TEAM1_MEMBER_EMAIL, "testtest");
        mockMvc.perform(get("/api/v1/registrations/users/1")
                .header(AUTHORIZATION_HEADER, token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetUserRegistrationAdmin() throws Exception {
        String token = getGlobalAdminToken();
        mockMvc.perform(get("/api/v1/registrations/users/1")
                .header(AUTHORIZATION_HEADER, token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
