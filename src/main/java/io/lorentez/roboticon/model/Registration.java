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
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "userregistration",
            joinColumns = { @JoinColumn(name = "registration_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private Set<User> users = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "registration", cascade = CascadeType.ALL)
    private Set<RegistrationStatus> statuses = new HashSet<>();
}
