package io.lorentez.roboticon.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "competitiontype")
public class CompetitionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 80)
    private String type;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ScoreType scoreType;

    @Builder.Default
    @OneToMany
    private Set<Competition> competitions = new HashSet<>();
}
