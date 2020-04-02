package com.hys.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class SpringCloudRegistryEureka {
	
	public static void main(String[] args) {
		
		SpringApplication.run(SpringCloudRegistryEureka.class, args);
		
	}

}
