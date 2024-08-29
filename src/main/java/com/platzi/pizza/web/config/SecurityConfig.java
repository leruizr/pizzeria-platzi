package com.platzi.pizza.web.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Deshabilitar CSRF
                .cors(Customizer.withDefaults()) // Habilitar CORS
                .authorizeHttpRequests(customizeRequests -> {
                            customizeRequests
                                    .requestMatchers("/api/auth/**").permitAll()
                                    .requestMatchers("/api/customers/**").hasAnyRole("CUSTOMER", "ADMIN")
                                    .requestMatchers(HttpMethod.GET, "/api/pizzas/**").hasAnyRole("CUSTOMER", "ADMIN")
                                    .requestMatchers(HttpMethod.POST, "/api/pizzas/**").hasRole("ADMIN")
                                    .requestMatchers(HttpMethod.PUT).hasRole("ADMIN")
                                    .requestMatchers("/api/orders/random").hasAuthority("random_order")
                                    .requestMatchers("/api/orders/**").hasRole("ADMIN")
                                    .anyRequest()
                                    .authenticated();
                        }
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
