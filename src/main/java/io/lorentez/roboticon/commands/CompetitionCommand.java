package io.lorentez.roboticon.commands;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitionCommand {

    private Long id;
    private String name;
    private String description;
    private CompetitionTypeCommand competitionType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer registrationsCounter;

}
