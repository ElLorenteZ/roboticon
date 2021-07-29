package io.lorentez.roboticon.security.managers;

import io.lorentez.roboticon.commands.RegistrationCommand;
import io.lorentez.roboticon.model.security.User;
import io.lorentez.roboticon.repositories.TeamRepository;
import io.lorentez.roboticon.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Slf4j
@Component
public class RegistrationAuthenticationManager extends BasicAuthenticationManager{

    private final UserRepository userRepository;

    public RegistrationAuthenticationManager(TeamRepository teamRepository, UserRepository userRepository) {
        super(teamRepository);
        this.userRepository = userRepository;
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
        return isUserAdminOrOwnerOfRegistrationTeam(authentication, registrationCommand);
    }


    public boolean canUserCreateRegistration(Authentication authentication, RegistrationCommand registrationCommand){
        return isUserAdminOrOwnerOfRegistrationTeam(authentication, registrationCommand);
    }

    private boolean isUserAdminOrOwnerOfRegistrationTeam(Authentication authentication, RegistrationCommand registrationCommand) {
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

    public boolean canUserSeeRegistration(Authentication authentication, Long userId){
        String email = (String) authentication.getPrincipal();
        if (email == null){
            email = "";
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.map(user -> user.getId().equals(userId)).orElse(Boolean.FALSE);
    }
}
