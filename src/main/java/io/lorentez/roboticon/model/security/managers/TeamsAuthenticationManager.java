package io.lorentez.roboticon.model.security.managers;

import io.lorentez.roboticon.model.Team;
import io.lorentez.roboticon.model.UserTeam;
import io.lorentez.roboticon.model.UserTeamStatus;
import io.lorentez.roboticon.model.security.User;
import io.lorentez.roboticon.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class TeamsAuthenticationManager {

    private final TeamRepository teamRepository;

    public boolean userCanInvite(Authentication authentication, Long teamId){
        String email = (String) authentication.getPrincipal();
        Optional<UserTeam> userTeamOptional = teamRepository.findActualMembersByTeamId(teamId, email);
        return userTeamOptional.map(userTeam -> userTeam.getStatus().equals(UserTeamStatus.ADMIN)
                || userTeam.getStatus().equals(UserTeamStatus.OWNER))
                .orElse(Boolean.FALSE);
    }

}
