package com.hys.project.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
/**
 * 
 * @author 86191
 *			面向数据库的实体类
 *				封装了用户一系列的信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MemberPO implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
    
    // 用户登录账号
    private String loginAcct;
    
    // 用户登录密码
    private String userPswd;
    
    // 用户的昵称
    private String userName;
    
    // 用户eamil
    private String email;
    
    // 实名认证状态      0 - 未实名认证， 1 - 实名认证申 请中， 2 - 已实名认证',
    private Integer authStatus;

    // 0 - 个人， 1 - 企业
    private Integer usertype;

    // 真实姓名
    private String realName;
    
    // 标识号
    private String cardNum;

    // '0 - 企业， 1 - 个体， 2 - 个人， 3 - 政府',
    private Integer acctType;

   
}