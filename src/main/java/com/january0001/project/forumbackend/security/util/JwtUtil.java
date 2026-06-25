package com.january0001.project.forumbackend.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.validity}")
    private Integer validityTokenExpiration;

    @Value("${jwt.refresh}")
    private Integer refreshTokenExpiration;

    //HMAC checks for key strength, key is now inside a method, will throw an error if it's too weak. Enforcement of security.
    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username, Integer userId, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("username", username) //username claim is added so we can yoink that later
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityTokenExpiration * 1000 * 60))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims claimParser(String token) {
        return Jwts.parser()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Integer yoinkUserID(String token) {
        return Integer.parseInt(claimParser(token).getSubject());
    }

    public String yoinkUserName(String token) {
        return claimParser(token).get("username", String.class);
    }

    public String yoinkRole(String token) {
        return claimParser(token).get("role", String.class);
    }


}
