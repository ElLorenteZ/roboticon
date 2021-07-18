package io.lorentez.roboticon.security.managers;

import io.lorentez.roboticon.model.security.User;
import io.lorentez.roboticon.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserAuthenticationManager {

    private final UserRepository userRepository;

    public boolean isUserSelf(Authentication authentication, Long userId) {
        String email = (String) authentication.getPrincipal();
        if (email == null) {
            return false;
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.map(user -> user.getId().equals(userId)).orElse(Boolean.FALSE);
    }

}
