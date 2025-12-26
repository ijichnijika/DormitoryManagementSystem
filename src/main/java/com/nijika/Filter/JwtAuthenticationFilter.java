package com.nijika.Filter;

import com.nijika.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT认证过滤器
 * 功能：拦截所有HTTP请求，从Authorization头提取JWT并验证，将用户信息存入SecurityContext
 * 执行时机：在UsernamePasswordAuthenticationFilter之前执行
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    /**
     * 核心过滤逻辑
     * 1. 从请求头提取JWT Token（格式：Bearer xxx）
     * 2. 验证Token有效性（签名、过期时间）
     * 3. 解析Token获取用户名和角色
     * 4. 构造Spring Security认证对象并存入SecurityContext
     * 5. 放行请求到下一个过滤器
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // 去除"Bearer "前缀

            try {
                if (jwtUtil.validateToken(token)) { // 验证Token（签名+过期时间）
                    String username = jwtUtil.getUsernameFromToken(token);
                    String role = jwtUtil.getRoleFromToken(token);

                    // 构造认证对象（用户名、凭证null、角色列表）
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)));

                    // 存入SecurityContext，后续可通过SecurityContextHolder获取
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Token解析失败，不设置认证信息，后续需登录的接口会被拦截
            }
        }

        filterChain.doFilter(request, response); // 放行到下一个过滤器
    }
}
