package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.TournamentCommand;
import io.lorentez.roboticon.converters.TournamentToTournamentCommandConverter;
import io.lorentez.roboticon.model.Tournament;
import io.lorentez.roboticon.repositories.TournamentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TournamentServiceImpl implements TournamentService{

    private final TournamentRepository tournamentRepository;
    private final TournamentToTournamentCommandConverter tournamentConverter;

    public TournamentServiceImpl(TournamentRepository tournamentRepository,
                                 TournamentToTournamentCommandConverter tournamentConverter) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentConverter = tournamentConverter;
    }

    @Override
    public List<TournamentCommand> findAll() {
        return tournamentRepository.findAllWithCompetitions()
                .stream()
                .map(tournament -> tournamentConverter.convert(tournament))
                .collect(Collectors.toList());

    }

}
