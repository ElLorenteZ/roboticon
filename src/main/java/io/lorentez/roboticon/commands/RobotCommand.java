package io.lorentez.roboticon.commands;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.lorentez.roboticon.model.RobotTeamStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RobotCommand {

    private Long id;
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RobotTeamStatus status;
    private LocalDateTime timeAdded;

}
