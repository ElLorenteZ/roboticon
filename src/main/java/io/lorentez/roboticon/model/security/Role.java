package io.lorentez.roboticon.model.security;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "role")
    private Set<UserRole> userRoles = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "authority", fetch = FetchType.EAGER)
    private Set<RoleAuthority> roleAuthorities = new HashSet<>();

    public void grantAuthority(Authority authority){
        this.getRoleAuthorities().add(RoleAuthority.builder()
                .role(this)
                .authority(authority)
                .build());
    }

    public void grantAuthority(Authority authority, LocalDateTime timeRemoved){
        this.getRoleAuthorities().add(RoleAuthority.builder()
                .role(this)
                .authority(authority)
                .timeRemoved(timeRemoved)
                .build());
    }
}
