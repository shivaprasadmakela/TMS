package com.tms.tms_backend.config;

import com.tms.tms_backend.exception.CustomAccessDeniedHandler;
import com.tms.tms_backend.exception.CustomAuthenticationEntryPoint;
import com.tms.tms_backend.security.JwtAuthenticationManager;
import com.tms.tms_backend.security.JwtAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationManager jwtAuthenticationManager;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public SecurityConfig(JwtAuthenticationManager jwtAuthenticationManager, JwtAuthenticationConverter jwtAuthenticationConverter) {
        this.jwtAuthenticationManager = jwtAuthenticationManager;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/auth/**").permitAll()
                        .anyExchange().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .addFilterAt(authenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public AuthenticationWebFilter authenticationWebFilter() {
        AuthenticationWebFilter authWebFilter = new AuthenticationWebFilter(jwtAuthenticationManager);
        authWebFilter.setServerAuthenticationConverter(jwtAuthenticationConverter);
        return authWebFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
