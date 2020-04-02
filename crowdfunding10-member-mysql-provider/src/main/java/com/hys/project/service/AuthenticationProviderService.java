package com.hys.project.service;

import com.hys.project.entity.MemberPO;
import com.hys.project.util.AjaxResult;

public interface AuthenticationProviderService {

	MemberPO queryMemberPOById(Integer id);

	AjaxResult<String> insertMemberPO(MemberPO memberPO);

	AjaxResult<MemberPO> queryMemberPOByLoginAcct(String loginAcct);

}
