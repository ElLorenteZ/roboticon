package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.BasicTeamCommand;
import io.lorentez.roboticon.commands.CurrentTeamUserCommand;
import io.lorentez.roboticon.commands.StatusCredentials;
import io.lorentez.roboticon.commands.TeamCommand;
import io.lorentez.roboticon.model.Team;
import io.lorentez.roboticon.model.UserTeamStatus;
import io.lorentez.roboticon.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

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
            "hasAuthority('user.team.invite') " +
                    "AND @teamsAuthenticationManager.userCanInvite(authentication, #teamId)")
    @PostMapping("{teamId}/invite")
    public ResponseEntity<?> inviteToTeam(@PathVariable @NotNull Long teamId, @RequestParam @NotBlank String email){
        if (teamService.isUserInTeamActive(teamId, email)){
            return ResponseEntity.unprocessableEntity().build();
        }
        Team team = teamService.findById(teamId);
        teamService.invitePersonToTeamByEmail(team, email);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('admin.team.user.status') OR " +
            "hasAuthority('user.team.user.status') " +
                    "AND @teamsAuthenticationManager.userCanChangeStatus(authentication, #teamId, #credentials)")
    @PostMapping("{teamId}/status")
    public ResponseEntity<?> changeStatusInTeam(@PathVariable @NotNull Long teamId, @RequestBody @Valid StatusCredentials credentials){
        teamService.changeUserStatus(teamId, credentials.getEmail(), UserTeamStatus.valueOf(credentials.getStatus()
                .toUpperCase(Locale.ROOT)));
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('admin.team.view') OR " +
            "hasAuthority('user.team.view') AND @teamsAuthenticationManager.isUserInTeam(authentication, #teamId)")
    @GetMapping("{teamId}")
    public ResponseEntity<?> getTeamById(@PathVariable Long teamId){
        TeamCommand teamCommand = teamService.findCommandById(teamId);
        if (teamCommand == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(teamCommand);
    }

    @PreAuthorize("hasAuthority('admin.team.edit') OR " +
            "hasAuthority('user.team.edit') AND @teamsAuthenticationManager.canUserUpdateTeamData(authentication, #teamId)")
    @PutMapping("{teamId}")
    public ResponseEntity<?> updateTeam(@PathVariable @NotNull Long teamId,
                                        @RequestBody @Valid BasicTeamCommand newTeamData){
        try {
            BasicTeamCommand teamCommand = teamService.update(teamId, newTeamData);
            return ResponseEntity.ok(teamCommand);
        }
        catch (NoSuchElementException ex){
            return ResponseEntity.notFound().build();
        }
    }



}