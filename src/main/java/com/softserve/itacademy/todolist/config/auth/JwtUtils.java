package com.softserve.itacademy.todolist.config.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {

    @Value("${jwt.jwtSecret}")
    private String jwtSecret;
    @Value("${jwt.refresh-token}")
    private long refreshJwtExpiration;
    //        @Value("${jwt.jwtExpiration_24H}")
    @Value("${jwt.jwtExpiration_20_Sec}")
    private long jwtExpirationMs;

    public String generateAccessTokenFromUserName(String username) {
        return buildToken(username, jwtExpirationMs);
    }

    public String generateRefreshToken(String username) {
        return buildToken(username, refreshJwtExpiration);
    }

    private String buildToken(String username, long time) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty {}", e.getMessage());
        } catch (SignatureException | MalformedJwtException e) {
            log.error("Signature validation failed", e);
        }
        return false;
    }

    public String getSubjectFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token).getBody();
    }


    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
