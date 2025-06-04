package com.api.apibackend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * JWTUtils 类用于生成和解析 JSON Web Token (JWT)
 *
 * @author Seamher
 */
@Component
public class JWTUtils {

    // 定义用于签名的密钥
    private static final String KEY = "icpr";

    /**
     * 生成 JWT 令牌
     *
     * @param claims 包含在令牌中的声明
     * @return 生成的 JWT 令牌
     */
    public static String genToken(Map<String, Object> claims) {
        // 使用 HMAC256 算法生成带有声明和过期时间的令牌
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date((System.currentTimeMillis() + 1000 * 60 * 60 * 12)))
                .sign(Algorithm.HMAC256(KEY));
    }

    /**
     * 解析 JWT 令牌
     *
     * @param token 要解析的 JWT 令牌
     * @return 令牌中的声明
     */
    public static Map<String, Object> parseToken(String token) {
        // 使用 HMAC256 算法验证令牌并获取声明
        return JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }

    /**
     * 解码JWT令牌
     *
     * @param token 要解码的JWT令牌
     * @return 解码后的JWT令牌
     */
    public static DecodedJWT decode(String token) {
        return JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token);
    }
}
