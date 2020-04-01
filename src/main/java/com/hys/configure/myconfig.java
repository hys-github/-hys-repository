package com.hys.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.hys.myComponent.LoginHandlerInterceptor;
import com.hys.myComponent.MyLocalResolver;
/**
 * 
 *	//此为自己扩展的webMvc内容，加载到容器中
 *	须继承WebMvcConfigurerAdapter
 */
@SuppressWarnings("deprecation")
@Configuration
public class myconfig extends WebMvcConfigurerAdapter{
	
	@Override
		//默认跳转的配置
		//此方法为在浏览器中加载的uri默认跳到thymeleaf内找相应的以html结尾的页面
	public void addViewControllers(ViewControllerRegistry registry) {

		registry.addViewController("/").setViewName("login");
		registry.addViewController("/index.html").setViewName("login");
		registry.addViewController("/main").setViewName("dashboard");
	}
	@Bean
		//加入国际化bean,springboot默认的LocaleResolver失效(原因有@ConditionalOnMissingBean)
	public LocaleResolver getlocaleResolver() {
		return new MyLocalResolver();
	}
	@Override
		//注册自定义的拦截器添加
		//SpringBoot中，拦截器不会拦截静态资源
	public void addInterceptors(InterceptorRegistry registry) {
			//addPathPatterns(),添加拦截路径,此方法参数可以拦截多个Controller,查看此方法,
			//参数可以一个一个添加，也可以是List<String>类型
			//excludePathPatterns(),排除要拦截的Controller，与addPathPatterns()类似			
				//  “/**”表示拦截所有
		
		registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
		.excludePathPatterns("/","/index.html","/user/login");
		
	}
	
}
