package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.UniversityCommand;
import io.lorentez.roboticon.model.University;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UniversityToUniversityCommandConverter implements Converter<University, UniversityCommand> {

    @Synchronized
    @Nullable
    @Override
    public UniversityCommand convert(University university) {
        if(university == null){
            return null;
        }
    return UniversityCommand.builder()
            .id(university.getId())
            .name(university.getName())
            .addressLine1(university.getAddressLine1())
            .addressLine2(university.getAddressLine2())
            .zipCode(university.getZipCode())
            .province(university.getProvince())
            .city(university.getCity())
            .country(university.getCountry())
            .build();
    }
}
