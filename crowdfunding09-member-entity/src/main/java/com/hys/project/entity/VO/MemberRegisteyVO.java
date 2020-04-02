package com.hys.project.entity.VO;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author 86191
 *		面向用户注册的实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberRegisteyVO implements Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 用户登录账号
    private String loginAcct;
    
    // 用户登录密码
    private String userPswd;
    
    // 用户的昵称
    private String userName;
    
    // 用户eamil
    private String email;
	
    // 注册用户手机号
    private String phoneNum;
    
    // 注册用户的验证码
    private String verifyCode;
}
