package io.lorentez.roboticon.commands;

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
    private LocalDateTime timeAdded;

}
