package com.hys.controller;
	
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hys.exception.MyDefineException;
	//测试代码，自定义异常
	//测试自己要打印的异常信息
		//编写自己异常(MyDefineException.java)——》抛出自定义异常时被自己的异常解析器解析(MyExceptionHandler.java)
		//——》想要在浏览器中打印自己编写的内容(实现DefaultErrorAttributes或者ErrorAttributes)
@RestController
public class TestMyDefineExceptionController {
	@RequestMapping(value="/hello")
	public String hello(@RequestParam("user")String user) {
		if(user.equals("aaa")) {
			throw new MyDefineException("用户haha");
		}
		return "hello World";
	}
	
}
