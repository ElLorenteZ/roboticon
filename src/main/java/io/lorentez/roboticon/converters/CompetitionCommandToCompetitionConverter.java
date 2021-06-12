package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.CompetitionCommand;
import io.lorentez.roboticon.model.Competition;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CompetitionCommandToCompetitionConverter implements Converter<CompetitionCommand, Competition> {

    private final CompetitionTypeCommandToCompetitionTypeConverter converter;

    public CompetitionCommandToCompetitionConverter(CompetitionTypeCommandToCompetitionTypeConverter converter) {
        this.converter = converter;
    }

    @Synchronized
    @Nullable
    @Override
    public Competition convert(CompetitionCommand competitionCommand) {
        if (competitionCommand == null) {
            return null;
        }
        Competition competition = new Competition();
        competition.setId(competitionCommand.getId());
        competition.setName(competitionCommand.getName());
        competition.setDescription(competitionCommand.getDescription());
        competition.setCompetitionType(converter.convert(competitionCommand.getCompetitionType()));
        return competition;
    }
}
