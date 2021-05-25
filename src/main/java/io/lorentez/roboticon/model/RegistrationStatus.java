package io.lorentez.roboticon.model;

import lombok.*;

import javax.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private RegistrationCurrentStatus status;

    private LocalDateTime timeFrom;

    private LocalDateTime timeTo;

    @ManyToOne
    private Registration registration;

}
