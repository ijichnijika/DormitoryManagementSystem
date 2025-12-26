package com.nijika.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * Knife4j 配置 - 全局添加 Authorization 鉴权参数
 */
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
                                                                .name("nijika")
                                                                .email("yidizhihongxiameow@gmail.com")))
                                // 配置全局鉴权参数 -
                                .components(new Components()
                                                .addSecuritySchemes(HttpHeaders.AUTHORIZATION,
                                                                new SecurityScheme()
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")));
        }

        @Bean
        public GroupedOpenApi allApi() {
                return GroupedOpenApi.builder()
                                .group("全部接口")
                                .pathsToMatch("/api/**")
                                .packagesToScan("com.nijika.controller")
                                .build();
        }

        /**
         * 全局自定义扩展 - 给所有接口添加 Authorization 参数
         * 排除登录和注册接口
         */
        @Bean
        public GlobalOpenApiCustomizer globalOpenApiCustomizer() {
                return openApi -> {
                        // 全局添加鉴权参数
                        if (openApi.getPaths() != null) {
                                openApi.getPaths().forEach((path, pathItem) -> {
                                        // 登录和注册接口不需要添加鉴权参数
                                        if (path.equals("/api/user/login") || path.equals("/api/user/")) {
                                                return;
                                        }
                                        // 其他接口添加鉴权参数
                                        pathItem.readOperations()
                                                        .forEach(operation -> operation.addSecurityItem(
                                                                        new SecurityRequirement().addList(
                                                                                        HttpHeaders.AUTHORIZATION)));
                                });
                        }
                };
        }
}
