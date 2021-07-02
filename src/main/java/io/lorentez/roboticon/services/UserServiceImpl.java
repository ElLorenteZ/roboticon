package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.BasicUserCommand;
import io.lorentez.roboticon.model.security.PasswordResetToken;
import io.lorentez.roboticon.model.security.User;
import io.lorentez.roboticon.repositories.PasswordResetTokenRepository;
import io.lorentez.roboticon.repositories.UserRepository;
import io.lorentez.roboticon.security.RoboticonPasswordEncoderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Used to provide information about Users
// and do custom operations with credentials.

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder encoder;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public void createPasswordResetToken(User user, String token) {
        //assuming only one token can be valid at time
        //remove previous if exist
        passwordResetTokenRepository.findByUser(user)
                .ifPresent(previousToken -> {
                    log.info("Removing previous PasswordResetToken for user: " + user.getEmail());
                    passwordResetTokenRepository.delete(previousToken);
                });
        //create new token
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(passwordResetToken);
        log.info("New PasswordResetToken was created for user: " + user.getEmail());
    }

    @Override
    public PasswordResetToken getPasswordResetToken(String tokenString) {
        return passwordResetTokenRepository.findByToken(tokenString).orElse(null);
    }

    @Override
    public void setNewPassword(User user, String newPassword) {
        user.setPassword(encoder.encode(newPassword));
        user.setAccountNonExpired(Boolean.TRUE);
        userRepository.save(user);
    }

    @Override
    public void removeToken(PasswordResetToken token) {
        passwordResetTokenRepository.delete(token);
    }
}
