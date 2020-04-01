package com.hys.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
	
		//查看源码可知，注解PostMapping就等于@RequestMapping(method=RequestMethod.POST)
	@PostMapping("/user/login")
		//@RequestMapping(value="/user/login",method = RequestMethod.POST)
	public String login(@RequestParam(value = "username")String username,
			@RequestParam(value="password")String password,
			Map<String,String> map,HttpSession session) {
		
		if(!StringUtils.isEmpty(username)&&"123456".equals(password)) {
				//通过拦截器检查
				//只要用户登录就在session中存入，防止重复登录
			session.setAttribute("loginUser",username);
				//此步骤是为了重定向到dashboard.html中
				//转发会有缓存，防止缓存使用重定向
				//在自定义中定义main，相当于就是dashboard.html
			return "redirect:/main";//SpringBoot中默人跳转到thymeleaf中的以html结尾的页面
		}else {
			map.put("message", "用户密码或者账户出错");
			return "login";//SpringBoot中默人跳转到thymeleaf中的以html结尾的页面
		}
	}
	
}
