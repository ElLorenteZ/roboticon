package io.lorentez.roboticon.security.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class LoginCredentials {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
