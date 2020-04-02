package com.hys.project.controller;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hys.project.config.SendShortMessageProperties;
import com.hys.project.entity.MemberPO;
import com.hys.project.entity.VO.MemberLoginVO;
import com.hys.project.entity.VO.MemberRegisteyVO;
import com.hys.project.service.MysqlRemoteService;
import com.hys.project.service.RedisRemoteService;
import com.hys.project.util.AjaxResult;
import com.hys.project.util.ManagerConstant;
import com.hys.project.util.ShortMessageVerifyUtil;

@Controller
public class AuthController {
	
	// 远程接口，在调用之前，必须加入注解@EnableFeignClients
	@Autowired
	RedisRemoteService redisRemoteService;
	
	// 远程接口，在调用之前，必须加入注解@EnableFeignClients
	@Autowired
	MysqlRemoteService mysqlRemoteService;
	
	// 自定义的配置文件
	@Autowired
	SendShortMessageProperties sendShortMessageProperties;
	
	private Logger logger=LoggerFactory.getLogger(AuthController.class);
	/**
	 * @return
	 * 		去登陆页面
	 */
	@RequestMapping("/")
	public String toAuthPage() {
		
		return "authPage";
	}
	
	@RequestMapping("/to/logout/page")
	public String toLogout(HttpSession session) {
		
		// 消除所有session记录
		session.invalidate();
		
		return "redirect:http://www.hys.com/to/login/page";
	}
	
	
	/**
	 * @param phone 从注册信息中得到手机号码进行获取短信验证码
	 * @return
	 * 		发送验证码给用户的controller
	 */
	@ResponseBody
	@RequestMapping("/to/auth/to/send/message.json")
	public AjaxResult<String> toSendMessageToAdmin(@RequestParam("phoneNum")String phone){
		
		// 从自定义的文件中取出相关的属性
		String host = sendShortMessageProperties.getHost();
		String path = sendShortMessageProperties.getPath();
		String appcode = sendShortMessageProperties.getAppcode();
		String method = sendShortMessageProperties.getMethod();
		String sign = sendShortMessageProperties.getSign();
		String skin = sendShortMessageProperties.getSkin();
		
		logger.info("host="+host);
		
		// 发送验证码到phone用户的短信验证码,并返回
		AjaxResult<String> ajaxResult = ShortMessageVerifyUtil.sendShortMessageToPhone(host, path, method, appcode, phone, sign, skin);
		
		// 判断验证码是否发送成功
		// 成功，将成功的验证码存储到redis中
		// 失败直接返回
		if(ajaxResult.getOperationResult()=="SUCCESS") {
			
			// 封装存如redis中的键
			String key=ManagerConstant.REDIS_SAVE_PHONE_IN_TIME+phone;
			
			// 得到发送短信成功的验证码
			String value=ajaxResult.getQueryData();
			
			// 设置过期时间,类型为long
			Long time=2L;
			
			// 设置过期时间的单位是分
			TimeUnit timeUnit = TimeUnit.MINUTES;
			
			// 将验证码有时间限制的存入到redis中
			AjaxResult<String> redisAjaxResult=redisRemoteService.setRedisKeyValueWithTimeout(key,value,time,timeUnit);
			
			// 无论远程服务器连接redis是否存储成功，返回，相关判断在redis服务器中查看
			// 成功，返回的是AjaxResult.successWithoutData()
			// 失败，返回AjaxResultfailed(String message)
			return redisAjaxResult;
			
		}else {
			
			// 此处返回的是发送短信验证到用户手机上失败或者过程出现异常的信息
			return ajaxResult;
			
		}
		
	}
	
	
	/**
	 * @param memberRegisteyVO	封装从注册表单提交过来的属性的视图层实体类
	 * @return
	 * 		控制器，判断redis验证码比较和mysql的插入注册人信息
	 * 			成功，重定向到的登陆页面
	 * 			失败，转发到注册页面并打印出错的消息注册页面	
	 */
	@RequestMapping("/to/auth/to/login/page")
	public String toLoginPage( MemberRegisteyVO memberRegisteyVO,ModelMap mav) {
		
		// 得到表单中的电话号码
		String phone=memberRegisteyVO.getPhoneNum();
		
		// 拼接验证码的键（在redis中存储）
		String key=ManagerConstant.REDIS_SAVE_PHONE_IN_TIME+phone;
		
		// 从redis远端服务器中取的验证码
		AjaxResult<String> redisAjaxResult=redisRemoteService.getRedisValueBykey(key);
		
		// 判断从redis中是否发生了异常，执行下面if中的代码
		if(AjaxResult.FAILED.equals(redisAjaxResult.getOperationResult())) {
			
			// 得到异常消息
			String message = redisAjaxResult.getOperationMessage();
			
			// 将异常消息存到ModelAndView中，返回到页面打印
			mav.addAttribute(ManagerConstant.MESSAGE_TO_HTML,message);
			
			// 跳转到注册页面
			return "register-page";
		}
		
		// 得到从redis中查询到的验证码（redis中）
		String verifyCodeRedis=redisAjaxResult.getQueryData();
		
		// 得到验证码（从表格中)
		String verifyCodeForm=memberRegisteyVO.getVerifyCode();
		
		// 比较从表单得到的验证码是否与redis中存储的一致
		// 不一致，执行if中的代码
		if(!(Objects.equals(verifyCodeForm, verifyCodeRedis))) {
			
			// 返回输入验证码不匹配的异常
			mav.addAttribute(ManagerConstant.MESSAGE_TO_HTML,ManagerConstant.REDIS_VERIFY_CODE_INVALID);
			
			// 跳转到注册页面
			return "register-page";
		}
		
		// 声明一个MySQL的实体类
		MemberPO memberPO = new MemberPO();
		
		// 通过BeanUtils将memberRegisteyVO和memberPO中对应的属性传递
		BeanUtils.copyProperties(memberRegisteyVO,memberPO );
		
		// 得到从表单中得到的密码
		String userPswdBeforeEncode = memberPO.getUserPswd();
		
		// spring security的加密类，每次登录使用不同的盐值进行加密
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		// 得到加密后的密码
		String userpasswordAfterEncode = passwordEncoder.encode(userPswdBeforeEncode);
		
		// 设置到对象中
		memberPO.setUserPswd(userpasswordAfterEncode);
		
		// 得到mysql数据库中插入操作返回的信息
		AjaxResult<String> mysqlAjaxResult=mysqlRemoteService.insertMemberPO(memberPO);
		
		// 判断如果mysql中出现了异常，执行以下if中的代码
		if(AjaxResult.FAILED.equals(mysqlAjaxResult.getOperationResult())) {
			
			// 得到异常消息
			String message=mysqlAjaxResult.getOperationMessage();
			
			// 将异常消息存到ModelAndView中，返回到页面打印
			mav.addAttribute(ManagerConstant.MESSAGE_TO_HTML, message);
			
			// 跳转到注册页面
			return "register-page";
		}
		
		// 如果以上redis的判断验证码成功和mysql的数据插入成功，就将redis中缓存的手机验证码删除，防止重复验证
		// 这个就不判断成功与否了，因为跟用户没啥关系，此时是后台管理人员的事
		// 所以判断都在redis服务器中
		redisRemoteService.deleteRedisValueByKey(key);
		
		// 如果以上判断都没出错，则注册成功，重定向到登陆页面
		// 采用重定向，防止浏览器刷新重复提交
		return "redirect:http://www.hys.com/to/login/page";
	}
	
	
	/**
	 * @param modelMap		
	 * @param loginAcct	用户登录账号
	 * @param userPswd	用户登录密码
	 * @return
	 * 		判断登录是否成功，成功重定向到主页面，将部分用户信息存储到session中。
	 * 		失败跳转到登录页面，并将打印失败消息在页面上
	 */
	@RequestMapping("/to/auth/main/page")
	public String toMainPage(ModelMap modelMap,HttpSession session,
			@RequestParam("loginAcct")String loginAcct,
			@RequestParam("userPswd")String userPswd) {
		
		logger.info("loginAcct="+loginAcct+"\t"+"userPswd="+userPswd);
		
		// 通过账号从mysql中查询用户的信息，并封装到AjaxResult中
		AjaxResult<MemberPO> mysqlAjaxResult=mysqlRemoteService.queryMemberPOByLoginAcct(loginAcct);
		
		// 如果在操作数据库中出现失败或者异常
		if(AjaxResult.FAILED.equals(mysqlAjaxResult.getOperationResult())) {
			
			// 得到异常消息
			String message= mysqlAjaxResult.getOperationMessage();
			
			// 将异常消息添加到request域中
			modelMap.addAttribute(ManagerConstant.MESSAGE_TO_HTML, message);
			
			// 设置跳转页面
			return "login-page";
		}
		
		// 如果操作数据库成功,传过来的就是MemberPO的对象
		MemberPO memberPO=mysqlAjaxResult.getQueryData();
		
		// 采用spring security中的加密方式，对表单中的密码进行加密，与数据库中的密码比较
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		// 得到从数据库中的密码（mysql）
		String userPswdMysql=memberPO.getUserPswd();
		
		// 由于加密的方式使用的是spring security提供的加密类。所以必须使用器加密类的比较密码的方法，或者肯定失败
		// spring security提供的加密，每次登录采用的盐值都不一样，所以，只有它自己的方法能够比较
		boolean matches = passwordEncoder.matches(userPswd, userPswdMysql);
		
		// 将数据库中的密码与表单中的密码进行比较，失败，执行if中的代码
		if(!matches) {
			
			// 登录失败打印异常信息到登录页面上
			modelMap.addAttribute(ManagerConstant.MESSAGE_TO_HTML, ManagerConstant.MESSAGE_LOGIN_FAILED);
			
			// 设置跳转页面
			return "login-page";
			
		}
		
		// 如果从数据库中取值成功且密码比较也成功
		// 并将id，username（昵称）,email封装到MemberLoginVO中，存储到session中
		MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(),memberPO.getUserName(),memberPO.getEmail());
		
		// 将用户部分信息存储到session，在主页面显示
		session.setAttribute(ManagerConstant.MESSAGE_TO_HTML_WITH_MEMBERLOGINVO, memberLoginVO);
		
		return "redirect:http://www.hys.com/to/main/page";
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		
		
		// 从自定义的文件中取出相关的属性
				String host ="https://feginesms.market.alicloudapi.com" ;
				String path = "/codeNotice";
				String appcode = "3bc43cc2347a425cbb21022c5f1b04f7";
				String method = "GET";
				String sign = "1";
				String skin = "10";
		
				ShortMessageVerifyUtil.sendShortMessageToPhone(host, path, method, appcode, "19138577983888", sign, skin);
				
	}
	
	
}
