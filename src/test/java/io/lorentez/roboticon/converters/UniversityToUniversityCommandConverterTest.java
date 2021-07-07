package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.UniversityCommand;
import io.lorentez.roboticon.model.University;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UniversityToUniversityCommandConverterTest {

    public static final Long ID = 1024L;
    public static final String NAME = "Sample name";
    public static final String ADDRESS_LINE_1 = "Sample Address Line 1";
    public static final String ADDRESS_LINE_2 = "Sample Address Line 2";
    public static final String ZIP_CODE = "00-000";
    public static final String PROVINCE = "Sample province";
    public static final String CITY = "Sample City";
    public static final String COUNTRY = "Sample Country";

    UniversityToUniversityCommandConverter converter;

    @BeforeEach
    void setUp() {
        converter = new UniversityToUniversityCommandConverter();
    }

    @Test
    void testNullObject() {
        //given

        //when
        UniversityCommand universityCommand = converter.convert(null);

        //then
        assertNull(universityCommand);
    }

    @Test
    void testEmptyObject() {
        //given
        University university = University.builder().build();

        //when
        UniversityCommand universityCommand = converter.convert(university);

        //then
        assertNotNull(universityCommand);
    }

    @Test
    void testFullObject() {
        //given
        University university = University.builder()
                .id(ID)
                .name(NAME)
                .addressLine1(ADDRESS_LINE_1)
                .addressLine2(ADDRESS_LINE_2)
                .zipCode(ZIP_CODE)
                .province(PROVINCE)
                .city(CITY)
                .country(COUNTRY)
                .build();

        //when
        UniversityCommand universityCommand = converter.convert(university);

        //then
        assertNotNull(universityCommand);
        assertEquals(ID, universityCommand.getId());
        assertEquals(NAME, universityCommand.getName());
        assertEquals(ADDRESS_LINE_1, universityCommand.getAddressLine1());
        assertEquals(ADDRESS_LINE_2, universityCommand.getAddressLine2());
        assertEquals(ZIP_CODE, universityCommand.getZipCode());
        assertEquals(PROVINCE, universityCommand.getProvince());
        assertEquals(CITY, universityCommand.getCity());
        assertEquals(COUNTRY, universityCommand.getCountry());
    }
}
