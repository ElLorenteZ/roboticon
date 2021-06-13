package io.lorentez.roboticon.commands;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TournamentCommand {

    private Long id;
    private String name;
    private LocalDate dateStart;
    private LocalDate dateEnd;

    @Builder.Default
    private Set<CompetitionCommand> competitions = new HashSet<>();

}
