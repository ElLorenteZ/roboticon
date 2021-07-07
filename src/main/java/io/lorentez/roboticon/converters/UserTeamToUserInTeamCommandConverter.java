package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.UserInTeamCommand;
import io.lorentez.roboticon.model.UserTeam;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UserTeamToUserInTeamCommandConverter implements Converter<UserTeam, UserInTeamCommand> {

    @Synchronized
    @Nullable
    @Override
    public UserInTeamCommand convert(UserTeam userTeam) {
        if(userTeam == null || userTeam.getUser() == null){
            return null;
        }
        UserInTeamCommand userInTeamCommand = new UserInTeamCommand();
        userInTeamCommand.setId(userTeam.getUser().getId());
        userInTeamCommand.setName(userTeam.getUser().getName());
        userInTeamCommand.setSurname(userTeam.getUser().getSurname());
        userInTeamCommand.setStatus(userTeam.getStatus());
        userInTeamCommand.setEmail(userTeam.getUser().getEmail());
        return userInTeamCommand;
    }
}
