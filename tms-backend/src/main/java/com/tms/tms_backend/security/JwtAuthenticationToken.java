package com.tms.tms_backend.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final String email;
    private final String token;

    public JwtAuthenticationToken(String token) {
        super(null);
        this.email = null;
        this.token = token;
        setAuthenticated(false);
    }

    public JwtAuthenticationToken(String email, String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.email = email;
        this.token = token;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException("Use the authenticated constructor instead!");
        }
        super.setAuthenticated(false);
    }
}
