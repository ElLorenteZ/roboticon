package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.CurrentTeamUserCommand;
import io.lorentez.roboticon.model.Team;
import io.lorentez.roboticon.model.UserTeamStatus;

import java.util.List;

public interface TeamService {
    List<CurrentTeamUserCommand> fetchCurrentUserTeams(String userEmail);

    boolean isUserInTeamActive(Long teamId, String email);

    void invitePersonToTeamByEmail(Team team, String email);

    Team findById(Long teamId);

    void changeUserStatus(Long teamId, String email, UserTeamStatus status);
}
