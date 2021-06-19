package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.CurrentTeamUserCommand;

import java.util.List;

public interface TeamService {
    List<CurrentTeamUserCommand> fetchCurrentUserTeams(Long userId);
}
