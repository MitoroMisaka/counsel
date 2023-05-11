package com.ecnu.rai.counsel.config;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API文档相关配置
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.xxx.xxx.controller")
                .title("商城项目")
                .description("商城后台接口文档")
                .contactName("xiaobing")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}