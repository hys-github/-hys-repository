package com.hys.project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hys.project.entity.MemberPO;
import com.hys.project.service.AuthenticationProviderService;
import com.hys.project.util.AjaxResult;
/**
 * @author 86191
 *			这个handler关联crowdfunding12-member-authentication-consumer这个服务器
 *				主要操作的mysql数据库为：MemberPO，成员登录信息等
 */
@RestController
public class AuthenticationProviderController {
	
	@Autowired
	AuthenticationProviderService memberPOService;
	
	private Logger logger=LoggerFactory.getLogger(AuthenticationProviderController.class);
	
	/**
	 * @param id
	 * @return
	 * 		得到数据memberPO对象通过主键id
	 * 
	 */
	@RequestMapping("/get/member/po/by/id")
	public AjaxResult<MemberPO> queryMemberPOById(@RequestParam("id")Integer id){
		
		MemberPO memberPO=memberPOService.queryMemberPOById(id);
		
		AjaxResult<MemberPO> successWithData = AjaxResult.successWithData(memberPO);
		
		return successWithData;
	}
	
	/**
	 * @param memberPO
	 * @return
	 * 		向mysql中插入对象
	 */
	@RequestMapping("/to/mysql/insert/memberPO")
	public AjaxResult<String> insertMemberPO(@RequestBody MemberPO memberPO){
		
		// 打印日志
		logger.info("memberPO="+memberPO);
		
		AjaxResult<String> mysqAjaxResult=memberPOService.insertMemberPO(memberPO);
		
		return mysqAjaxResult;
	}
	
	
	/**
	 * @param loginAcct	登录用户的账号
	 * @return	
	 * 		查询用户通过账号
	 */
	@RequestMapping("/to/mysql/query/memberPO/by/acct")
	public AjaxResult<MemberPO> queryMemberPOByLoginAcct(@RequestParam("loginAcct")String loginAcct){
		
		
		AjaxResult<MemberPO> mysqlAjaxResult=memberPOService.queryMemberPOByLoginAcct(loginAcct);
		
		return mysqlAjaxResult;
	}
	
	
	
	
}
