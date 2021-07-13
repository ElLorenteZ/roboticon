package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.BasicUserCommand;
import io.lorentez.roboticon.converters.UserToBasicUserCommandConverter;
import io.lorentez.roboticon.model.security.PasswordResetToken;
import io.lorentez.roboticon.model.security.User;
import io.lorentez.roboticon.repositories.PasswordResetTokenRepository;
import io.lorentez.roboticon.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    public static final Long USER_ID = 5L;
    public static final String USER_EMAIL = "test@test.pl";
    public static final String USER_PASSWORD = "{noop}nomatter";
    public static final String USER_NAME = "John";
    public static final String USER_SURNAME = "Doe";

    public static final String SAMPLE_TOKEN = "Test token";

    @Captor
    ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

    @InjectMocks
    UserServiceImpl service;

    @Mock
    UserToBasicUserCommandConverter converter;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    void findByEmailNotNull() {
        //given
        User givenUser = User.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .surname(USER_SURNAME)
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();
        given(userRepository.findByEmail(anyString())).willReturn(Optional.ofNullable(givenUser));

        //when
        User returnedUser = service.findByEmail(USER_EMAIL);

        assertNotNull(returnedUser);
        assertEquals(USER_ID, returnedUser.getId());
        assertEquals(USER_NAME, returnedUser.getName());
        assertEquals(USER_SURNAME, returnedUser.getSurname());
        assertEquals(USER_EMAIL, returnedUser.getEmail());
    }

    @Test
    void createPasswordResetToken() {
        //given
        User user = User.builder().id(1L).build();
        String token = "Test";

        //when
        service.createPasswordResetToken(user, token);

        //then
        verify(passwordResetTokenRepository, times(1)).save(any());
        verify(passwordResetTokenRepository, times(1)).findByUser(any());
        verifyNoMoreInteractions(passwordResetTokenRepository);
        verifyNoInteractions(userRepository);
    }

    @Test
    void getPasswordResetTokenSuccess() {
        //given
        User user = User.builder().id(USER_ID).build();
        PasswordResetToken passwordResetToken = new PasswordResetToken(SAMPLE_TOKEN, user);
        given(passwordResetTokenRepository.findByToken(any())).willReturn(Optional.of(passwordResetToken));

        //when
        PasswordResetToken returnedPasswordResetToken = service.getPasswordResetToken(SAMPLE_TOKEN);

        //then
        assertNotNull(returnedPasswordResetToken);
        assertEquals(SAMPLE_TOKEN, returnedPasswordResetToken.getToken());
        assertEquals(USER_ID, returnedPasswordResetToken.getUser().getId());
    }

    @Test
    void getPasswordResetTokenNull() {
        //given
        given(passwordResetTokenRepository.findByToken(any())).willReturn(Optional.empty());

        //when
        PasswordResetToken passwordResetToken = service.getPasswordResetToken(SAMPLE_TOKEN);

        //then
        assertNull(passwordResetToken);
    }

    @Test
    void setNewPasswordEncoder() {
        //given
        User user = User.builder().id(1L).build();
        String newPassword = "TestTest123";

        //when
        service.setNewPassword(user, newPassword);

        //then
        verify(userRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(any());
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(passwordEncoder);
    }

    @Test
    void removeUsedToken() {
        //given

        //when
        service.removeToken(new PasswordResetToken("Test", User.builder().build()));

        //then
        verify(passwordResetTokenRepository, times(1)).delete(any());
        verifyNoMoreInteractions(passwordResetTokenRepository);
        verifyNoInteractions(userRepository);
    }

    @Test
    void testUserDetailsUpdate() {
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(User.builder()
                    .id(USER_ID)
                    .email("Old email")
                    .name("Old name")
                    .surname("Old surname")
                    .build()));
        BasicUserCommand returnedUserCommand = new BasicUserCommand(USER_ID,
                USER_NAME,
                USER_SURNAME,
                USER_EMAIL);
        given(converter.convert(any())).willReturn(returnedUserCommand);

        BasicUserCommand updatedUser = service.changeUserDetails(USER_ID, returnedUserCommand);

        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();
        assertEquals(USER_ID, savedUser.getId());
        assertEquals(USER_NAME, savedUser.getName());
        assertEquals(USER_SURNAME, savedUser.getSurname());
        assertEquals(USER_EMAIL, savedUser.getEmail());
        verify(userRepository).findById(anyLong());
        verify(converter).convert(any());
    }

    @Test
    void changeUsersPasswordSuccess() throws IllegalAccessException {
        given(passwordEncoder.matches(any(), any())).willReturn(Boolean.TRUE);

        service.changeUserPassword(User.builder().build(),
                Map.of("currentPassword", "testtest", "newPassword", "testtest2"));

        verify(userRepository).save(any());
        verifyNoMoreInteractions(userRepository);
        verify(passwordEncoder).matches(any(), any());
        verify(passwordEncoder).encode(any());
        verifyNoMoreInteractions(passwordEncoder);
    }

    @Test
    void changeUsersPasswordFailure() {
        given(passwordEncoder.matches(any(), any())).willReturn(Boolean.FALSE);

        assertThrows(IllegalAccessException.class, () -> {
            service.changeUserPassword(User.builder().build(),
                    Map.of("currentPassword", "testtest", "newPassword", "testtest2"));
        });

        verify(passwordEncoder).matches(any(), any());
        verifyNoMoreInteractions(passwordEncoder);
        verifyNoInteractions(userRepository);
    }
}