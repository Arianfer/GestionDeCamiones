package com.proyecto_final.proyecto_final.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final com.proyecto_final.proyecto_final.Config.FiltroAutenticacionJwt filtroAutenticacionJwt;
    private final AuthenticationProvider proveedorAutenticacion;

    @Bean
    public SecurityFilterChain cadenaFiltrosSeguridad(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(configuracionCors()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                // Permitimos peticiones previas de seguridad (CORS)
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                                // Dejamos HTML y estáticos libres para que cargue la web
                                .requestMatchers("/", "/index.html").permitAll()

                                // Dejamos Login libre para que todos puedan iniciar sesión
                                .requestMatchers("/api/auth/**").permitAll()

                                // Aca solo jefes pueden gestionar/crear usuarios
                                .requestMatchers("/api/usuarios/**").hasAnyRole("ADMIN", "OFICINA")

                                // Y por ultimo, lo demás (clientes, camiones, recorridos) solo requiere estar logueado
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManager ->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(proveedorAutenticacion)
                .addFilterBefore(filtroAutenticacionJwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource configuracionCors() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}