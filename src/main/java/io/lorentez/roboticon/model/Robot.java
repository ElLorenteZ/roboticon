package io.lorentez.roboticon.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
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

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    @PastOrPresent
    private LocalDateTime timeAdded;

    @Builder.Default
    @OneToMany(mappedBy = "robot")
    private Set<Registration> registrations = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "robot")
    private Set<RobotTeam> robotTeams = new HashSet<>();

    @PrePersist
    private void setTimeAddedTimestamp(){
        this.timeAdded = LocalDateTime.now();
    }

}
