package io.lorentez.roboticon.commands;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class StatusCredentials {

    @NotNull
    private String email;

    @NotNull
    private String status;

}
