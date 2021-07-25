package io.lorentez.roboticon.commands;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.lorentez.roboticon.model.RobotTeamStatus;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RobotCommand {

    private Long id;

    @NotBlank
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RobotTeamStatus status;

    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timeAdded;

    private BasicTeamCommand teamCommand;
}
