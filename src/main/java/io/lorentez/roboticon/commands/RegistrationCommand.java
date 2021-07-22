package io.lorentez.roboticon.commands;

import io.lorentez.roboticon.model.RegistrationCurrentStatus;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationCommand {

    private Long id;
    private RobotCommand robot;
    private CompetitionCommand competition;
    private List<BasicUserCommand> userCommands = new ArrayList<>();
    private RegistrationCurrentStatus status;

}