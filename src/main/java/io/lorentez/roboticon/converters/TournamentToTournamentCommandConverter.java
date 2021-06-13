package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.TournamentCommand;
import io.lorentez.roboticon.model.Tournament;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class TournamentToTournamentCommandConverter implements Converter<Tournament, TournamentCommand> {

    private final CompetitionToCompetitionCommandConverter converter;

    public TournamentToTournamentCommandConverter(CompetitionToCompetitionCommandConverter converter) {
        this.converter = converter;
    }

    @Nullable
    @Synchronized
    @Override
    public TournamentCommand convert(Tournament tournament) {
        if (tournament == null){
            return null;
        }
        TournamentCommand tournamentCommand = new TournamentCommand();
        tournamentCommand.setId(tournament.getId());
        tournamentCommand.setName(tournament.getName());
        tournamentCommand.setDateStart(tournament.getDateStart());
        tournamentCommand.setDateEnd(tournament.getDateEnd());
        tournament.getCompetitions()
                .stream()
                .forEach(competition ->
                        tournamentCommand.getCompetitions()
                                .add(converter.convert(competition))
                );
        return tournamentCommand;
    }
}
