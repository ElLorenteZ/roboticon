package io.lorentez.roboticon.commands;

import io.lorentez.roboticon.model.UserTeamStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInTeamCommand extends BasicUserCommand {

    private UserTeamStatus status;

}
