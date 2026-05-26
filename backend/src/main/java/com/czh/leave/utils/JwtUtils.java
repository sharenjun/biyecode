package com.czh.leave.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    private static final String SECRET_KEY = "your_secret_key_change_this_in_prod"; // 瀵嗛挜
    private static final long EXPIRATION_TIME = 1000 * 60 * 30; // 30 鍒嗛挓

    // Generate Token
    public String generateToken(Long userId, String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Validate Token & Get Claims
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if token is expired
    public boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }
    
    // Get User ID from Token
    public Long getUserId(String token) {
        return parseToken(token).get("userId", Long.class);
    }
}
