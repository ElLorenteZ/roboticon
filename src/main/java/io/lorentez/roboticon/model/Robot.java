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
@Table(name = "Robot")
public class Robot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDateTime timeAdded;

    @Builder.Default
    @OneToMany(mappedBy = "robot")
    private Set<Registration> registrations = new HashSet<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "RobotTeam",
            joinColumns = {@JoinColumn(name = "robot_id")},
            inverseJoinColumns = {@JoinColumn(name = "team_id")}
    )
    private Set<Team> teams = new HashSet<>();
}
