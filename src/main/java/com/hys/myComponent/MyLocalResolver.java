package com.hys.myComponent;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

/**
 *	LocaleResolver 国际化解析器
 *	可以获得从链接上得到携带来的区域信息，通俗说就是uri后面的get信息 
 *	
 */
public class MyLocalResolver implements LocaleResolver{

	@Override
	//resolverLocale:解析国际化方法
	public Locale resolveLocale(HttpServletRequest request) {
		//获得默认的国际化信息(就是浏览器自带的语言环境)
		Locale locale=Locale.getDefault();
		//获得uri链接上传过来的信息,通过HttpServletRequest获取
		String l=request.getParameter("l");
		//Spring的工具类StringUtils,判断是否为空
		if(!StringUtils.isEmpty(l)) {
			String[] split=l.split("_");
			//Locale的构造方法，new Locale(language,country);
			//language为输入的语言简写,如中文=》zh;country为输入的国家简写，中国=>CN;
			locale=new Locale(split[0],split[1]);
		}
		return locale;
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		// TODO Auto-generated method stub
		
	}

	

}
