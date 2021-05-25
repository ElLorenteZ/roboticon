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
@Table(name = "CompetitionType")
public class CompetitionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;

    @Enumerated(EnumType.STRING)
    private ScoreType scoreType;

    @Builder.Default
    @OneToMany
    private Set<Competition> competitions = new HashSet<>();
}
