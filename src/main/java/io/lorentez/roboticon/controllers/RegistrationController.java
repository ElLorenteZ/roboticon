package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.RegistrationCommand;
import io.lorentez.roboticon.commands.RegistrationCurrentStatusCommand;
import io.lorentez.roboticon.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PreAuthorize("hasAuthority('admin.registration.team.view') OR " +
            "hasAuthority('user.registration.team.view') " +
            "AND @registrationAuthenticationManager.canUserViewRegistrationStatus(authentication, #teamId)")
    @GetMapping("/tournaments/{tournamentId}/team/{teamId}")
    public ResponseEntity<?> findRegistrationsInTournamentByTeamId(@PathVariable @NotNull Long tournamentId,
                                                                        @PathVariable @NotNull Long teamId){
        List<RegistrationCommand> registrations =
                registrationService.getTeamsRegistrationsForTournament(tournamentId, teamId);
        return ResponseEntity.ok(registrations);
    }

    @PreAuthorize("hasAuthority('admin.registration.edit') OR " +
            "hasAuthority('user.registration.edit') " +
            "AND @registrationAuthenticationManager.canUserChangeRegistration(authentication, #newRegistrationData)")
    @PutMapping("{registrationId}")
    public ResponseEntity<?> updateRegistration(@PathVariable @NotNull Long registrationId,
                                                @RequestBody @Valid RegistrationCommand newRegistrationData){
        try {
            RegistrationCommand command = registrationService.updateRegistration(registrationId, newRegistrationData);
            return ResponseEntity.noContent().build();
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('admin.registration.create') OR " +
            "hasAuthority('user.registration.create') " +
            "AND @registrationAuthenticationManager.canUserCreateRegistration(authentication, #newRegistrationData)")
    @PostMapping
    public ResponseEntity<?> createRegistration(@RequestBody @Valid RegistrationCommand newRegistrationData){
        try {
            RegistrationCommand command = registrationService.createRegistration(newRegistrationData);
            return ResponseEntity.created(URI.create("/api/v1/registrations/" + command.getId())).build();
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('admin.registration.status.update')")
    @PostMapping("{registrationId}/status")
    public ResponseEntity<?> addNewStatus(@PathVariable @NotNull Long registrationId,
                                          @RequestBody @Valid RegistrationCurrentStatusCommand statusCommand){
        registrationService.setNewRegistrationStatus(registrationId, statusCommand.getStatus());
        return ResponseEntity.noContent().build();
    }



}
