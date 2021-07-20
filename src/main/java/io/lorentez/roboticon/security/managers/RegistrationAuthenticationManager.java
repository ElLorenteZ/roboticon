package io.lorentez.roboticon.security.managers;

import io.lorentez.roboticon.commands.RegistrationCommand;
import io.lorentez.roboticon.repositories.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class RegistrationAuthenticationManager extends BasicAuthenticationManager{

    public RegistrationAuthenticationManager(TeamRepository teamRepository) {
        super(teamRepository);
    }

    public boolean canUserViewRegistrationStatus(Authentication authentication, Long teamId){
        String email = (String) authentication.getPrincipal();
        if (email == null){
            email = "";
        }
        log.info("User: " + email + " attempted to get registration information of team with ID: " + teamId.toString());
        return this.isUserMemberOfTeam(email, teamId);
    }

    public boolean canUserChangeRegistration(Authentication authentication, RegistrationCommand registrationCommand){
        String email = (String) authentication.getPrincipal();
        if (email == null){
            email = "";
        }
        Long teamId;
        try{
            teamId = registrationCommand.getRobot().getTeamCommand().getId();
        }
        catch (Exception e){
            return false;
        }
        log.info("User: " + email + " attempted to get registration information of team with ID: " + teamId.toString());
        return this.isUserAdminOrOwner(email, teamId);
    }




}
