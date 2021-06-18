package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.CurrentTeamUserCommand;

import java.util.Set;

public interface TeamService {
    Set<CurrentTeamUserCommand> fetchCurrentUserTeams(Long userId);
}
