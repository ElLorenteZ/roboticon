package io.lorentez.roboticon.commands;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.lorentez.roboticon.model.RegistrationCurrentStatus;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationCommand {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private RobotCommand robot;
    private CompetitionCommand competition;
    private List<BasicUserCommand> userCommands = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RegistrationCurrentStatus status;

}