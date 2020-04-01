package com.hys.myComponent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
/**
 *		
 * 	自定义拦截器，实现HandlerInterceptor接口
 * 	与springboot中默认的拦截器一起作用，所以只要在我们扩展的
 * 	springMvc中使用继承WebMvcConfigurerAdapter中相应添加拦截器方法就行
 * 	拦截用户登录检查
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {
	
	@Override
	//在Controller之前执行，false，阻止进入controller
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
			//从session得到用户名
		Object user = request.getSession().getAttribute("loginUser");
			//判断user是否为空
		if(StringUtils.isEmpty(user)) {
			 //未登录，转发到登录页面
			request.setAttribute("message", "没有权限，请先登录");
			request.getRequestDispatcher("/").forward(request, response);
			return false;
		}else {
				//登录成功
			return true;
		}
	}
	@Override
		//controller执行后执行，在静茹jsp之前
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
