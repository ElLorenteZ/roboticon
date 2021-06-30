package io.lorentez.roboticon.model;

import io.lorentez.roboticon.model.security.User;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationTest {

    public static final Long ID = 1L;
    public static final RegistrationCurrentStatus STATUS = RegistrationCurrentStatus.APPLIED;

    @Test
    void builderEmptyCollections() {
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

    @Test
    void builderAndPopulateCollections() {
        //given
        User user1 = User.builder().id(1L).build();
        User user2 = User.builder().id(2L).build();
        RegistrationStatus status1 = RegistrationStatus.builder().id(1L).build();
        RegistrationStatus status2 = RegistrationStatus.builder().id(2L).build();

        //when
        Registration registration = Registration.builder()
                .id(ID)
                .build();
        registration.getUsers().addAll(Set.of(user1, user2));
        registration.getStatuses().addAll(Set.of(status1, status2));

        //then
        assertEquals(ID, registration.getId());
        assertNotNull(registration.getUsers());
        assertThat(registration.getUsers()).hasSize(2);
        assertNotNull(registration.getStatuses());
        assertThat(registration.getStatuses()).hasSize(2);
    }
}