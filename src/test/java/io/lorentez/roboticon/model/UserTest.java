package io.lorentez.roboticon.model;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    public static final Long ID = 1L;
    public static final String NAME = "John";
    public static final String SURNAME = "Doe";
    public static final String EMAIL = "johndoe@asdf.com";

    @Test
    void builderEmptyCollection() {
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

    @Test
    void builderAndPopulateCollections() {
        //given
        Registration registration1 = Registration.builder().id(1L).build();
        Registration registration2 = Registration.builder().id(2L).build();

        UserTeam userTeam1 = UserTeam.builder().id(1L).build();
        UserTeam userTeam2 = UserTeam.builder().id(2L).build();

        //when
        User user = User.builder()
                .id(ID)
                .name(NAME)
                .surname(SURNAME)
                .email(EMAIL)
                .build();
        user.getUserTeams().addAll(Set.of(userTeam1, userTeam2));
        user.getRegistrations().addAll(Set.of(registration1, registration2));

        //then
        assertEquals(ID, user.getId());
        assertEquals(NAME, user.getName());
        assertEquals(SURNAME, user.getSurname());
        assertEquals(EMAIL, user.getEmail());

        assertTrue(user.getAccountNonExpired());
        assertTrue(user.getCredentialsNonExpired());
        assertTrue(user.getAccountNonLocked());

        assertNotNull(user.getRegistrations());
        assertThat(user.getRegistrations()).hasSize(2);
        assertNotNull(user.getUserTeams());
        assertThat(user.getUserTeams()).hasSize(2);
    }
}