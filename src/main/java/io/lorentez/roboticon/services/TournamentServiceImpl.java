package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.CompetitionCommand;
import io.lorentez.roboticon.commands.TournamentCommand;
import io.lorentez.roboticon.converters.CompetitionCommandToCompetitionConverter;
import io.lorentez.roboticon.converters.TournamentCommandToTournamentConverter;
import io.lorentez.roboticon.converters.TournamentToTournamentCommandConverter;
import io.lorentez.roboticon.model.Competition;
import io.lorentez.roboticon.model.CompetitionType;
import io.lorentez.roboticon.model.Tournament;
import io.lorentez.roboticon.repositories.CompetitionRepository;
import io.lorentez.roboticon.repositories.CompetitionTypeRepository;
import io.lorentez.roboticon.repositories.TournamentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TournamentServiceImpl implements TournamentService{

    private final TournamentRepository tournamentRepository;
    private final CompetitionRepository competitionRepository;
    private final CompetitionTypeRepository competitionTypeRepository;
    private final TournamentToTournamentCommandConverter tournamentToTournamentCommandConverter;
    private final TournamentCommandToTournamentConverter tournamentCommandToTournamentConverter;
    private final CompetitionCommandToCompetitionConverter competitionCommandToCompetitionConverter;

    public TournamentServiceImpl(TournamentRepository tournamentRepository,
                                 CompetitionRepository competitionRepository, CompetitionTypeRepository competitionTypeRepository, TournamentToTournamentCommandConverter tournamentConverter, TournamentCommandToTournamentConverter tournamentCommandToTournamentConverter, CompetitionCommandToCompetitionConverter competitionCommandToCompetitionConverter) {
        this.tournamentRepository = tournamentRepository;
        this.competitionRepository = competitionRepository;
        this.competitionTypeRepository = competitionTypeRepository;
        this.tournamentToTournamentCommandConverter = tournamentConverter;
        this.tournamentCommandToTournamentConverter = tournamentCommandToTournamentConverter;
        this.competitionCommandToCompetitionConverter = competitionCommandToCompetitionConverter;
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
//        newTournament.getCompetitions().forEach(competition -> {
//            CompetitionType competitionType = competitionTypeRepository.findById(competition.getCompetitionType().getId())
//                    .orElseThrow();
//            competition.setCompetitionType(competitionType);
//            competition = competitionRepository.save(competition);
//        });
        return tournamentToTournamentCommandConverter.convert(newTournament);
    }

    @Transactional
    @Override
    public TournamentCommand update(Long tournamentId, TournamentCommand tournamentCommand) {
        Tournament tournament = tournamentRepository.findByIdFetchCompetitions(tournamentId).orElseThrow();
        tournament.setName(tournamentCommand.getName());
        tournament.setDateStart(tournamentCommand.getDateStart());
        tournament.setDateEnd(tournamentCommand.getDateEnd());
        tournament.getCompetitions().forEach(competition -> {
            Optional<CompetitionCommand> updatedCompetitionOptional = tournamentCommand.getCompetitions()
                    .stream()
                    .filter(competitionCommand -> competitionCommand.getId() != null &&
                            !competitionCommand.getId().equals(0L) &&
                            competitionCommand.getId().equals(competition.getId()))
                    .findFirst();
            if (updatedCompetitionOptional.isEmpty()){
                //tournament.getCompetitions().remove(competition);
                competitionRepository.delete(competition);
            }
            else {
                CompetitionCommand updatedCompetition = updatedCompetitionOptional.get();
                competition.setName(updatedCompetition.getName());
                competition.setDescription(updatedCompetition.getDescription());
                if (!competition.getCompetitionType().getId().equals(updatedCompetition.getCompetitionType().getId())){
                    CompetitionType competitionType = competitionTypeRepository
                            .findById(updatedCompetition.getCompetitionType().getId()).orElseThrow();
                    competition.setCompetitionType(competitionType);
                }
                competitionRepository.save(competition);
            }
        });
        tournamentCommand.getCompetitions()
                .stream()
                .filter(competitionCommand -> competitionCommand.getId() == null || competitionCommand.getId().equals(0L))
                .forEach(competitionCommand -> {
                    Competition competition = competitionCommandToCompetitionConverter.convert(competitionCommand);
                    if (competition == null){
                        throw new IllegalArgumentException();
                    }
                    competition.setTournament(tournament);
                    CompetitionType competitionType = competitionTypeRepository.findById(
                            competitionCommand.getCompetitionType().getId()).orElseThrow();
                    competition.setCompetitionType(competitionType);
                    competitionRepository.save(competition);
                });
        tournamentRepository.save(tournament);
        return tournamentToTournamentCommandConverter.convert(tournament);
    }
}
