package io.lorentez.roboticon.model;

import io.lorentez.roboticon.model.security.User;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Registration")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Robot robot;

    @ManyToOne
    private Competition competition;

    @Builder.Default
    @ManyToMany(mappedBy = "registrations")
    private Set<User> users = new HashSet<>();

    @Builder.Default
    @OneToMany
    private Set<RegistrationStatus> statuses = new HashSet<>();
}
