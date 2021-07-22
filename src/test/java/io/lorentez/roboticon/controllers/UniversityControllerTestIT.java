package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.UniversityCommand;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UniversityControllerTestIT extends BaseIT{

    private static final String USER_EMAIL = "krystian.barka@test.pl"; //User ID: 43

    public static final String UNIVERSITY_NAME = "Sample University";
    public static final String ADDRESS_LINE1 = "sample address";
    public static final String ADDRESS_LINE2 = "";
    public static final String ZIP_CODE = "00-000";
    public static final String PROVINCE = "Samplasca";
    public static final String CITY = "Los Samplos";
    public static final String COUNTRY = "United Samples";

    @Test
    void testGetUniversityByIdUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/universities/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetUniversityByIdAnyAuthorized() throws Exception{
        String token = getToken(USER_EMAIL, "testtest");
        mockMvc.perform(get("/api/v1/universities/1")
                .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetAllUniversitiesUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/universities"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetAllUniversities() throws Exception {
        String token = getToken(USER_EMAIL, "testtest");
        mockMvc.perform(get("/api/v1/universities")
                .header(AUTHORIZATION_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testAddUniversityUnauthorized() throws Exception {
        UniversityCommand university = UniversityCommand.builder()
                .name(UNIVERSITY_NAME)
                .addressLine1(ADDRESS_LINE1)
                .addressLine2(ADDRESS_LINE2)
                .zipCode(ZIP_CODE)
                .province(PROVINCE)
                .city(CITY)
                .country(COUNTRY)
                .build();
        mockMvc.perform(post("/api/v1/universities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(university)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAddUniversityForbidden() throws Exception {
        String token = getToken(USER_EMAIL, "testtest");
        UniversityCommand university = UniversityCommand.builder()
                .name(UNIVERSITY_NAME)
                .addressLine1(ADDRESS_LINE1)
                .addressLine2(ADDRESS_LINE2)
                .zipCode(ZIP_CODE)
                .province(PROVINCE)
                .city(CITY)
                .country(COUNTRY)
                .build();
        mockMvc.perform(post("/api/v1/universities")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(university)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAddUniversitySuccess() throws Exception {
        String token = getGlobalAdminToken();
        UniversityCommand university = UniversityCommand.builder()
                .name(UNIVERSITY_NAME)
                .addressLine1(ADDRESS_LINE1)
                .addressLine2(ADDRESS_LINE2)
                .zipCode(ZIP_CODE)
                .province(PROVINCE)
                .city(CITY)
                .country(COUNTRY)
                .build();
        mockMvc.perform(post("/api/v1/universities")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(university)))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateUniversityUnauthorized() throws Exception {
        UniversityCommand university = UniversityCommand.builder()
                .name(UNIVERSITY_NAME)
                .addressLine1(ADDRESS_LINE1)
                .addressLine2(ADDRESS_LINE2)
                .zipCode(ZIP_CODE)
                .province(PROVINCE)
                .city(CITY)
                .country(COUNTRY)
                .build();
        mockMvc.perform(put("/api/v1/universities/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(university)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdateUniversityForbidden() throws Exception {
        String token = getToken(USER_EMAIL, "testtest");
        UniversityCommand university = UniversityCommand.builder()
                .name(UNIVERSITY_NAME)
                .addressLine1(ADDRESS_LINE1)
                .addressLine2(ADDRESS_LINE2)
                .zipCode(ZIP_CODE)
                .province(PROVINCE)
                .city(CITY)
                .country(COUNTRY)
                .build();
        mockMvc.perform(put("/api/v1/universities/1")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(university)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateUniversitySuccess() throws Exception {
        String token = getGlobalAdminToken();
        UniversityCommand university = UniversityCommand.builder()
                .name(UNIVERSITY_NAME)
                .addressLine1(ADDRESS_LINE1)
                .addressLine2(ADDRESS_LINE2)
                .zipCode(ZIP_CODE)
                .province(PROVINCE)
                .city(CITY)
                .country(COUNTRY)
                .build();
        mockMvc.perform(put("/api/v1/universities/1")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(university)))
                .andExpect(status().isNoContent());
    }
}