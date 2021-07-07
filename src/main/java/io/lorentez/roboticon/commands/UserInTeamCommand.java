package io.lorentez.roboticon.commands;

import io.lorentez.roboticon.model.UserTeamStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInTeamCommand extends BasicUserCommand {

    private UserTeamStatus status;

}
