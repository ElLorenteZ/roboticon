package io.lorentez.roboticon.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UniversityTest {

    public static final Long ID = 1L;
    public static final String NAME = "Sample university";
    public static final String ADDRESS_LINE1 = "Main Street 1";
    public static final String ADDRESS_LINE2 = "";
    public static final String ZIPCODE = "00-000";
    public static final String CITY = "Las Nullas";
    public static final String PROVINCE = "Nullasca";
    public static final String COUNTRY = "Nulland";

    @Test
    void builder() {
        //given

        //when
        University university = University.builder()
                .id(ID)
                .name(NAME)
                .addressLine1(ADDRESS_LINE1)
                .addressLine2(ADDRESS_LINE2)
                .zipCode(ZIPCODE)
                .city(CITY)
                .province(PROVINCE)
                .country(COUNTRY)
                .build();

        //then
        assertEquals(ID, university.getId());
        assertEquals(NAME, university.getName());
        assertEquals(ADDRESS_LINE1, university.getAddressLine1());
        assertEquals(ADDRESS_LINE2, university.getAddressLine2());
        assertEquals(ZIPCODE, university.getZipCode());
        assertEquals(CITY, university.getCity());
        assertEquals(PROVINCE, university.getProvince());
        assertEquals(COUNTRY, university.getCountry());

        assertNotNull(university.getTeams());
        assertThat(university.getTeams()).hasSize(0);
    }
}