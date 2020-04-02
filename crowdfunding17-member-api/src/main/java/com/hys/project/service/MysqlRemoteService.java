package com.hys.project.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hys.project.entity.MemberPO;
import com.hys.project.entity.VO.ProjectVO;
import com.hys.project.util.AjaxResult;

@FeignClient("PROJECT-MYSQL-PROVIDER")
public interface MysqlRemoteService {
	
	@RequestMapping("/get/member/po/by/id")
	public AjaxResult<MemberPO> queryMemberPOById(@RequestParam("id")Integer id);
	
	
	@RequestMapping("/to/mysql/insert/memberPO")
	public AjaxResult<String> insertMemberPO(@RequestBody MemberPO memberPO);

	@RequestMapping("/to/mysql/query/memberPO/by/acct")
	public AjaxResult<MemberPO> queryMemberPOByLoginAcct(@RequestParam("loginAcct")String loginAcct);

	
	@RequestMapping("/to/mysql/save/project/info")
	public AjaxResult<String> saveProjectVOToMysql(@RequestBody ProjectVO projectVO);


	
}
