package com.hys.project.service.Impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hys.project.entity.VO.MemberConfirmInfoVO;
import com.hys.project.entity.VO.MemberLauchInfoVO;
import com.hys.project.entity.VO.ProjectVO;
import com.hys.project.entity.VO.ReturnVO;
import com.hys.project.entity.po.MemberConfirmInfoPO;
import com.hys.project.entity.po.MemberLaunchInfoPO;
import com.hys.project.entity.po.ProjectPO;
import com.hys.project.entity.po.ReturnPO;
import com.hys.project.mapper.MemberConfirmInfoPOMapper;
import com.hys.project.mapper.MemberLaunchInfoPOMapper;
import com.hys.project.mapper.ProjectPOMapper;
import com.hys.project.mapper.ReturnPOMapper;
import com.hys.project.service.ProjectProviderService;
import com.hys.project.util.AjaxResult;

@Service
@Transactional(readOnly = true)
public class ProjectProviderServiceImpl implements ProjectProviderService {
	
	
	@Autowired
	ProjectPOMapper projectPOMapper;
	
	@Autowired
	ReturnPOMapper returnPOMapper;
	
	@Autowired
	MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;
	
	@Autowired
	MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;
	
	
	
	static final Logger logger=LoggerFactory.getLogger(ProjectProviderServiceImpl.class);
	
	/**
	 * 		将众筹的页面信息等存入数据库中
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class,readOnly = false)
	public AjaxResult<String> saveProjectVOToMysql(ProjectVO projectVO) {
		
			// 一、保存ProjectPO对象，将项目信息保存到mysql数据库中
			// 1.创建空的ProjectPO对象
			ProjectPO projectPO=new ProjectPO();
			
			// 2.把projectVO中的属性复制到projectPO中,将属性一致的复制到projectPO中
			BeanUtils.copyProperties(projectVO, projectPO);
			logger.info("projectPO==="+projectPO);
			
			// 修复bug：生成项目创建时间存入
			String format = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
			projectPO.setCreateDate(format);
			
			// 修复bug：status设置成0，表示即将开始,表示项目才刚创建，还没有开始众筹发布
			projectPO.setStatus(0);
			
			// 3.保存projectPO
			// 为了能够获取到projectPO保存后的自增主键，需要到ProjectPOMapper.xml文件中进行相关设置
			// <insert id="insertSelective" useGeneratedKeys="true" keyProperty="id" ……
			// 返回的自增主键是返回到插入的对象里面的，而不是下面这个方法的返回值，方法返回值是☞插入成功了几行
			logger.info("projectPO==="+projectPO);
			projectPOMapper.insertSelective(projectPO);
			logger.info("projectPO==="+projectPO);
			
			// 4.从projectPO对象这里获取自增主键
			Integer projectId = projectPO.getId();
			
			// 二、保存项目、分类的关联关系信息
			// 1.从ProjectVO对象中获取typeIdList
			List<Integer> typeIdList = projectVO.getTypeIdList();
			
			// 关联表t_project_type
			projectPOMapper.insertInfoTot_project_typeByProjectIdAndTypeIdList(projectId,typeIdList);
			
			// 三、保存项目、标签的关联关系信息,
			List<Integer> tagIdList = projectVO.getTagIdList();
			
			// 插入t_project_tag项目与标签的关联表，一个项目多个标签
			projectPOMapper.insertInfoTot_project_tagByProjectIdAndTagId(projectId,tagIdList);
			
			// 四、保存项目中详情图片路径信息,项目关联图片详情的表t_project_item_pic
			List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
			
			projectPOMapper.insertInfoTot_project_item_picByProjectIdAndPicturePath(projectId,detailPicturePathList);
			
			// 五、保存项目发起人信息
			MemberLauchInfoVO memberLauchInfoVO = projectVO.getMemberLauchInfoVO();
			
			// new一个MemberLaunchInfoPO
			MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
			
			// 将前面对象属性与后面对象属性一致的值复制到后面对象
			BeanUtils.copyProperties(memberLauchInfoVO, memberLaunchInfoPO);
			memberLaunchInfoPO.setMemberId(projectVO.getMemberId());
			
			memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);
			
			// 六、保存项目回报信息
			List<ReturnVO> returnVOList = projectVO.getReturnVOList();
			
			// 创建数据库的回报信息集合
			List<ReturnPO> returnPOList=new ArrayList<>();
			
			// 遍历从页面上拿到的回报信息
			for (ReturnVO returnVO : returnVOList) {
				
				// new一个回报信息到数据库的对象
				ReturnPO returnPO = new ReturnPO();
				
				// 将项目的主键id注入，表示这个回报信息关联哪一个项目
				returnPO.setProjectId(projectId);
				
				// 将前面对象属性与后面对象属性一致的值复制到后面对象
				BeanUtils.copyProperties(returnVO, returnPO);
				
				// 添加到回报信息到数据库中的集合中
				returnPOList.add(returnPO);
			}
			
			logger.info(returnPOList.toString());
			// 将回报信息批量插入到数据库中
			returnPOMapper.insertReturnPOList(returnPOList);
			logger.info("returnPOList==="+returnPOList.toString());
			
			// 七、保存项目确认信息
			MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
			
			MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
			
			BeanUtils.copyProperties(memberConfirmInfoVO, memberConfirmInfoPO);
			
			memberConfirmInfoPO.setMemberId(projectVO.getMemberId());
			
			memberConfirmInfoPOMapper.insert(memberConfirmInfoPO);
			
			// 以上操作成功，返回不带数据的信息
			return AjaxResult.successWithoutData();
	}
	
	
	
	
	
}
