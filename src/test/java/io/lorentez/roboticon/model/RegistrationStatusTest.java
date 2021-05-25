package io.lorentez.roboticon.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationStatusTest {

    public static final Long ID = 1L;
    public static final RegistrationCurrentStatus STATUS = RegistrationCurrentStatus.APPLIED;
    public static final LocalDateTime TIME_FROM = LocalDateTime.now();

    @Test
    void builder() {
        //given

        //when
        RegistrationStatus registrationStatus = RegistrationStatus.builder()
                .id(ID)
                .status(STATUS)
                .timeFrom(TIME_FROM)
                .build();

        //then
        assertNull(registrationStatus.getTimeTo());
        assertEquals(ID, registrationStatus.getId());
        assertEquals(STATUS, registrationStatus.getStatus());
        assertEquals(TIME_FROM, registrationStatus.getTimeFrom());
    }
}