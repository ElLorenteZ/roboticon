package io.lorentez.roboticon.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    public static final Long ID = 1L;
    public static final String NAME = "John";
    public static final String SURNAME = "Doe";
    public static final String EMAIL = "johndoe@asdf.com";

    @Test
    void builder() {
        //given

        //when
        User user = User.builder()
                .id(ID)
                .name(NAME)
                .surname(SURNAME)
                .email(EMAIL)
                .build();

        //then
        assertEquals(ID, user.getId());
        assertEquals(NAME, user.getName());
        assertEquals(SURNAME, user.getSurname());
        assertEquals(EMAIL, user.getEmail());

        assertTrue(user.getAccountNonExpired());
        assertTrue(user.getCredentialsNonExpired());
        assertTrue(user.getAccountNonLocked());

        assertNotNull(user.getRegistrations());
        assertThat(user.getRegistrations()).hasSize(0);
        assertNotNull(user.getUserTeams());
        assertThat(user.getUserTeams()).hasSize(0);
    }
}