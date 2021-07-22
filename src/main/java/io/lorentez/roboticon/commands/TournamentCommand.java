package io.lorentez.roboticon.commands;

import lombok.*;

import javax.validation.constraints.NotNull;
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

    @NotNull
    private String name;

    @NotNull
    private LocalDate dateStart;

    @NotNull
    private LocalDate dateEnd;

    @Builder.Default
    private Set<CompetitionCommand> competitions = new HashSet<>();

}
