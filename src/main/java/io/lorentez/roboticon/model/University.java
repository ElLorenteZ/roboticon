package io.lorentez.roboticon.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "University")
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 300)
    private String name;

    @NotBlank
    @Size(max = 200)
    @Column(name = "address_line1")
    private String addressLine1;

    @NotBlank
    @Size(max = 200)
    @Column(name = "address_line2")
    private String addressLine2;

    @NotBlank
    @Size(max = 10)
    @Column(name = "zipcode")
    private String zipCode;

    @NotBlank
    @Size(max = 100)
    @Column(name = "province")
    private String province;

    @NotBlank
    @Size(max = 100)
    @Column(name = "city")
    private String city;

    @NotBlank
    @Size(max = 100)
    @Column(name = "country")
    private String country;

    @Builder.Default
    @OneToMany(mappedBy = "university")
    private Set<Team> teams = new HashSet<>();

}
