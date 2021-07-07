package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.UserInTeamCommand;
import io.lorentez.roboticon.model.UserTeam;
import io.lorentez.roboticon.model.UserTeamStatus;
import io.lorentez.roboticon.model.security.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTeamToUserInTeamCommandConverterTest {

    public static final Long ID = 204L;
    public static final String EMAIL = "sample@email.pl";
    public static final String NAME = "Sample name";
    public static final String SURNAME = "Sample surname";
    public static final UserTeamStatus USER_TEAM_STATUS = UserTeamStatus.MEMBER;

    UserTeamToUserInTeamCommandConverter converter;

    @BeforeEach
    void setUp() {
        converter = new UserTeamToUserInTeamCommandConverter();
    }

    @Test
    void testNullObject() {
        //given

        //when
        UserInTeamCommand userInTeamCommand = converter.convert(null);

        //then
        assertNull(userInTeamCommand);
    }

    @Test
    void testEmptyObject() {
        //given
        UserTeam userTeam = new UserTeam();

        //when
        UserInTeamCommand userInTeamCommand = converter.convert(userTeam);

        //then
        assertNull(userInTeamCommand);
    }

    @Test
    void testFullObject() {
        //given
        UserTeam userTeam = UserTeam.builder()
                .user(User.builder()
                        .id(ID)
                        .name(NAME)
                        .surname(SURNAME)
                        .email(EMAIL)
                        .build())
                .status(USER_TEAM_STATUS)
                .build();

        //when
        UserInTeamCommand userInTeamCommand = converter.convert(userTeam);

        //then
        assertNotNull(userInTeamCommand);
        assertEquals(ID, userInTeamCommand.getId());
        assertEquals(NAME, userInTeamCommand.getName());
        assertEquals(SURNAME, userInTeamCommand.getSurname());
        assertEquals(EMAIL, userInTeamCommand.getEmail());
        assertEquals(USER_TEAM_STATUS, userInTeamCommand.getStatus());
    }
}