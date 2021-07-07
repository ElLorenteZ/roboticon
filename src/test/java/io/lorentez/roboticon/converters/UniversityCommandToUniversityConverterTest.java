package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.UniversityCommand;
import io.lorentez.roboticon.model.University;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UniversityCommandToUniversityConverterTest {

    public static final Long ID = 1024L;
    public static final String NAME = "Sample name";
    public static final String ADDRESS_LINE_1 = "Sample Address Line 1";
    public static final String ADDRESS_LINE_2 = "Sample Address Line 2";
    public static final String ZIP_CODE = "00-000";
    public static final String PROVINCE = "Sample province";
    public static final String CITY = "Sample City";
    public static final String COUNTRY = "Sample Country";

    UniversityCommandToUniversityConverter converter;

    @BeforeEach
    void setUp() {
        converter = new UniversityCommandToUniversityConverter();
    }

    @Test
    void testNullObject() {
        //given

        //when
        University university = converter.convert(null);

        //then
        assertNull(university);
    }

    @Test
    void testEmptyObject() {
        //given
        UniversityCommand command = UniversityCommand.builder().build();

        //when
        University university = converter.convert(command);

        //then
        assertNotNull(university);
    }

    @Test
    void testFullObject() {
        //given
        UniversityCommand universityCommand = UniversityCommand.builder()
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
        University university = converter.convert(universityCommand);

        //then
        assertNotNull(university);
        assertEquals(ID, university.getId());
        assertEquals(NAME, university.getName());
        assertEquals(ADDRESS_LINE_1, university.getAddressLine1());
        assertEquals(ADDRESS_LINE_2, university.getAddressLine2());
        assertEquals(ZIP_CODE, university.getZipCode());
        assertEquals(PROVINCE, university.getProvince());
        assertEquals(CITY, university.getCity());
        assertEquals(COUNTRY, university.getCountry());
    }
}