package com.nijika.config;

import com.nijika.Filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security安全配置
 * 核心功能：JWT无状态认证 + 接口权限控制
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 配置安全过滤链
     * 1. 禁用CSRF
     * 2. 无状态会话管理（不使用Session）
     * 3. 配置接口权限：登录/注册/API文档公开，其他接口需认证
     * 4. 添加JWT过滤器到认证链
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/user/login").permitAll() // 登录接口公开
                        .requestMatchers("/api/user", "/api/user/").permitAll() // 注册接口公开(支持带或不带斜杠)
                        .requestMatchers("/doc.html", "/webjars/**", "/v3/api-docs/**", "/swagger-ui/**",
                                "/swagger-resources/**").permitAll() // Knife4j文档公开
                        .anyRequest().authenticated()) // 其他接口需认证
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JWT过滤器优先执行
        return http.build();
    }

    /**
     * 密码编码器 - 使用BCrypt单向加密算法
     * BCrypt特点：自动加盐、可配置强度、同一密码每次加密结果不同
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
