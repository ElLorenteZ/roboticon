package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.TournamentCommand;

import java.util.List;

public interface TournamentService {

    List<TournamentCommand> findAll();

    TournamentCommand findById(Long id);

    List<TournamentCommand> findSearchedTournaments(String keyword);
}
