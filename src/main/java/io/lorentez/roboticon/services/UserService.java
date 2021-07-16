package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.BasicUserCommand;
import io.lorentez.roboticon.commands.ChangePasswordCredentials;
import io.lorentez.roboticon.commands.UserRegisterCommand;
import io.lorentez.roboticon.model.security.PasswordResetToken;
import io.lorentez.roboticon.model.security.User;

import java.util.Map;

public interface UserService {

    User findByEmail(String email);

    void createPasswordResetToken(User user, String token);

    PasswordResetToken getPasswordResetToken(String tokenString);

    void setNewPassword(User user, String newPassword);

    void removeToken(PasswordResetToken token);

    BasicUserCommand changeUserDetails(Long userId, BasicUserCommand updatedUser);

    void changeUserPassword(User user, ChangePasswordCredentials passwordCredentials) throws IllegalAccessException;

    void registerUser(UserRegisterCommand command);
}
