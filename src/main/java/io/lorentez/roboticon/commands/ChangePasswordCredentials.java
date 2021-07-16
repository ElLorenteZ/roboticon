package io.lorentez.roboticon.commands;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordCredentials {

    private String currentPassword;
    private String newPassword;

}
