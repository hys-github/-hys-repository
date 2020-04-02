package com.hys.project.service;

import com.hys.project.entity.VO.ProjectVO;
import com.hys.project.util.AjaxResult;

public interface ProjectProviderService {

	AjaxResult<String> saveProjectVOToMysql(ProjectVO projectVO);

}
