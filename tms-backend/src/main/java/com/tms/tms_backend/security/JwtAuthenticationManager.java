package com.tms.tms_backend.security;

import com.tms.tms_backend.util.JwtUtil;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;

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

        if (email != null && jwtUtil.validateToken(token)) {
            return Mono.just(new User(email, "", Collections.emptyList()))
                    .map(user -> new JwtAuthenticationToken(user, token, user.getAuthorities()));
        }
        return Mono.empty();
    }
}
