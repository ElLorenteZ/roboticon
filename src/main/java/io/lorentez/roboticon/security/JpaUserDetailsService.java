package io.lorentez.roboticon.security;

import io.lorentez.roboticon.model.security.User;
import io.lorentez.roboticon.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Used to provide UserDetails to Spring Security filters.

@RequiredArgsConstructor
@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailWithAuthorities(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User with email: " + email + " was not found in database!"));
        return user;
    }

}
