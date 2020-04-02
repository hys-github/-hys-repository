package com.hys.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.hys.project")
@EnableEurekaClient
@SpringBootApplication
public class SpringBootAuthConsumer {
	
	public static void main(String[] args) {
		
		SpringApplication.run(SpringBootAuthConsumer.class, args);
		
	}
	
	
}
