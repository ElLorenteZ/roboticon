package io.lorentez.roboticon.model.security;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String permission;

    @NotBlank
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "authority")
    private Set<RoleAuthority> roleAuthorities = new HashSet<>();

}
