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
@Entity
@Table(name = "UserTeam")
@TimeEndAfterTimeStart
public class UserTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserTeamStatus status;

    @NotNull
    @PastOrPresent
    private LocalDateTime timeAdded;

    private LocalDateTime timeRemoved;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;


}
