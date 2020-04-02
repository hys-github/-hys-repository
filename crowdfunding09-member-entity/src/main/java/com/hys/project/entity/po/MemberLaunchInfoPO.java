package com.hys.project.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberLaunchInfoPO {
	
	// 项目发起人在数据库中的主键id
    private Integer id;

    // 项目发起人的主键id，这个id关联发起人的所有信息
    private Integer memberId;

    // 项目发起人的简洁
    private String descriptionSimple;

    // 项目发起人的详细描述
    private String descriptionDetail;

    // 项目发起人的私人电话号码
    private String phoneNum;

    // 项目发起人的客服电话号码
    private String serviceNum;

    
}