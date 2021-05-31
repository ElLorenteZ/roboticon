package io.lorentez.roboticon.model;

import lombok.*;
import org.hibernate.annotations.Cascade;

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
@Table(name = "Team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 150)
    private String name;

    @ManyToOne
    @JoinColumn(name="university_id")
    private University university;

    @NotNull
    @PastOrPresent
    @Column(name = "time_created")
    private LocalDateTime timeCreated;

    @Builder.Default
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    Set<RobotTeam> robotTeams = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private Set<UserTeam> userTeams = new HashSet<>();

    @PrePersist
    private void setTimeCreated(){
        this.timeCreated = LocalDateTime.now();
    }
}
