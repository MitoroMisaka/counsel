package com.ecnu.rai.counsel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@MapperScan("com.ecnu.rai.counsel.mapper")
public class CounselApplication {

    public static void main(String[] args) {
        SpringApplication.run(CounselApplication.class, args);
    }


}
