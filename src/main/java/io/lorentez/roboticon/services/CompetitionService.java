package io.lorentez.roboticon.services;


import io.lorentez.roboticon.commands.CompetitionCommand;

public interface CompetitionService {

    CompetitionCommand findById(Long competitionId);

}
