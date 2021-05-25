package io.lorentez.roboticon.model;

import lombok.*;

import javax.persistence.*;
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
    private String name;

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "zipcode")
    private String zipCode;

    @Column(name = "province")
    private String province;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Builder.Default
    @OneToMany(mappedBy = "university")
    private Set<Team> teams = new HashSet<>();

}
