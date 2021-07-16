package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.ChangePasswordCredentials;
import io.lorentez.roboticon.commands.UserRegisterCommand;
import io.lorentez.roboticon.model.security.PasswordResetToken;
import io.lorentez.roboticon.model.security.User;
import io.lorentez.roboticon.security.commands.LoginCredentials;
import io.lorentez.roboticon.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("resetPassword")
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

    @PostMapping("setPassword")
    public ResponseEntity<?> changePassword(@RequestParam String token,
                                            @RequestParam String newPassword){
        PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);
        if (passwordResetToken == null){
            return ResponseEntity.notFound().build();
        }
        if (passwordResetToken.isInvalid()){
            return ResponseEntity.badRequest().build();
        }
        userService.setNewPassword(passwordResetToken.getUser(), newPassword);
        userService.removeToken(passwordResetToken);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("login")
    public ResponseEntity<Object> loginUser(@RequestBody LoginCredentials credentials){
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasAuthority('user.user.password.change')")
    @PostMapping("changePassword")
    public ResponseEntity<?> changeUsersPassword(@RequestBody ChangePasswordCredentials passwordCredentials,
                                                 @AuthenticationPrincipal String email){
        User currentUser = userService.findByEmail(email);
        try {
            userService.changeUserPassword(currentUser, passwordCredentials);
            return ResponseEntity.noContent().build();
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterCommand command){
        try {
            userService.registerUser(command);
            return ResponseEntity.noContent().build();
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
