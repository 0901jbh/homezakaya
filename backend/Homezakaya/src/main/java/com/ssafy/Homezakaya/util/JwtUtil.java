package com.ssafy.Homezakaya.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;


@Component
@Slf4j
public class JwtUtil {
    private final String SALT = "Homezakaya";

    // 어세스 토큰 유효시간 | 20s
    private long accessTokenValidTime = 20 * 1000L; // 30 * 60 * 1000L;
    // 리프레시 토큰 유효시간 | 1m
    private long refreshTokenValidTime = 1 * 60 * 1000L;


    // access token 생성
    // Access Token 생성.


    // 토큰 생성 (데이터 추가로 담기 가능 :배열 등 사용)
    public String createAccessToken(String claimId, String data) throws UnsupportedEncodingException {

        return Jwts.builder().setHeaderParam("alg", "HS256").setHeaderParam("type", "JWT").claim(claimId, data).setExpiration(new Date(System.currentTimeMillis() + accessTokenValidTime)).signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, SALT.getBytes("UTF-8")).compact();
    }

    // Refresh Token 생성.
    public String createRefreshToken(String claimId, String data) throws UnsupportedEncodingException {

        return Jwts.builder().setHeaderParam("alg", "HS256").setHeaderParam("type", "JWT").claim(claimId, data).setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidTime)).signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, SALT.getBytes("UTF-8")).compact();
    }

    // 토큰 정보 반환
    public Map<String, Object> checkAndGetClaims(final String jwt) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(SALT.getBytes()).parseClaimsJws(jwt);
        log.trace("claims: {}", claims);
        // Claims는 Map의 구현체이다.
        return claims.getBody();
    }
}