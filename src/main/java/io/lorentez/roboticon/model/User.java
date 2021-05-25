package io.lorentez.roboticon.model;

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
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String email;

    @Builder.Default
    private Boolean accountNonExpired = Boolean.TRUE;

    @Builder.Default
    private Boolean credentialsNonExpired = Boolean.TRUE;

    @Builder.Default
    private Boolean accountNonLocked = Boolean.TRUE;

    @Builder.Default
    @OneToMany
    private Set<UserTeam> userTeams = new HashSet<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "UserRegistration",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "registration_id") }
    )
    private Set<Registration> registrations = new HashSet<>();
}
