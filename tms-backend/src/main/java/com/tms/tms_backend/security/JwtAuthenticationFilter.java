package com.tms.tms_backend.security;

import com.tms.tms_backend.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements ServerAuthenticationConverter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.empty();
        }

        String token = authHeader.substring(7); // Extract JWT token
        String email = jwtUtil.extractEmail(token);

        if (email == null || jwtUtil.validateToken(token)) {
            return Mono.empty();
        }

        return Mono.just(new JwtAuthenticationToken(email, token, null)); // No roles yet, add if needed
    }
}
