package io.lorentez.roboticon.model.security;

import io.lorentez.roboticon.model.Registration;
import io.lorentez.roboticon.model.UserTeam;
import lombok.*;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "User")
public class User implements UserDetails, CredentialsContainer {

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

    @NotBlank
    private String password;

    @Builder.Default
    private Boolean enabled = Boolean.TRUE;

    @Builder.Default
    private Boolean accountNonExpired = Boolean.TRUE;

    @Builder.Default
    private Boolean credentialsNonExpired = Boolean.TRUE;

    @Builder.Default
    private Boolean accountNonLocked = Boolean.TRUE;

    @Builder.Default
    @OneToMany(mappedBy = "team")
    private Set<UserTeam> userTeams = new HashSet<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "UserRegistration",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "registration_id") }
    )
    private Set<Registration> registrations = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Set<UserRole> userRoles = new HashSet<>();

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.userRoles.stream()
                .filter(userRole -> userRole.getTimeRemoved() == null ||
                        userRole.getTimeRemoved().isAfter(LocalDateTime.now()))
                .map(UserRole::getRole)
                .map(Role::getRoleAuthorities)
                .flatMap(Set::stream)
                .filter(roleAuthority -> roleAuthority.getTimeRemoved() == null ||
                        roleAuthority.getTimeRemoved().isAfter(LocalDateTime.now()))
                .map(RoleAuthority::getAuthority)
                .map(authority -> new SimpleGrantedAuthority(authority.getPermission()))
                .collect(Collectors.toSet());
    }

    public void grantRole(Role role){
        UserRole userRole = UserRole.builder()
                .user(this)
                .role(role)
                .build();
        this.getUserRoles().add(userRole);
    }

    public void grantRole(Role role, LocalDateTime expirationDate){
        UserRole userRole = UserRole.builder()
                .user(this)
                .role(role)
                .timeRemoved(expirationDate)
                .build();
        this.getUserRoles().add(userRole);
    }
}
