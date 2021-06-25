package io.lorentez.roboticon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> {
            //TODO remove for production
            authorize
                    .antMatchers("/h2-console/**").permitAll()
                    .antMatchers("/api/v1/auth/login/**", "/api/v1/auth/register/**").permitAll()
                    .mvcMatchers(HttpMethod.GET,"/api/v1/competitions/*").permitAll()
                    .mvcMatchers(HttpMethod.GET,"/api/v1/tournaments").permitAll();
        })
            .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();
    }



}
