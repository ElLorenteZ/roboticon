package io.lorentez.roboticon.commands;

import lombok.*;

import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasicUserCommand {

    private Long id;
    private String name;
    private String surname;
    private String email;

}
