package com.hys.project.entity.VO;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 86191
 *		封装登录成功的id，userName，email信息
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberLoginVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 关联mysql中t_member中的用户主键id
	private Integer id;
	
	// 关联mysql中t_member中的用户昵称user_name
    private String userName;
    
    // 关联mysql中t_member中的用户邮箱email
	private String email;
	
	
	
}
