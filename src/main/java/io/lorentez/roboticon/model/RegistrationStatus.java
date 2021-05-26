package io.lorentez.roboticon.model;

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
@Table(name = "RegistrationStatus")
public class RegistrationStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RegistrationCurrentStatus status;

    @NotNull
    @PastOrPresent
    private LocalDateTime timeFrom;

    @PastOrPresent
    private LocalDateTime timeTo;

    @ManyToOne
    private Registration registration;

}
