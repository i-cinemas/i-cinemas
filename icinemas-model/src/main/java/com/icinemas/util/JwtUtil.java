package com.icinemas.util;

import com.icinemas.enums.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET = "YourSuperSecretKeyThatShouldBeLongEnough1234567890";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours
    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Generate token
    public static String generateToken(String userId, UserRole role) {
        return Jwts.builder().setClaims(Collections.singletonMap("role", role))
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
