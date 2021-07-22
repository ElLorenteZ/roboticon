package io.lorentez.roboticon.commands;

import io.lorentez.roboticon.model.UserTeamStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInTeamCommand extends BasicUserCommand {

    @NotNull
    private UserTeamStatus status;

}
