package io.lorentez.roboticon.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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

    @Size(max = 100)
    private String name;

    @Size(max = 200)
    private String surname;

    @Email
    @NotBlank
    @Size(max = 200)
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
