package com.ridehailing.backend.util;

import com.ridehailing.backend.config.JwtProperties;
import com.ridehailing.backend.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String ROLE_CLAIM = "role";

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String subject, Role role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
                .subject(subject)
                .claim(ROLE_CLAIM, role.name())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getSubjectFromToken(String token) {
        Claims claims = validateToken(token);
        return claims.getSubject();
    }

    public Role getRoleFromToken(String token) {
        Claims claims = validateToken(token);
        String roleName = claims.get(ROLE_CLAIM, String.class);
        return Role.valueOf(roleName);
    }
}

