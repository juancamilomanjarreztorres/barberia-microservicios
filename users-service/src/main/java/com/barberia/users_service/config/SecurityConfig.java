package com.barberia.users_service.config;

import com.barberia.users_service.security.JwtAuthenticationFilter;

import jakarta.servlet.DispatcherType;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:5173")
        );

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
                        )
                )
                .authorizeHttpRequests(auth -> auth

                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers("/error").permitAll()

                        // Solo los administradores pueden crear usuarios administrativos
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/admin")
                        .hasRole("ADMINISTRADOR")

                        // Registro e inicio de sesión públicos
                        .requestMatchers(HttpMethod.POST,
                                "/api/usuarios",
                                "/api/usuarios/login",
                                "/api/usuarios/recuperar-contrasena",
                                "/api/usuarios/restablecer-contrasena"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/usuarios/perfil").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/perfil").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/cambiar-contrasena").authenticated()

                        // Consultas requieren autenticación
                        .requestMatchers(HttpMethod.GET,
                                "/api/usuarios",
                                "/api/usuarios/**"
                        ).hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/*/estado")
                        .hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/*/rol")
                        .hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/*")
                        .hasRole("ADMINISTRADOR")

                        .anyRequest().denyAll()

                )

                // Ejecutar nuestro filtro antes del filtro de autenticación
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}