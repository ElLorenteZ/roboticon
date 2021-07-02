package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.BasicUserCommand;
import io.lorentez.roboticon.model.security.User;

public interface UserService {

    User findByEmail(String email);
    void createPasswordResetToken(User user, String token);

}
