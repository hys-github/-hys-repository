package com.hys.project.mapper;

import com.hys.project.entity.po.ProjectPO;
import com.hys.project.entity.po.ProjectPOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProjectPOMapper {
    int countByExample(ProjectPOExample example);

    int deleteByExample(ProjectPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProjectPO record);

    int insertSelective(ProjectPO record);

    List<ProjectPO> selectByExample(ProjectPOExample example);

    ProjectPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProjectPO record, @Param("example") ProjectPOExample example);

    int updateByExample(@Param("record") ProjectPO record, @Param("example") ProjectPOExample example);

    int updateByPrimaryKeySelective(ProjectPO record);

    int updateByPrimaryKey(ProjectPO record);
    
    // 插入项目并返回自动生成的主键id
    // <insert id="insertProjectPOAndReturnPrimaryId" useGeneratedKeys="true" keyProperty="id" ……
	Integer insertProjectPOAndReturnPrimaryId(@Param("projectPO")ProjectPO projectPO);
	
	// 插入到t_project_type项目与类型关联表中，一个项目可以有多个类型
	void insertInfoTot_project_typeByProjectIdAndTypeIdList( @Param("projectId")Integer projectId, @Param("typeIdList")List<Integer> typeIdList);
	
	// 插入t_project_item_pic项目与图片路径关联表， 一个项目对应多个图片
	void insertInfoTot_project_item_picByProjectIdAndPicturePath(@Param("projectId")Integer projectId, @Param("detailPicturePathList")List<String> detailPicturePathList);
	
	// 插入t_project_tag项目与标签的关联表，一个项目多个标签
	void insertInfoTot_project_tagByProjectIdAndTagId(@Param("projectId")Integer projectId, @Param("tagIdList")List<Integer> tagIdList);
}