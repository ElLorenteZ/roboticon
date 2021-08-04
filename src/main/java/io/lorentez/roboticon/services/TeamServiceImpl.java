package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.BasicTeamCommand;
import io.lorentez.roboticon.commands.CurrentTeamUserCommand;
import io.lorentez.roboticon.commands.TeamCommand;
import io.lorentez.roboticon.converters.TeamToBasicTeamCommandConverter;
import io.lorentez.roboticon.converters.TeamToCurrentTeamUserCommandConverter;
import io.lorentez.roboticon.converters.TeamToTeamCommandConverter;
import io.lorentez.roboticon.converters.UniversityCommandToUniversityConverter;
import io.lorentez.roboticon.model.Team;
import io.lorentez.roboticon.model.University;
import io.lorentez.roboticon.model.UserTeam;
import io.lorentez.roboticon.model.UserTeamStatus;
import io.lorentez.roboticon.model.security.PasswordResetToken;
import io.lorentez.roboticon.model.security.Role;
import io.lorentez.roboticon.model.security.User;
import io.lorentez.roboticon.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TeamServiceImpl implements TeamService{

    private final TeamToBasicTeamCommandConverter basicTeamConverter;
    private final TeamToTeamCommandConverter teamConverter;
    private final RoleRepository roleRepository;
    private final UserTeamRepository userTeamRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final UniversityRepository universityRepository;
    private final TeamToCurrentTeamUserCommandConverter converter;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UniversityCommandToUniversityConverter universityConverter;

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
            Optional<Role> userRoleOptional = roleRepository.findByName("USER");
            if(userRoleOptional.isEmpty()){
                throw new RuntimeException("Cannot find user role in database!");
            }
            User user = User.builder()
                    .email(email)
                    .password("{noop}password")
                    .enabled(false)
                    .build();
            user.grantRole(userRoleOptional.get());
            user = userRepository.save(user);
            userTeamRepository.save(UserTeam.builder()
                    .team(team)
                    .user(user)
                    .timeAdded(LocalDateTime.now())
                    .status(UserTeamStatus.INVITED)
                    .build());
            String token = UUID.randomUUID().toString();
            PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
            passwordResetTokenRepository.save(passwordResetToken);
        }
    }

    @Override
    public Team findById(Long teamId) {
        Optional<Team> teamOptional = teamRepository.findById(teamId);
        return teamOptional.orElse(null);
    }

    @Override
    public void changeUserStatus(Long teamId, String email, UserTeamStatus status) {
        Optional<UserTeam> previousUserStatus = teamRepository.findActualMembersByTeamId(teamId, email);
        previousUserStatus.ifPresent(userTeam -> {
            if (!userTeam.getStatus().equals(status)){
                LocalDateTime timestamp = LocalDateTime.now();
                userTeam.setTimeRemoved(timestamp);
                userTeamRepository.save(userTeam);
                userTeamRepository.save(UserTeam.builder()
                        .user(userTeam.getUser())
                        .team(userTeam.getTeam())
                        .status(status)
                        .timeAdded(timestamp)
                        .build());
            }
        });
    }

    @Override
    public TeamCommand findCommandById(Long id) {
        Optional<Team> teamOptional = teamRepository.fetchSingleTeamInformation(id);
        return teamOptional.map(teamConverter::convert).orElse(null);
    }

    @Override
    public boolean existByTeamId(Long teamId) {
        return teamRepository.existsById(teamId);
    }

    @Override
    public BasicTeamCommand update(Long teamId, BasicTeamCommand newTeamData) {
        Optional<Team> updatedTeamOptional = teamRepository.findById(teamId);
        return updatedTeamOptional.map(updatedTeam -> {
            updatedTeam.setName(newTeamData.getName());
            updatedTeam.setUniversity(universityConverter.convert(newTeamData.getUniversity()));
            Team team = teamRepository.save(updatedTeam);
            return basicTeamConverter.convert(team);
        }).orElseThrow();
    }

    @Transactional
    @Override
    public BasicTeamCommand createTeam(BasicTeamCommand team, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        University university = universityConverter.convert(team.getUniversity());
        Team newTeam = Team.builder()
                .name(team.getName())
                .timeCreated(LocalDateTime.now())
                .university(university)
                .build();
        newTeam = teamRepository.save(newTeam);
        UserTeam userTeamOwner = UserTeam.builder()
                .user(user)
                .team(newTeam)
                .status(UserTeamStatus.OWNER)
                .timeAdded(LocalDateTime.now())
                .build();
        userTeamRepository.save(userTeamOwner);
        return basicTeamConverter.convert(newTeam);
    }
}
