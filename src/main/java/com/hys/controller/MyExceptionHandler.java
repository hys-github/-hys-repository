package com.hys.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hys.exception.MyDefineException;
	//异常处理器注解
@ControllerAdvice
	//在异常界面时，打印我们自己定义的异常信息
public class MyExceptionHandler {

//第一种：完全自定义返回内容，无自适应效果
	//没有结合SpringBoot提供的自适应异常信息
	
		//当出现MyDefineException异常时，返回此异常处理器(自定义异常，一般都是抛出声明,也可以在注解括号中加入其它系统已给出的异常)
		//且以json格式返回给浏览器客服端(map中的内容返回)
//	@ResponseBody
//		//异常处理。标注处理什么异常,里面加入我自定义异常
//	@ExceptionHandler(MyDefineException.class)
//	public Map<String,Object> handlerException(Exception e) {
//			//使用Map集合，加入一些我们自定义打印的内容
//		Map<String,Object> map=new HashMap<>();
//		map.put("code", "用户不存在异常");
//		map.put("message", e.getMessage());
//		return map;
//	}
	
	//第二种:结合SpringBoot异常信息，进行自适应效果
			//异常处理。标注处理什么异常,里面加入我自定义异常
		@ExceptionHandler(MyDefineException.class)
		public String handlerException(Exception e,HttpServletRequest req) {
				//设置我们自己的状态码,或者错误页面的状态码一直是200，进入到SpringBoot默认的错误页面
				//设置状态码,java.servlet.error.status_code
						//源码中设置的状态码使用的是request从请求头得到的，所以使用request设置，
						//且key值为java.servlet.error.status_code,进入到我们自定义/error/4xx或者5xx的页面
			req.setAttribute("javax.servlet.error.status_code", 500);
				//使用Map集合，加入一些我们自定义打印的内容
				//此时放在map中的内容，在/error下的页面是拿不到的
			Map<String,Object> map=new HashMap<>();
			map.put("code", "用户不存在异常");
			map.put("message", "=============");
				//将map加入到request中，在通过自定义继承DefalutErrorAttributes类的的实现类来完成页面得到我们想打印出来的信息
					/**
					 * 
				@Bean
				@ConditionalOnMissingBean(value = ErrorAttributes.class, search = SearchStrategy.CURRENT)
				public DefaultErrorAttributes errorAttributes() {
					return new DefaultErrorAttributes(this.serverProperties.getError().isIncludeException());
				}
					 */
				//也就是给容器中加入我们自定义的ErrorAttribute, ErrorAttributes.class
				//此时才能在页面中得到我们想要打印的内容
			
			req.setAttribute("map", map);
				//配合SpringBoot默认使用的机制，在templates页面中加入error文件夹
				//出现异常跳转到templates下的/error下的4xx或者5xx等响应错误状态码的html页面
			return "forward:/error";
		}
	
}
