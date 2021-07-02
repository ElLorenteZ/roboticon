package io.lorentez.roboticon.model.security;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "passwordresettoken")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime expirationDate;

    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @PrePersist
    private void setExpirationDate(){
        this.expirationDate = LocalDateTime.now().plusDays(3L);
    }

    public PasswordResetToken(String token, User user){
        this.token = token;
        this.user = user;
    }

}
