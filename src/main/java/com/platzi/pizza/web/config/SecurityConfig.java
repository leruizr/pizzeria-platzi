package com.platzi.pizza.web.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Deshabilitar CSRF
                .cors(Customizer.withDefaults()) // Habilitar CORS
                .authorizeHttpRequests(customizeRequests -> {
                            customizeRequests
                                    .requestMatchers(HttpMethod.GET, "/api/*").permitAll()
                                    .requestMatchers(HttpMethod.PUT).denyAll()
                                    .anyRequest()
                                    .authenticated();
                        }
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
