package io.lorentez.roboticon.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationTest {

    public static final Long ID = 1L;
    public static final RegistrationCurrentStatus STATUS = RegistrationCurrentStatus.APPLIED;

    @Test
    void builder() {
        //given

        //when
        Registration registration = Registration.builder()
                .id(ID)
                .build();

        //then
        assertEquals(ID, registration.getId());
        assertNotNull(registration.getUsers());
        assertThat(registration.getUsers()).hasSize(0);
        assertNotNull(registration.getStatuses());
        assertThat(registration.getStatuses()).hasSize(0);
    }
}