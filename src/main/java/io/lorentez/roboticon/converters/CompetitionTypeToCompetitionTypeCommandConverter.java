package io.lorentez.roboticon.converters;


import com.sun.istack.Nullable;
import io.lorentez.roboticon.commands.CompetitionTypeCommand;
import io.lorentez.roboticon.model.CompetitionType;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CompetitionTypeToCompetitionTypeCommandConverter implements Converter<CompetitionType, CompetitionTypeCommand> {

    @Synchronized
    @Nullable
    @Override
    public CompetitionTypeCommand convert(CompetitionType competitionType) {
        if(competitionType == null){
            return null;
        }
        final CompetitionTypeCommand competitionTypeCommand = new CompetitionTypeCommand();
        competitionTypeCommand.setId(competitionType.getId());
        competitionTypeCommand.setType(competitionType.getType());
        competitionTypeCommand.setScoreType(competitionType.getScoreType());
        return competitionTypeCommand;
    }

}
