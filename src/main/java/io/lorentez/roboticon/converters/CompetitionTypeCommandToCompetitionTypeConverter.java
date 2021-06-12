package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.CompetitionTypeCommand;
import io.lorentez.roboticon.model.CompetitionType;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CompetitionTypeCommandToCompetitionTypeConverter implements Converter<CompetitionTypeCommand, CompetitionType> {

    @Synchronized
    @Nullable
    @Override
    public CompetitionType convert(CompetitionTypeCommand competitionTypeCommand) {
        if (competitionTypeCommand == null){
            return null;
        }
        CompetitionType competitionType = new CompetitionType();
        competitionType.setId(competitionTypeCommand.getId());
        competitionType.setType(competitionTypeCommand.getType());
        competitionType.setScoreType(competitionTypeCommand.getScoreType());
        return competitionType;
    }
}
