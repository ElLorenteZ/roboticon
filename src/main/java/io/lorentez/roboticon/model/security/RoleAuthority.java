package io.lorentez.roboticon.model.security;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "roleauthority")
@Entity
public class RoleAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "authority_id", nullable = false)
    private Authority authority;

    @NotNull
    private LocalDateTime timeGranted;

    private LocalDateTime timeRemoved;

    @PrePersist
    public void setTimeGrantedTimestamp(){
        this.timeGranted = LocalDateTime.now();
    }

}
