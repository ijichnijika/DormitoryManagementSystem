package com.nijika;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.nijika.mapper")
public class DormitoryManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(DormitoryManagementSystemApplication.class, args);
    }

}
