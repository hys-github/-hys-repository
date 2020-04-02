package com.hys.project.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectPO {

	// 这个项目在mysql中的id
	private Integer id;

	// 这个id关联的是这个项目发起人的主键id，也即是这个登录用户的id
	private Integer memberId;

	// 项目名称
	private String projectName;

	// 项目描述
	private String projectDescription;

	// 计划筹集金额
	private Integer money;

	// 计划筹集天数
	private Integer day;

	// 0-即将开始，1-众筹中，2-众筹成功，3-众筹失败
	private Integer status;

	// 项目创建时间，即创建这个项目的开始时间
	private String createDate;

	// 项目发起时间，就是开启众筹的时间，提交众筹后，还要经过管理员审核后才能部署
	private String deployDate;

	// 已筹集到的金额
	private Long supportMoney;

	// 支持人数
	private Integer supporter;

	// 百分比完成度
	private Integer completion;

	// 关注人数
	private Integer follower;

	// 头图路径
	private String headerPicturePath;

}