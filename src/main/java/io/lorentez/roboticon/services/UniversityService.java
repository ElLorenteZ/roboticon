package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.UniversityCommand;

import java.util.List;

public interface UniversityService {

    UniversityCommand addUniversity(UniversityCommand universityCommand);

    UniversityCommand update(Long universityId, UniversityCommand universityCommand);

    UniversityCommand findById(Long id);

    List<UniversityCommand> findAll();

}
