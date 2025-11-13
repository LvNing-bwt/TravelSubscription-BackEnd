package org.travel.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {

    private static final String secret = "travel-subscription-access-key-2025";
    private static final Long expiration = 7 * 24 * 60 * 60 * 1000L;

    /**
     * JWT生成token
     * @param uid 用户id
     * @return 返回token字符串
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
    public static Claims extractAccessTokenJwt(String jwtToken){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    public static Claims extractRefreshTokenJwt(String jwtToken){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }
}








