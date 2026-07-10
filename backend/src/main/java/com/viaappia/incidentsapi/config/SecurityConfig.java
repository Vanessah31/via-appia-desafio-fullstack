package com.viaappia.incidentsapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Rotas públicas: login e Swagger
                        .requestMatchers(
                                "/auth/login",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Leitura (GET): qualquer usuário autenticado, dos dois perfis
                        .requestMatchers(HttpMethod.GET, "/incidents/**").hasAnyRole("READ_ONLY", "READ_WRITE")
                        .requestMatchers(HttpMethod.GET, "/stats/**").hasAnyRole("READ_ONLY", "READ_WRITE")

                        // Escrita: só quem tem perfil READ_WRITE
                        .requestMatchers(HttpMethod.POST, "/incidents/**").hasRole("READ_WRITE")
                        .requestMatchers(HttpMethod.PUT, "/incidents/**").hasRole("READ_WRITE")
                        .requestMatchers(HttpMethod.DELETE, "/incidents/**").hasRole("READ_WRITE")
                        .requestMatchers(HttpMethod.PATCH, "/incidents/**").hasRole("READ_WRITE")

                        // Qualquer outra requisição precisa estar autenticada
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}