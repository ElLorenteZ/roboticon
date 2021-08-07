package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.BasicTeamCommand;
import io.lorentez.roboticon.commands.CurrentTeamUserCommand;
import io.lorentez.roboticon.commands.TeamCommand;
import io.lorentez.roboticon.model.Team;
import io.lorentez.roboticon.model.UserTeamStatus;

import java.util.List;

public interface TeamService {
    List<CurrentTeamUserCommand> fetchCurrentUserTeams(Long userId);

    boolean isUserInTeamActive(Long teamId, String email);

    void invitePersonToTeamByEmail(Team team, String email);

    Team findById(Long teamId);

    TeamCommand findCommandById(Long id);

    void changeUserStatus(Long teamId, String email, UserTeamStatus status);

    boolean existByTeamId(Long teamId);

    BasicTeamCommand update(Long teamId, BasicTeamCommand newTeamData);

    BasicTeamCommand createTeam(BasicTeamCommand team, String email);

}
