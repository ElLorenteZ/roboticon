package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.BasicUserCommand;
import io.lorentez.roboticon.model.security.User;
import io.lorentez.roboticon.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    public static final String USER_EMAIL = "test@test.pl";
    public static final String USER_PASSWORD = "{noop}nomatter";
    public static final String USER_NAME = "John";
    public static final String USER_SURNAME = "Doe";

    @InjectMocks
    UserServiceImpl service;

    @Mock
    UserRepository userRepository;

    @Test
    void findByEmailNotNull() {
        //given
        User givenUser = User.builder()
                .name(USER_NAME)
                .surname(USER_SURNAME)
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();
        given(userRepository.findByEmail(anyString())).willReturn(Optional.ofNullable(givenUser));

        //when
        User returnedUser = service.findByEmail(USER_EMAIL);

        assertNotNull(returnedUser);
        assertEquals(USER_NAME, returnedUser.getName());
        assertEquals(USER_SURNAME, returnedUser.getSurname());
        assertEquals(USER_EMAIL, returnedUser.getEmail());
    }

    @Test
    void createPasswordResetToken() {
    }
}