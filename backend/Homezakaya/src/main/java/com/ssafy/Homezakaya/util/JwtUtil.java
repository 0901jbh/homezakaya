package com.ssafy.Homezakaya.util;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class JwtUtil {
    private final String SALT = "Homezakaya";

    // 토큰 생성
    public String createToken(String claimId, String data) throws UnsupportedEncodingException {
        return Jwts.builder().setHeaderParam("alg", "HS256").setHeaderParam("type", "JWT").claim(claimId, data).signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, SALT.getBytes("UTF-8")).compact();
    }
    // 유효성 검사
    public void valid(String token) throws Exception {
        Jwts.parser().setSigningKey(SALT.getBytes("UTF-8")).parseClaimsJws(token);
    }
}
