package io.lorentez.roboticon.commands;

import io.lorentez.roboticon.model.RegistrationCurrentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationCurrentStatusCommand {
    private RegistrationCurrentStatus status;
}
