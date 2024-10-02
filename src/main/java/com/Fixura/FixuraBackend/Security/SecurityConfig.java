package com.Fixura.FixuraBackend.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @SuppressWarnings("removal")
    @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors()
        .and()
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth

            // Endpoints permitidos para todos los usuarios
            .requestMatchers("/api/usuario/login", "/api/usuario/register").permitAll()

            .requestMatchers("/api/v1/departamento/**").permitAll()

            // Endpoints permitidos para usuarios con rol ADMIN
            .requestMatchers("/api/admin/**").hasAuthority("ADMIN")

            // Endpoints permitidos para usuarios con rol MODERADOR
            .requestMatchers("/api/moderator/**").hasAuthority("MODERATOR")
            
            // Permitir el acceso para cualquier usuario autenticado
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

    return http.build();
}

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
