package com.tms.tms_backend.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final String email;
    private final String token;

    // Constructor for an unauthenticated token (used before authentication)
    public JwtAuthenticationToken(String token) {
        super(null);
        this.email = null; // Email is extracted later after validation
        this.token = token;
        setAuthenticated(false); // 🚀 Marked as NOT authenticated
    }

    // Constructor for an authenticated token (used after authentication)
    public JwtAuthenticationToken(String email, String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.email = email;
        this.token = token;
        super.setAuthenticated(true); // ✅ Marked as authenticated
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }

    // 🚀 Prevents setting authentication manually outside the authentication manager
    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException("Use the authenticated constructor instead!");
        }
        super.setAuthenticated(false);
    }
}
