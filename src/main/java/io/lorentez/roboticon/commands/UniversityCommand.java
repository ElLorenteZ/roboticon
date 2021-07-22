package io.lorentez.roboticon.commands;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniversityCommand {

    private Long id;

    @NotBlank
    @Size(max = 300)
    private String name;

    @NotBlank
    @Size(max = 200)
    private String addressLine1;

    @NotNull
    @Size(max = 200)
    private String addressLine2;

    @NotBlank
    @Size(max = 10)
    private String zipCode;

    @NotBlank
    @Size(max = 100)
    private String province;

    @NotBlank
    @Size(max = 100)
    private String city;

    @NotBlank
    @Size(max = 100)
    private String country;

}
