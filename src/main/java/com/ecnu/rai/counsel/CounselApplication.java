package com.ecnu.rai.counsel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ecnu.rai.counsel.mapper")
public class CounselApplication {

	public static void main(String[] args) {
		SpringApplication.run(CounselApplication.class, args);
	}

}
