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
@Table(name = "Competition")
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 200)
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "competition_type_id")
    private CompetitionType competitionType;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @Builder.Default
    @OneToMany(mappedBy = "competition")
    private Set<Registration> registrations = new HashSet<>();


}
