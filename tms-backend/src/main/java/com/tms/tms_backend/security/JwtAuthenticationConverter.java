package com.tms.tms_backend.security;

import com.tms.tms_backend.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationConverter implements ServerAuthenticationConverter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationConverter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith("Bearer "))
                .map(authHeader -> authHeader.substring(7))
                .flatMap(token -> {
                    String email = jwtUtil.extractEmail(token);
                    String role = jwtUtil.extractRole(token); // ✅ Extract role

                    if (email == null || !jwtUtil.validateToken(token)) {
                        return Mono.empty();
                    }

                    List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));

                    return Mono.just(new JwtAuthenticationToken(email, token, authorities)); // ✅ Pass authorities
                });
    }

}
