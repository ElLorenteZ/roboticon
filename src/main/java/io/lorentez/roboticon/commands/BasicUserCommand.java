package io.lorentez.roboticon.commands;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasicUserCommand {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private String email;

}
