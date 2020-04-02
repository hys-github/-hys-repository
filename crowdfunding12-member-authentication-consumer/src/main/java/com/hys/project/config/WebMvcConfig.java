package com.hys.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		
		// 相当于控制器中一个方法上的url
		String urlPath="/to/auth/to/register/page";
		
		// 跳转到那个视图,与classpath下的templates拼接，并以html结尾
		String viewName="register-page";
		
		// 跳转到注册页面
		registry.addViewController(urlPath).setViewName(viewName);
		
		// 跳转到登录页面
		registry.addViewController("/to/login/page").setViewName("login-page");
		
		// 跳转到主页面
		registry.addViewController("/to/main/page").setViewName("main-page");
		
		registry.addViewController("/to/crowd/page/controller").setViewName("crowd-page");
		
		// 重定向
		//registry.addRedirectViewController("/to/login/page", "login-page");
	
	}
	
	
	
	
	
}
