package io.lorentez.roboticon.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final long expiration_time;
    private final String secret;

    public RestAuthenticationSuccessHandler(@Value("${jwt.expiration_time}") long expiration_time,
                                            @Value("${jwt.secret}") String secret) {
        this.expiration_time = expiration_time;
        this.secret = secret;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        long currentTime = System.currentTimeMillis();
        String token = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(currentTime + expiration_time))
                .sign(Algorithm.HMAC512(secret));
        response.addHeader("Authorization", "Bearer " + token);


    }

}
