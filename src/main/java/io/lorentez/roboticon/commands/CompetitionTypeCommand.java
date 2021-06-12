package io.lorentez.roboticon.commands;

import io.lorentez.roboticon.model.ScoreType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitionTypeCommand {

    private Long id;
    private String type;
    private ScoreType scoreType;

}
