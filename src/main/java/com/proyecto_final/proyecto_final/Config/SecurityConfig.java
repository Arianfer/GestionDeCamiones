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

                                // /error es la página interna a la que Spring reenvía los errores no manejados.
                                // Si no está permitida, Spring Security la bloquea y todo error llega como 403.
                                .requestMatchers("/", "/index.html", "/error").permitAll()

                                // Dejamos Login libre para que todos puedan iniciar sesión
                                .requestMatchers("/api/auth/**").permitAll()

                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**",
                                        "/v3/api-docs",
                                        "/webjars/**"
                                ).permitAll()

                                // SCRUM-138: permisos por rol.
                                // Spring aplica la PRIMERA regla que coincide, por eso lo que usa el chofer
                                // va antes que la regla general de gestión.

                                // Vista del chofer: sus tareas, su ruta y el cambio de estado de su tarea.
                                // Que un chofer solo vea y cambie LO SUYO se controla en TareaService.
                                .requestMatchers(HttpMethod.GET, "/api/tareas/mis-tareas/**", "/api/tareas/mi-ruta/**")
                                    .hasAnyRole("ADMIN", "OFICINA", "EMPLEADO")
                                .requestMatchers(HttpMethod.PATCH, "/api/tareas/*/estado")
                                    .hasAnyRole("ADMIN", "OFICINA", "EMPLEADO")
                                // La vista del chofer también consulta rutas y camiones (solo lectura)
                                .requestMatchers(HttpMethod.GET, "/api/rutas/**", "/api/camiones/**")
                                    .hasAnyRole("ADMIN", "OFICINA", "EMPLEADO")

                                // Gestión: todo lo demás de estos módulos es solo para ADMIN y OFICINA
                                .requestMatchers(
                                        "/api/usuarios/**",
                                        "/api/camiones/**",
                                        "/api/clientes/**",
                                        "/api/rutas/**",
                                        "/api/servicios/**",
                                        "/api/tareas/**"
                                ).hasAnyRole("ADMIN", "OFICINA")

                                // Cualquier otra ruta requiere estar logueado
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