package com.levimartines.mylearningbackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String username) {
        return Jwts.builder().setSubject(username)
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSigningKey())
            .compact();
    }

    public boolean isTokenValid(String token) {
        Claims claims = getClaims(token);
        if (claims == null || claims.isEmpty()) {
            return false;
        }
        String username = claims.getSubject();
        Date expirationDate = claims.getExpiration();
        Date now = new Date(System.currentTimeMillis());
        return username != null && expirationDate != null && now.before(expirationDate);
    }

    private Claims getClaims(String token) {
        try {
            return getParser()
                .parseClaimsJws(token)
                .getBody();
        } catch (Exception e) {
            return new DefaultClaims(Collections.emptyMap());
        }
    }

    private JwtParser getParser() {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token);
        if (claims == null || claims.isEmpty()) {
            return null;
        }
        return claims.getSubject();
    }

}

