package com.nijika.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类 - 核心逻辑：无状态认证，Token中携带userId+role
 */
@Component
public class JwtUtil {
    @Value("${jwt.secret:dormitory-management-system-secret-key-2025-very-secure}") // 配置文件可覆盖
    private String secret;
    @Value("${jwt.expiration:86400000}") // 默认24小时(毫秒)
    private Long expiration;

    private SecretKey getSigningKey() { // HMAC-SHA256签名密钥
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username, Long userId, String role) { // 登录成功后生成Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId); // Payload携带用户ID
        claims.put("role", role); // Payload携带角色字符串（如"STUDENT,INSPECTOR"）
        return Jwts.builder()
                .claims(claims)
                .subject(username) // 标准字段：主题(用户名)
                .issuedAt(new Date()) // 标准字段：签发时间
                .expiration(new Date(System.currentTimeMillis() + expiration)) // 标准字段：过期时间
                .signWith(getSigningKey()) // 使用HMAC-SHA256签名
                .compact();
    }

    public Claims getClaimsFromToken(String token) { // 解析并验证Token
        return Jwts.parser()
                .verifyWith(getSigningKey()) // 验证签名
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsernameFromToken(String token) { // 从Token提取用户名
        return getClaimsFromToken(token).getSubject();
    }

    public Long getUserIdFromToken(String token) { // 从Token提取用户ID
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }

    public String getRoleFromToken(String token) { // 从Token提取角色字符串（前端路由守卫需要）
        Claims claims = getClaimsFromToken(token);
        return claims.get("role", String.class);
    }

    public boolean validateToken(String token) { // 验证Token是否过期
        try {
            Claims claims = getClaimsFromToken(token);
            return !claims.getExpiration().before(new Date()); // 过期时间晚于当前时间=有效
        } catch (Exception e) { // 签名错误、格式错误等异常=无效
            return false;
        }
    }
}
