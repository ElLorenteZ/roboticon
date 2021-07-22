package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.UniversityCommand;
import io.lorentez.roboticon.converters.UniversityCommandToUniversityConverter;
import io.lorentez.roboticon.converters.UniversityToUniversityCommandConverter;
import io.lorentez.roboticon.model.University;
import io.lorentez.roboticon.repositories.UniversityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;
    private final UniversityToUniversityCommandConverter universityToUniversityCommandConverter;
    private final UniversityCommandToUniversityConverter universityCommandToUniversityConverter;

    @Override
    public UniversityCommand addUniversity(UniversityCommand universityCommand) {
        University university = universityCommandToUniversityConverter.convert(universityCommand);
        if(university == null){
            throw new NoSuchElementException();
        }
        university.setId(null);
        university = universityRepository.save(university);
        return universityToUniversityCommandConverter.convert(university);
    }

    @Override
    public UniversityCommand update(Long universityId, UniversityCommand universityCommand) {
        if(universityCommand == null){
            throw new IllegalArgumentException();
        }
        University university = universityRepository.findById(universityId).orElseThrow();
        university.setName(universityCommand.getName());
        university.setAddressLine1(universityCommand.getAddressLine1());
        university.setAddressLine2(universityCommand.getAddressLine2());
        university.setZipCode(universityCommand.getZipCode());
        university.setProvince(universityCommand.getProvince());
        university.setCity(universityCommand.getCity());
        university.setCountry(universityCommand.getCountry());
        university = universityRepository.save(university);
        return universityToUniversityCommandConverter.convert(university);
    }

    @Override
    public UniversityCommand findById(Long id) {
        University foundUniversity = universityRepository.findById(id).orElseThrow();
        return universityToUniversityCommandConverter.convert(foundUniversity);
    }

    @Override
    public List<UniversityCommand> findAll() {
        return universityRepository.findAll()
                .stream()
                .map(universityToUniversityCommandConverter::convert)
                .collect(Collectors.toList());
    }
}
