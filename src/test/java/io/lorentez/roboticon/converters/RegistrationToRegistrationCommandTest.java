package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.BasicUserCommand;
import io.lorentez.roboticon.commands.CompetitionCommand;
import io.lorentez.roboticon.commands.RegistrationCommand;
import io.lorentez.roboticon.commands.RobotCommand;
import io.lorentez.roboticon.model.*;
import io.lorentez.roboticon.model.security.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationToRegistrationCommandTest {

    @Mock
    RobotToRobotCommandConverter robotConverter;

    @Mock
    CompetitionToCompetitionCommandConverter competitionConverter;

    @Mock
    UserToBasicUserCommandConverter userConverter;

    @InjectMocks
    RegistrationToRegistrationCommandConverter converter;

    public static final Long REGISTRATION_ID = 15L;
    public static final Long ROBOT_ID = 50L;
    public static final Long COMPETITION_ID = 150L;
    public static final RegistrationCurrentStatus REGISTRATION_STATUS = RegistrationCurrentStatus.CONFIRMED;

    public static final Long USER_ID = 200L;
    public static final String USER_NAME = "John";
    public static final String USER_SURNAME = "Doe";
    public static final String USER_EMAIL = "johndoe@test.au";

    @Test
    void testNull() {
        //given

        //when
        RegistrationCommand command = converter.convert(null);

        //then
        assertNull(command);
        verifyNoInteractions(robotConverter);
        verifyNoInteractions(userConverter);
        verifyNoInteractions(competitionConverter);
    }

    @Test
    void testEmptyObject() {
        Registration registration = new Registration();

        assertThrows(NoSuchElementException.class, () -> {
            converter.convert(registration);
        });
    }

    @Test
    void testFullObject() {
        //given
        Registration registration = Registration.builder()
                .id(REGISTRATION_ID)
                .robot(Robot.builder().id(ROBOT_ID).build())
                .competition(Competition.builder().id(COMPETITION_ID).build())
                .statuses(Set.of(
                        RegistrationStatus.builder()
                                .status(RegistrationCurrentStatus.APPLIED)
                                .timeTo(LocalDateTime.now().minusDays(1))
                                .build(),
                        RegistrationStatus.builder()
                                .status(REGISTRATION_STATUS)
                                .build()))
                .users(Set.of(User.builder()
                        .id(USER_ID)
                        .name(USER_NAME)
                        .surname(USER_SURNAME)
                        .email(USER_EMAIL)
                        .build()))
                .build();
        BasicUserCommand userCommand = new BasicUserCommand(USER_ID, USER_NAME, USER_SURNAME, USER_EMAIL);
        given(userConverter.convert(any())).willReturn(userCommand);
        given(competitionConverter.convert(any())).willReturn(CompetitionCommand.builder().id(COMPETITION_ID).build());
        given(robotConverter.convert(any())).willReturn(RobotCommand.builder().id(ROBOT_ID).build());

        //when
        RegistrationCommand command = converter.convert(registration);

        //then
        assertNotNull(command);
        assertEquals(REGISTRATION_ID, command.getId());
        assertEquals(REGISTRATION_STATUS, command.getStatus());
        assertThat(command.getUserCommands()).hasSize(1);
        verify(userConverter).convert(any());
        verify(robotConverter).convert(any());
        verify(competitionConverter).convert(any());
        verifyNoMoreInteractions(userConverter);
        verifyNoMoreInteractions(robotConverter);
        verifyNoMoreInteractions(competitionConverter);
    }
}