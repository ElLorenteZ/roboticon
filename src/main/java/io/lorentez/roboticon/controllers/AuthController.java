package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.EmailObject;
import io.lorentez.roboticon.model.security.User;
import io.lorentez.roboticon.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam String email) {
        User user = userService.findByEmail(email);
        if (user == null){
            log.info("Failed attempt of password's reset for user: " + email);
            return ResponseEntity.notFound().build();
        }
        String token = UUID.randomUUID().toString();
        userService.createPasswordResetToken(user, token);
        log.info("Password reset token for user: " + email + " created..");
        //TODO mail sender redirecting to angular application
        return ResponseEntity.noContent().build();
    }

}
