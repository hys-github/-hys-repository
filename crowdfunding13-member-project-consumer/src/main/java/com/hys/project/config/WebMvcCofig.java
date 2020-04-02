package com.hys.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcCofig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		
		
		
		// 相当于控制器中一个方法上的url
		String urlPath = "/to/agree/protocol/page/controller";

		// 跳转到那个视图,与classpath下的templates拼接，并以html结尾
		String viewName = "protocol-page";
		
		// 点击发起众筹，跳转到同意协议页面
		registry.addViewController(urlPath).setViewName(viewName);
		
		// 点击同意，跳转到launch-project-page
		registry.addViewController("/launch/project/page/controller").setViewName("launch-project-page");
		
		registry.addViewController("/to/return/project/page").setViewName("return-project-page");
		
		registry.addViewController("/to/confirm/project/page").setViewName("confirm-project-page");
		
		registry.addViewController("/to/success/page").setViewName("success-project-page");
	}

}
