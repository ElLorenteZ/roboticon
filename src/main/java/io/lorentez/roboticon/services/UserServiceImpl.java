package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.BasicUserCommand;
import io.lorentez.roboticon.commands.ChangePasswordCredentials;
import io.lorentez.roboticon.commands.UserRegisterCommand;
import io.lorentez.roboticon.converters.UserToBasicUserCommandConverter;
import io.lorentez.roboticon.model.security.PasswordResetToken;
import io.lorentez.roboticon.model.security.Role;
import io.lorentez.roboticon.model.security.User;
import io.lorentez.roboticon.repositories.PasswordResetTokenRepository;
import io.lorentez.roboticon.repositories.RoleRepository;
import io.lorentez.roboticon.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

// Used to provide information about Users
// and do custom operations with credentials.

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserToBasicUserCommandConverter converter;
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

    @Override
    public BasicUserCommand changeUserDetails(Long userId, BasicUserCommand newUserData) {
        log.info("Attempt of update data of user with id: " + userId.toString());
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()){
            User updatedUser = userOptional.get();
            updatedUser.setName(newUserData.getName());
            updatedUser.setSurname(newUserData.getSurname());
            updatedUser.setEmail(newUserData.getEmail());
            updatedUser = userRepository.save(updatedUser);
            return converter.convert(updatedUser);
        }
        else {
            log.warn("User with id: " + userId.toString() + " was not found!");
            throw new NoSuchElementException("User with id: " + userId.toString() + " was not found!");
        }
    }

    @Override
    public void changeUserPassword(User user, ChangePasswordCredentials passwordCredentials) throws IllegalAccessException {
        if (passwordEncoder.matches(passwordCredentials.getCurrentPassword(), user.getPassword())){
            user.setPassword(passwordEncoder.encode(passwordCredentials.getNewPassword()));
            userRepository.save(user);
        }
        else {
            log.info("Attempt of update password user: " + user.getEmail() + " with wrong password!");
            throw new IllegalAccessException("Current password is not correct!");
        }
    }

    @Override
    public void registerUser(UserRegisterCommand command) {
        if(!userRepository.existsByEmail(command.getEmail())){
            User user = User.builder()
                    .name(command.getName())
                    .surname(command.getSurname())
                    .email(command.getEmail())
                    .password(passwordEncoder.encode(command.getPassword()))
                    .build();
            Role userRole = roleRepository.findByName("USER").orElseThrow();
            user.grantRole(userRole);
            user = userRepository.save(user);
        }
        else{
            log.info("Attempt of creating user with email which is currently in database: " + command.getEmail());
            throw new IllegalArgumentException();
        }
    }
}
