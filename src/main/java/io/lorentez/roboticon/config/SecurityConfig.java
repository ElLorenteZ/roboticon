package io.lorentez.roboticon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lorentez.roboticon.repositories.UserRepository;
import io.lorentez.roboticon.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JpaUserDetailsService userDetailsService;
    private final RestAuthenticationSuccessHandler successHandler;
    private final RestAuthenticationFailureHandler failureHandler;
    private final ObjectMapper objectMapper;
    private final String secret;

    public SecurityConfig(
                            JpaUserDetailsService userDetailsService,
                            RestAuthenticationSuccessHandler successHandler,
                            RestAuthenticationFailureHandler failureHandler,
                            ObjectMapper objectMapper,
                            @Value("${jwt.secret}") String secret) {
        this.userDetailsService = userDetailsService;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.objectMapper = objectMapper;
        this.secret = secret;
    }

    public JsonObjectAuthenticationFilter jsonObjectAuthenticationFilter(AuthenticationManager authenticationManager){
        JsonObjectAuthenticationFilter filter = new JsonObjectAuthenticationFilter(
                new AntPathRequestMatcher("/api/**"),
                objectMapper);
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    // TODO remove for production
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/api/v1/auth/**").permitAll()
                .mvcMatchers(HttpMethod.GET,"/api/v1/competitions/*").permitAll()
                .mvcMatchers(HttpMethod.GET,"/api/v1/tournaments").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jsonObjectAuthenticationFilter(authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), super.userDetailsService(), secret))
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return RoboticonPasswordEncoderFactory.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }
}
