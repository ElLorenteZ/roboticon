package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.UniversityCommand;
import io.lorentez.roboticon.model.University;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UniversityCommandToUniversityConverter implements Converter<UniversityCommand, University> {

    @Synchronized
    @Nullable
    @Override
    public University convert(UniversityCommand universityCommand) {
        if (universityCommand == null){
            return null;
        }
        return University.builder()
                .id(universityCommand.getId())
                .name(universityCommand.getName())
                .addressLine1(universityCommand.getAddressLine1())
                .addressLine2(universityCommand.getAddressLine2())
                .zipCode(universityCommand.getZipCode())
                .province(universityCommand.getProvince())
                .city(universityCommand.getCity())
                .country(universityCommand.getCountry())
                .build();
    }
}
