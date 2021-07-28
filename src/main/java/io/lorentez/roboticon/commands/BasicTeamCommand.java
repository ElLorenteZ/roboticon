package io.lorentez.roboticon.commands;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasicTeamCommand {

    private Long id;

    private String name;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime timeCreated;

    private UniversityCommand universityCommand;

}
