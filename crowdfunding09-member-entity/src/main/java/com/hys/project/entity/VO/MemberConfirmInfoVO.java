package com.hys.project.entity.VO;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberConfirmInfoVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	// 易付宝账号
	private String payNum; 
	
	// 法人身份证号 
	private String cardNum;
	
	
	
	
	
}
