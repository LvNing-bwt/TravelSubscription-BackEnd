package me.hearta.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {

    private static final String secret = "travel-subscription-dev-key-2025-for-learning";
    private static final Long expiration = 604800000L;

    /**
     * 根据用户ID生成JWT Token
     * @param uid 用户唯一标识符
     * @return JWT Token字符串
     */
    public static String generateToken(Long uid){
        long timeMillis = System.currentTimeMillis();
        return Jwts.builder()
                // 载荷（payload）
                .subject(uid.toString())
                .issuedAt(new Date(timeMillis))
                .expiration(new Date(timeMillis + expiration))
                // 签名（signature）
                .signWith(getSigningKey())
                .compact();
    }

    private static SecretKey getSigningKey(){
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 解析出token中的uid
     * @param jwtToken 字符串类型token
     * @return 解析出的uid(Long)
     */
    public static Claims extractJwt(String jwtToken){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }
}








