package io.lorentez.roboticon.commands;

import io.lorentez.roboticon.model.CompetitionType;
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

}
