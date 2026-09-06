package com.example.multitenantExpenseTracker.Security;

//package com.example.multitenantExpenseTracker.security; // adjust to your actual package

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    // 1. GENERATE — now also embeds the role
    public String generateToken(UserDetails userDetails) {
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        // ^ pulls "ROLE_ADMIN" or "ROLE_USER" out of the UserDetails we built earlier

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("role", role)              // <-- NEW: embed role as a custom claim
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    // 2. EXTRACT username (unchanged)
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // 3. EXTRACT role (NEW)
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // 4. VALIDATE (unchanged)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}