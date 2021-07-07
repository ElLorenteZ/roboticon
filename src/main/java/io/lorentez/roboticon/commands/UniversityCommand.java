package io.lorentez.roboticon.commands;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniversityCommand {

    private Long id;
    private String name;
    private String addressLine1;
    private String addressLine2;
    private String zipCode;
    private String province;
    private String city;
    private String country;

}
