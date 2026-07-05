package com.january0001.project.forumbackend.security.config;

import com.january0001.project.forumbackend.security.filter.JWTRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTRequestFilter jwtRequestFilter;

    //Dev bypass is in fact disabled now, we're no longer permitting changes on a whim.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JWTRequestFilter jWTRequestFilter) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.contentSecurityPolicy(csp -> csp
                        .policyDirectives("default-src 'self'; script-src 'self'; style-src 'self';")))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/register", "/api/login", "/api/emailVerify/verify", "/api/emailVerify/resend").permitAll() // issue encountered here when making /api/login where an extra slash caused a 403. Make sure that the mapping matches 1:1.
                        .requestMatchers(HttpMethod.GET, "/api/thread-categories", "/api/threads/category/{categoryId}").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A, 12);
    }

}
