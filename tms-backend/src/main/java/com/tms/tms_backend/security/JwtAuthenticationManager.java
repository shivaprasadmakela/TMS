package com.tms.tms_backend.security;

import com.tms.tms_backend.util.JwtUtil;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationManager(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String email = jwtUtil.extractEmail(token);

        if (email == null || !jwtUtil.validateToken(token)) {
            return Mono.error(new BadCredentialsException("Invalid JWT Token"));
        }

        String role = jwtUtil.extractRole(token);
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));

        return Mono.just(new JwtAuthenticationToken(email, token, authorities));
    }
}
