package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.CompetitionCommand;
import io.lorentez.roboticon.converters.CompetitionToCompetitionCommandConverter;
import io.lorentez.roboticon.model.Competition;
import io.lorentez.roboticon.repositories.CompetitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CompetitionServiceImpl implements CompetitionService{

    private final CompetitionRepository competitionRepository;
    private final CompetitionToCompetitionCommandConverter competitionToCompetitionCommandConverter;

    @Override
    public CompetitionCommand findById(Long competitionId) {
        Optional<Competition> competitionOptional = competitionRepository.findWithRegistrationsById(competitionId);
        return competitionOptional.map(competitionToCompetitionCommandConverter::convert).orElse(null);
    }
}
