package io.lorentez.roboticon.model;

import lombok.*;

import javax.persistence.*;
import java.lang.annotation.Target;

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
    private String name;
    private String description;

    @ManyToOne
    private CompetitionType competitionType;

    @ManyToOne
    private Tournament tournament;

}
