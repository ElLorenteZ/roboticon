package io.lorentez.roboticon.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lorentez.roboticon.security.commands.LoginCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class JsonObjectAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public JsonObjectAuthenticationFilter(ObjectMapper objectMapper) {
        super("/api/v1/auth/**");
        this.objectMapper = objectMapper;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.debug("Authentication success. Updating SecurityContextHolder to contain: " + authResult);
        }
        SecurityContextHolder.getContext().setAuthentication(authResult);
        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        CachedRequestWrapper wrappedRequest = new CachedRequestWrapper(request);

        if (logger.isDebugEnabled()){
            logger.debug("Request is to process authentication in JsonObjectAuthenticationFilter");
        }
        try {
            Authentication authResult = attemptAuthentication(wrappedRequest, response);
            if (authResult != null) {
                successfulAuthentication(wrappedRequest, response, chain, authResult);
            } else {
                chain.doFilter(wrappedRequest, response);
            }
        }
        catch (AuthenticationException e) {
            unsuccessfulAuthentication(wrappedRequest, response, e);
        }


    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse)
            throws AuthenticationException {
        try {

            String requestBody = requestToString(httpServletRequest);

            LoginCredentials loginCredentials = objectMapper.readValue(requestBody, LoginCredentials.class);
            if (loginCredentials.getPassword() == null || loginCredentials.getPassword().isBlank()){
                return null;
            }
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(loginCredentials.getEmail(),
                            loginCredentials.getPassword());
            token.setDetails(this.authenticationDetailsSource.buildDetails(httpServletRequest));
            log.info("Attempt of authentication with user: " + loginCredentials.getEmail());
            return getAuthenticationManager().authenticate(token);
        }
        catch (IOException e) {
            return null;
        }

    }

    private String requestToString(HttpServletRequest httpServletRequest) throws IOException {
        ServletInputStream inputStream = httpServletRequest.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();

    }


}
