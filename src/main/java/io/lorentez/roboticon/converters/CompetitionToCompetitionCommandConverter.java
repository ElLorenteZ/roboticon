package io.lorentez.roboticon.converters;


import io.lorentez.roboticon.commands.CompetitionCommand;
import io.lorentez.roboticon.model.Competition;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CompetitionToCompetitionCommandConverter implements Converter<Competition, CompetitionCommand> {

    private final CompetitionTypeToCompetitionTypeCommandConverter competitionTypeConverter;

    public CompetitionToCompetitionCommandConverter(CompetitionTypeToCompetitionTypeCommandConverter converter) {
        this.competitionTypeConverter = converter;
    }

    @Synchronized
    @Nullable
    @Override
    public CompetitionCommand convert(Competition competition) {
        if(competition == null) {
            return null;
        }
        CompetitionCommand competitionCommand = new CompetitionCommand();
        competitionCommand.setId(competition.getId());
        competitionCommand.setName(competition.getName());
        competitionCommand.setDescription(competition.getDescription());
        competitionCommand.setCompetitionType(competitionTypeConverter.convert(competition.getCompetitionType()));
        if(competition.getRegistrations() != null){
            competitionCommand.setRegistrationsCounter(competition.getRegistrations().size());
        }
        else{
            competitionCommand.setRegistrationsCounter(0);
        }
        return competitionCommand;
    }
}
