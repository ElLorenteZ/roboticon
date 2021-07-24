package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.TournamentCommand;
import io.lorentez.roboticon.converters.TournamentCommandToTournamentConverter;
import io.lorentez.roboticon.converters.TournamentToTournamentCommandConverter;
import io.lorentez.roboticon.model.CompetitionType;
import io.lorentez.roboticon.model.Tournament;
import io.lorentez.roboticon.repositories.CompetitionRepository;
import io.lorentez.roboticon.repositories.CompetitionTypeRepository;
import io.lorentez.roboticon.repositories.TournamentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TournamentServiceImpl implements TournamentService{

    private final TournamentRepository tournamentRepository;
    private final CompetitionRepository competitionRepository;
    private final CompetitionTypeRepository competitionTypeRepository;
    private final TournamentToTournamentCommandConverter tournamentToTournamentCommandConverter;
    private final TournamentCommandToTournamentConverter tournamentCommandToTournamentConverter;

    public TournamentServiceImpl(TournamentRepository tournamentRepository,
                                 CompetitionRepository competitionRepository, CompetitionTypeRepository competitionTypeRepository, TournamentToTournamentCommandConverter tournamentConverter, TournamentCommandToTournamentConverter tournamentCommandToTournamentConverter) {
        this.tournamentRepository = tournamentRepository;
        this.competitionRepository = competitionRepository;
        this.competitionTypeRepository = competitionTypeRepository;
        this.tournamentToTournamentCommandConverter = tournamentConverter;
        this.tournamentCommandToTournamentConverter = tournamentCommandToTournamentConverter;
    }

    @Override
    public List<TournamentCommand> findAll() {
        return tournamentRepository.findAllWithCompetitions()
                .stream()
                .map(tournamentToTournamentCommandConverter::convert)
                .collect(Collectors.toList());

    }

    @Override
    public TournamentCommand findById(Long id) {
        return tournamentRepository.findById(id)
                .map(tournamentToTournamentCommandConverter::convert)
                .orElse(null);
    }

    @Override
    public List<TournamentCommand> findSearchedTournaments(String keyword) {
        Set<Long> ids = tournamentRepository.getSearchedTournamentsIds(keyword);
        if (ids.isEmpty()){
            return new ArrayList<>();
        }
        return tournamentRepository.searchForCompetition(ids)
                .stream()
                .map(tournamentToTournamentCommandConverter::convert)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public TournamentCommand save(TournamentCommand newTournamentCommand) {
        Tournament newTournament = tournamentCommandToTournamentConverter.convert(newTournamentCommand);
        if (newTournament == null) {
            throw new IllegalArgumentException();
        }
        newTournament.setId(null);
        newTournament = tournamentRepository.save(newTournament);
        newTournament.getCompetitions().forEach(competition -> {
            CompetitionType competitionType = competitionTypeRepository.findById(competition.getCompetitionType().getId())
                    .orElseThrow();
            competition.setCompetitionType(competitionType);
            competition = competitionRepository.save(competition);
        });
        return tournamentToTournamentCommandConverter.convert(newTournament);
    }
}
