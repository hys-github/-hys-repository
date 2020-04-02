package com.hys.project.cofig;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.hys.project.entity.VO.MemberLoginVO;
import com.hys.project.util.JudgeRequestPassSourceUtil;
import com.hys.project.util.ManagerConstant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * @author 86191
 *		把项目中必须登录才能访问的功能保护起来，如果没有登录就访问则跳转到登录页 面
 *			准备不需要登录检查的资源：静态资源
 *			需要经过认证的资源，各服务中的动态web资源路径
 *
 *			request.getServletPath：拿到当前请求的路径(不包括http:ip:port)
 *			RequestContext：
 *		
 */

@Component
public class ZuulConfigFilter extends ZuulFilter {

	@Override
	/**
	 * 	true if the run() method should be invoked. false will not invoke the run() method
	 * 		true：表示该url要走run方法，进行run方法中的一系列认证或者其它要求
	 * 		false：表示直接放行该url
	 */
	public boolean shouldFilter() {
		
		// 通过threadLocal本地线程池获取RequestContext，里面存储了一系列关于httpServletRequest的对象
		RequestContext requestContext = RequestContext.getCurrentContext();
		
		// 得到request域对象
		HttpServletRequest request = requestContext.getRequest();
		
		// 得到请求的url（不包括http://ip:port）
		String servletPath = request.getServletPath();
		
		// 判断servlet是否为可以放行的web url（像登陆页面，注册页面这些肯定是每个人都可以有的权限）
		boolean bool = JudgeRequestPassSourceUtil.webRequestPermitAllSet.contains(servletPath);
		
		// bool为true，表示是应该放行的web url；bool为false时，表示不应该放行，要经过认证才行
		if (bool) {
			
			return false;
		}
		
		// true，表示是web请求或者其它不可预料的请求，都要经过认证; false，表示是静态请求，放行
		boolean flag = JudgeRequestPassSourceUtil.judgeWebUrlOrStaticUrl(servletPath);
		
		return flag;
	}

	@Override
	/**
	 * 	此方法，在shouldFilter方法返回true时调用，false不管
	 */
	public Object run() throws ZuulException {
		
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest request = requestContext.getRequest();
		
		// 得到session域
		HttpSession session = request.getSession();
		
		// 如果经过了登录页面，则在session中存在这个键值对
		// 没有就为空，就要重定向到登陆页面经行登录认证
		MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(ManagerConstant.MESSAGE_TO_HTML_WITH_MEMBERLOGINVO);
		
		if (memberLoginVO==null) {
			
			// 得到重定向的响应response，由于时跨服务器的，所以只能使用重定向，不能使用request转发
			HttpServletResponse response = requestContext.getResponse();
			
			// 将访问失败消息存储到session域中（跨服务器）
			session.setAttribute(ManagerConstant.MESSAGE_TO_HTML, ManagerConstant.MESSAGE_ACCESS_FORBIDEN);
			
			try {
				
				// 重定向到登录页面
				response.sendRedirect("/to/login/page");
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "pre";
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

}
