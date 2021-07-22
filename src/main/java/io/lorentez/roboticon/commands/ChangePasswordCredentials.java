package io.lorentez.roboticon.commands;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordCredentials {

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;

}
