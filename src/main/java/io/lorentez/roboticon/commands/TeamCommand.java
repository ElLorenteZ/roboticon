package io.lorentez.roboticon.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamCommand extends BasicTeamCommand{

    private List<UserInTeamCommand> users;
    private List<RobotCommand> robots;

}
