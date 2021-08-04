package io.lorentez.roboticon.commands;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UniversityCommand university;

}
