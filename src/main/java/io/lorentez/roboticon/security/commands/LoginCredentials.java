package io.lorentez.roboticon.security.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginCredentials {

    private String email;
    private String password;

}
