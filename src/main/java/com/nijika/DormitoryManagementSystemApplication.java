package com.nijika;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 学生宿舍卫生管理系统 - Spring Boot启动类
 * 功能概览：用户管理、宿舍管理、卫生检查、检查员申请审核
 */
@SpringBootApplication
@MapperScan("com.nijika.mapper") // 扫描Mapper接口，无需@Mapper注解
public class DormitoryManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(DormitoryManagementSystemApplication.class, args);
    }

}
