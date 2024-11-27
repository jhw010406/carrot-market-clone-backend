package com.jhw.carrot_market_clone_backend.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    public static String key;

    public JwtService(@Value("${jwt.secret.key}") String inputKey) {
        key = inputKey;
    }

    // JWT의 header와 claim은 Base64 기반으로 인코딩 되어있으며, 쉽게 디코딩 될 수 있다.
    // 따라서, signature를 통해 해당 JWT가 유효한지 판단하며, signature는 secret key를 기반으로 생성한다.
    public static SecretKey getSecretKey(String inputKey) {
        byte[] keyBytes = Decoders.BASE64.decode(inputKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static String generateAccessToken(String userId) {
        return generateToken(userId, 60 * 15);
    }

    public static String generateRefreshToken(String userId) {
        return generateToken(userId, 60 * 60 * 24 * 180);
    }

    // 유저 id 기반으로 JWT 생성
    public static String generateToken(String userId, int expireTime) {
        return Jwts.builder()
                .subject(userId)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(expireTime)))
                .signWith(getSecretKey(key))
                .compact();
    }

    // JWT 에서 subject 값 추출
    public static String getUserIdInToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey(key))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public static long getExpireTimeInToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey(key))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .toInstant()
                .getEpochSecond();
    }

}
