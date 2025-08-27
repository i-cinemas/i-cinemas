package com.icinemas.util;

import com.icinemas.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

import static com.icinemas.constants.AuthConstants.JWT_USER_ROLE;

public class JwtUtil {

    private static final String SECRET = "YourSuperSecretKeyThatShouldBeLongEnough1234567890";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours
    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Generate token
    public static String generateToken(String userId, UserRole role) {
        return Jwts.builder().setClaims(Collections.singletonMap(JWT_USER_ROLE, role))
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // invalid signature, expired, malformed etc.
        }
    }

    public static Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public static String getUserId(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public static String getUserRole(String token) {
        return getAllClaimsFromToken(token).get(JWT_USER_ROLE, String.class);
    }
}