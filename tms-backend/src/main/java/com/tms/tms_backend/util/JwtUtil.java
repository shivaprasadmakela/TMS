package com.tms.tms_backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    private static final String RAW_SECRET_KEY = "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0a1b2c3d4e5f6g7h8"; // Same across backend
    private static final String SECRET_KEY_STRING = Base64.getEncoder().encodeToString(RAW_SECRET_KEY.getBytes()); // Encode
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY_STRING));
    private static final long EXPIRATION_TIME = 86400000; // 24 hours

    public String generateToken(String email, String role, String clientCode) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role.toUpperCase())
                .claim("clientCode", clientCode)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return (String) getClaims(token).get("role");
    }

    public boolean validateToken(String token) {
        try {
            return getClaims(token).getExpiration().after(new Date());
        } catch (Exception e) {
            return false; // Invalid token
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(getClaims(token));
    }
}
