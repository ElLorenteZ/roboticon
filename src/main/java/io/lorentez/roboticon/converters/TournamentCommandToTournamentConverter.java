package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.CompetitionCommand;
import io.lorentez.roboticon.commands.TournamentCommand;
import io.lorentez.roboticon.model.Competition;
import io.lorentez.roboticon.model.Tournament;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class TournamentCommandToTournamentConverter implements Converter<TournamentCommand, Tournament> {

    private final CompetitionCommandToCompetitionConverter converter;

    public TournamentCommandToTournamentConverter(CompetitionCommandToCompetitionConverter converter) {
        this.converter = converter;
    }

    @Nullable
    @Synchronized
    @Override
    public Tournament convert(TournamentCommand tournamentCommand) {
        if (tournamentCommand == null){
            return null;
        }
        Tournament tournament = new Tournament();
        tournament.setId(tournamentCommand.getId());
        tournament.setName(tournamentCommand.getName());
        tournament.setDateStart(tournamentCommand.getDateStart());
        tournament.setDateEnd(tournamentCommand.getDateEnd());
        tournamentCommand.getCompetitions()
                .stream()
                .forEach(competitionCommand -> {
                            Competition competition = converter.convert(competitionCommand);
                            competition.setTournament(tournament);
                            tournament.getCompetitions().add(competition);
                        }
                );
        return tournament;
    }

}
