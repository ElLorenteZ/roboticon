package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.BasicUserCommand;
import io.lorentez.roboticon.model.security.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class UserToBasicUserCommandConverterTest {

    public static final Long USER_ID = 12345L;
    public static final String USER_NAME = "Test Name";
    public static final String USER_SURNAME = "Test Surname";
    public static final String USER_EMAIL = "Test Email";

    UserToBasicUserCommandConverter converter;

    @BeforeEach
    void setUp() {
        converter = new UserToBasicUserCommandConverter();
    }

    @Test
    void convertNull() {
        //given

        //when
        BasicUserCommand command = converter.convert(null);

        //then
        assertNull(command);
    }

    @Test
    void convertEmpty() {
        //given
        User user = User.builder().build();

        //when
        BasicUserCommand userCommand = converter.convert(user);

        //then
        assertNotNull(userCommand);
        assertNull(userCommand.getId());
        assertNull(userCommand.getName());
        assertNull(userCommand.getSurname());
        assertNull(userCommand.getEmail());
    }

    @Test
    void convertFullObject() {
        //given
        User user = User.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .surname(USER_SURNAME)
                .email(USER_EMAIL)
                .build();

        //when
        BasicUserCommand userCommand = converter.convert(user);

        //then
        assertNotNull(userCommand);
        assertEquals(USER_ID, userCommand.getId());
        assertEquals(USER_NAME, userCommand.getName());
        assertEquals(USER_SURNAME, userCommand.getSurname());
        assertEquals(USER_EMAIL, userCommand.getEmail());
    }
}