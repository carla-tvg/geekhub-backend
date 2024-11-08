package com.geekhub1.geekhub1.config;

import com.geekhub1.geekhub1.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Autowired
    @Lazy
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    @Lazy
    private IUsuarioService usuarioService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Configuración de CORS de forma explícita con el nuevo método
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Usamos el método de configuración de CORS actualizado
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF (explícito en Spring Security 6.1)
                .authorizeRequests(auth -> auth
                        .requestMatchers("/usuarios/login", "/usuarios/crear").permitAll()  // Rutas públicas
                        .requestMatchers(HttpMethod.GET, "/usuarios/perfil").authenticated()  // Requiere autenticación
                        .requestMatchers("/usuarios/traer").hasAnyRole("USER", "ADMIN") // Rutas protegidas
                        .requestMatchers("/usuarios/editar/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/usuarios/eliminar/**").hasRole("ADMIN")
                        .anyRequest().authenticated()) // Cualquier otra ruta requiere autenticación
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Usamos JWT, no mantenemos sesiones
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Filtro JWT antes de la autenticación

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3002")); // Cambia esto según tu frontend
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true); // Permitir el uso de cookies

        // Ahora, usamos la implementación estándar de Spring MVC, que es compatible con tu configuración
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplicar la configuración a todas las rutas

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
