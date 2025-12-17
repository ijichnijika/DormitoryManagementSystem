package com.nijika.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("学生宿舍卫生管理系统 API")
                        .version("1.0")
                        .description("提供宿舍卫生检查、权限申请等核心业务接口")
                        .contact(new Contact()
                                .name("开发团队")
                                .email("dev@example.com")));
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("全部接口")
                .pathsToMatch("/api/**")
                .packagesToScan("com.nijika.controller")
                .build();
    }
}
