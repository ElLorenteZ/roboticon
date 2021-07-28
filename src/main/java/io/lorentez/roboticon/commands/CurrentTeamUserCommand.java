package io.lorentez.roboticon.commands;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime timeCreated;
    private UserTeamStatus currentStatus;

}
