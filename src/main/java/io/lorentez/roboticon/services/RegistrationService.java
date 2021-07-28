package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.RegistrationCommand;
import io.lorentez.roboticon.model.RegistrationCurrentStatus;

import java.util.List;

public interface RegistrationService {

    List<RegistrationCommand> getTeamsRegistrationsForTournament(Long tournamentId, Long teamId);

    List<RegistrationCommand> getRegistrationsForCompetition(Long competitionId);

    RegistrationCommand updateRegistration(Long registrationId, RegistrationCommand newRegistrationData);

    void setNewRegistrationStatus(Long registrationId, RegistrationCurrentStatus status);

    RegistrationCommand createRegistration(RegistrationCommand newRegistrationData);
}

