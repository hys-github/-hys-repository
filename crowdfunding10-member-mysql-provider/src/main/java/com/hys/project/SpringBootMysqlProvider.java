package com.hys.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@EnableEurekaClient
@SpringBootApplication
@MapperScan("com.hys.project.mapper")
public class SpringBootMysqlProvider {
	
	public static void main(String[] args) {
		
		SpringApplication.run(SpringBootMysqlProvider.class, args);
		
	}
	
}
