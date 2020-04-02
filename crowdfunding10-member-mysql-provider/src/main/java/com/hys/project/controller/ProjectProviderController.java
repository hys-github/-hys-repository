package com.hys.project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hys.project.entity.VO.ProjectVO;
import com.hys.project.service.ProjectProviderService;
import com.hys.project.util.AjaxResult;

@RestController
public class ProjectProviderController {
	
	static final Logger LOGGER=LoggerFactory.getLogger(ProjectProviderController.class);
	
	@Autowired
	ProjectProviderService projectProviderService;
	
	/**
	 * @param projectVO		封装了众筹项目中的一切信息
	 * @return
	 */
	@RequestMapping("/to/mysql/save/project/info")
	public AjaxResult<String> saveProjectVOToMysql(@RequestBody ProjectVO projectVO){
		
			try {
				AjaxResult<String> mysqAjaxResult=projectProviderService.saveProjectVOToMysql(projectVO);
				
				return mysqAjaxResult;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				return AjaxResult.failed(e.getMessage());
			}
	}
	
	
	
	
	
}
