package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.CurrentTeamUserCommand;
import io.lorentez.roboticon.commands.StatusCredentials;
import io.lorentez.roboticon.model.Team;
import io.lorentez.roboticon.model.UserTeamStatus;
import io.lorentez.roboticon.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/teams")
public class TeamController {

    private final TeamService teamService;

    @GetMapping("user")
    public List<CurrentTeamUserCommand> getUserTeams(@AuthenticationPrincipal String email){
        return teamService.fetchCurrentUserTeams(email);
    }

    @PreAuthorize("hasAuthority('admin.team.invite') OR " +
            "hasAuthority('user.team.invite') AND @teamsAuthenticationManager.userCanInvite(authentication, #teamId)")
    @PostMapping("{teamId}/invite")
    public ResponseEntity<?> inviteToTeam(@PathVariable Long teamId, @RequestParam String email){
        if (teamService.isUserInTeamActive(teamId, email)){
            return ResponseEntity.unprocessableEntity().build();
        }
        Team team = teamService.findById(teamId);
        teamService.invitePersonToTeamByEmail(team, email);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('admin.team.user.status')")
    @PostMapping("{teamId}/status")
    public ResponseEntity<?> changeStatusInTeam(@PathVariable Long teamId, @RequestBody StatusCredentials credentials){
        teamService.changeUserStatus(teamId, credentials.getEmail(), UserTeamStatus.valueOf(credentials.getStatus()
                .toUpperCase(Locale.ROOT)));
        return ResponseEntity.noContent().build();
    }

}
