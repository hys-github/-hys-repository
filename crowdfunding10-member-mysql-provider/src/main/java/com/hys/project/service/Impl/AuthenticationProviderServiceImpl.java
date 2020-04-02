package com.hys.project.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hys.project.entity.MemberPO;
import com.hys.project.mapper.MemberPOMapper;
import com.hys.project.service.AuthenticationProviderService;
import com.hys.project.util.AjaxResult;
import com.hys.project.util.ManagerConstant;

@Service
public class AuthenticationProviderServiceImpl implements AuthenticationProviderService {
	
	@Autowired
	MemberPOMapper memberPOMapper;
	
	@Override
	public MemberPO queryMemberPOById(Integer id) {
	
		try {
			
			MemberPO memberPO = memberPOMapper.selectByPrimaryKey(id);
			
			return memberPO;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	
	}
	
	/**
	 * 		向mysql中插入用户（memberPO）信息
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
	public AjaxResult<String> insertMemberPO(MemberPO memberPO) {
		
		try {
			
			// 插入数据
			memberPOMapper.insert(memberPO);
			
			AjaxResult<String> successWithoutData = AjaxResult.successWithoutData();
			
			return successWithoutData;
			
		} catch (Exception e) {
		
			e.printStackTrace();
			
			// 判断出现的异常是否为数据库中账号（unique）重复问题
			if(e instanceof DuplicateKeyException) {
				
				AjaxResult<String> failed = AjaxResult.failed(ManagerConstant.MESSAGE_LOGIN_ACCT_IN_USE);
				
				return failed;
			}
			
			// 如果是其它不可预知的错误，执行该异常代码返回信息
			return AjaxResult.failed(e.getMessage());
		}
	}
	
	/**
	 * 		mysql：查询用户同过用户的账号
	 */
	@Override
	public AjaxResult<MemberPO> queryMemberPOByLoginAcct(String loginAcct) {
		
		try {
			
			MemberPO memberPO = memberPOMapper.queryMemberPOByLoginAcct(loginAcct);
			
			// 判断memberPO是否有值
			if (memberPO==null) {
				
				// 返回信息该用户不存在
				return AjaxResult.failed(ManagerConstant.MESSAGE_NO_PEOPLE_IN_MYSQL);
			}
			
			// 有值，返回
			AjaxResult<MemberPO> successWithData = AjaxResult.successWithData(memberPO);
			
			return successWithData;
		} catch (Exception e) {
			
			e.printStackTrace();
			
			// 抛出异常
			AjaxResult<MemberPO> failed = AjaxResult.failed(e.getMessage());
			
			return failed;
		}
		
	}

}
