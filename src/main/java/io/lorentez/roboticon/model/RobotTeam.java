package io.lorentez.roboticon.model;

import io.lorentez.roboticon.model.validators.TimeEndAfterTimeStart;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TimeEndAfterTimeStart
@Entity
@Table(name = "robotteam")
public class RobotTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @PastOrPresent
    private LocalDateTime timeAdded;

    private LocalDateTime timeRemoved;

    @Builder.Default
    @NotNull
    @Enumerated(EnumType.STRING)
    private RobotTeamStatus status = RobotTeamStatus.OWNED;

    @ManyToOne
    @JoinColumn(name = "robot_id", nullable = false)
    private Robot robot;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @PrePersist
    void setTimeAdded(){
        this.timeAdded = LocalDateTime.now();
    }
}
