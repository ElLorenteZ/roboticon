package io.lorentez.roboticon.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name="university_id")
    private University university;

    @Column(name = "time_created")
    private LocalDateTime timeCreated;

    @Builder.Default
    @ManyToMany(mappedBy = "teams")
    private Set<Robot> robots = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "team")
    private Set<UserTeam> userTeams = new HashSet<>();
}
