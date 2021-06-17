package io.lorentez.roboticon.commands;

import io.lorentez.roboticon.model.UserTeamStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentTeamUserCommand {

    private Long teamId;
    private String name;
    private String universityName;
    private LocalDateTime timeCreated;
    private UserTeamStatus currentStatus;

}
