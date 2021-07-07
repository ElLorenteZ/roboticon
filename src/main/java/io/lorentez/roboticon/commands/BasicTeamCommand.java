package io.lorentez.roboticon.commands;

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
    private LocalDateTime timeCreated;
    private UniversityCommand universityCommand;

}
