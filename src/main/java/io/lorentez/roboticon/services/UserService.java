package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.BasicUserCommand;
import io.lorentez.roboticon.model.security.PasswordResetToken;
import io.lorentez.roboticon.model.security.User;

public interface UserService {

    User findByEmail(String email);
    void createPasswordResetToken(User user, String token);
    PasswordResetToken getPasswordResetToken(String tokenString);
    void setNewPassword(User user, String newPassword);
    void removeToken(PasswordResetToken token);
}
