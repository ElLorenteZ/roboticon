package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.CurrentTeamUserCommand;
import io.lorentez.roboticon.converters.TeamToCurrentTeamUserCommandConverter;
import io.lorentez.roboticon.repositories.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService{

    private final TeamRepository teamRepository;
    private final TeamToCurrentTeamUserCommandConverter converter;

    public TeamServiceImpl(TeamRepository teamRepository,
                           TeamToCurrentTeamUserCommandConverter converter) {
        this.teamRepository = teamRepository;
        this.converter = converter;
    }

    @Override
    public Set<CurrentTeamUserCommand> fetchCurrentUserTeams(Long userId) {
        return teamRepository.findUserTeams(userId)
                .stream()
                .map(converter::convert)
                .collect(Collectors.toSet());
    }
}
