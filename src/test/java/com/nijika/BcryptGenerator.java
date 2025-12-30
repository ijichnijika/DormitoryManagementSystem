package com.nijika;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 用于生成BCrypt密码的工具类
 */
public class BcryptGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = "123456";
        String encoded = encoder.encode(password);

        System.out.println("=".repeat(60));
        System.out.println("Password: " + password);
        System.out.println("BCrypt Hash: " + encoded);
        System.out.println("=".repeat(60));

        // 验证
        boolean matches = encoder.matches(password, encoded);
        System.out.println("Verification: " + (matches ? "SUCCESS" : "FAILED"));

        // 同时生成 admin123 的密码用于对比
        String admin123 = encoder.encode("admin123");
        System.out.println("\nPassword: admin123");
        System.out.println("BCrypt Hash: " + admin123);
    }
}
