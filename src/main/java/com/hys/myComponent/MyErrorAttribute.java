package com.hys.myComponent;

import java.util.Map;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

//给容器中加入我们自己定义的ErrorAttributes
@Component
public class MyErrorAttribute extends DefaultErrorAttributes {
	
	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
			
			//WebRequest继承与RequestAttributes,此接口实现了从HttpServletRequest中拿到参数
			//通过webRequest.getAttribute("map", 0);第一个参数为在自定义异常处理器中request中存储的key值,第二个参数查看源码
				//此map是从MyExceptionHandler(自定义的异常处理器中)中得到要打印的内容
		Map<String, Object> map = (Map<String, Object>) webRequest.getAttribute("map", 0);
			//return super.getErrorAttributes(webRequest, includeStackTrace);
			//自定义加入自己要打印的内容
				//这个是要返回的Map数据，将在页面中输出，在页面直接使用${msg.xxx}得到map中内容
		Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
		errorAttributes.put("msg", map);
		return errorAttributes;
	}
}
