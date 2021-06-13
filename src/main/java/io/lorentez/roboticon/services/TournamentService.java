package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.TournamentCommand;

import java.util.List;

public interface TournamentService {

    public List<TournamentCommand> findAll();

}
