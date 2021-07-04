package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.CurrentTeamUserCommand;
import io.lorentez.roboticon.converters.TeamToCurrentTeamUserCommandConverter;
import io.lorentez.roboticon.model.Team;
import io.lorentez.roboticon.model.UserTeam;
import io.lorentez.roboticon.model.UserTeamStatus;
import io.lorentez.roboticon.model.security.User;
import io.lorentez.roboticon.repositories.TeamRepository;
import io.lorentez.roboticon.repositories.UserRepository;
import io.lorentez.roboticon.repositories.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TeamServiceImpl implements TeamService{

    private final UserTeamRepository userTeamRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TeamToCurrentTeamUserCommandConverter converter;

    @Override
    public List<CurrentTeamUserCommand> fetchCurrentUserTeams(String email) {
        return teamRepository.findUserTeams(email)
                .stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isUserInTeamActive(Long teamId, String email) {
        return teamRepository.findActualMembersByTeamId(teamId, email).isPresent();
    }

    @Override
    public void invitePersonToTeamByEmail(Team team, String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            userTeamRepository.save(UserTeam.builder()
                    .team(team)
                    .user(userOptional.get())
                    .timeAdded(LocalDateTime.now())
                    .status(UserTeamStatus.INVITED)
                    .build());
        }
        else {
            User user = User.builder()
                    .email(email)
                    .password("{noop}password")
                    .enabled(false)
                    .build();
            user = userRepository.save(user);
            userTeamRepository.save(UserTeam.builder()
                    .team(team)
                    .user(user)
                    .timeAdded(LocalDateTime.now())
                    .status(UserTeamStatus.INVITED)
                    .build());
        }
    }

    @Override
    public Team findById(Long teamId) {
        Optional<Team> teamOptional = teamRepository.findById(teamId);
        return teamOptional.orElse(null);
    }
}
