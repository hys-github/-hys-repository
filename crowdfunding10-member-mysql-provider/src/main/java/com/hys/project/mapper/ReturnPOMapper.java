package com.hys.project.mapper;

import com.hys.project.entity.po.ReturnPO;
import com.hys.project.entity.po.ReturnPOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ReturnPOMapper {
    int countByExample(ReturnPOExample example);

    int deleteByExample(ReturnPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ReturnPO record);

    int insertSelective(ReturnPO record);

    List<ReturnPO> selectByExample(ReturnPOExample example);

    ReturnPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ReturnPO record, @Param("example") ReturnPOExample example);

    int updateByExample(@Param("record") ReturnPO record, @Param("example") ReturnPOExample example);

    int updateByPrimaryKeySelective(ReturnPO record);

    int updateByPrimaryKey(ReturnPO record);
    
    // 将回报信息批量插入到数据库中
	void insertReturnPOList(@Param("returnPOList")List<ReturnPO> returnPOList);
}