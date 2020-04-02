package com.hys.project.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hys.project.config.OSSProperties;
import com.hys.project.entity.VO.MemberConfirmInfoVO;
import com.hys.project.entity.VO.MemberLoginVO;
import com.hys.project.entity.VO.ProjectVO;
import com.hys.project.entity.VO.ReturnVO;
import com.hys.project.service.MysqlRemoteService;
import com.hys.project.util.AjaxResult;
import com.hys.project.util.AliyunOSSUtil;
import com.hys.project.util.ManagerConstant;

/**
 * @author 86191
 *		
 *		跟页面发起众筹的信息等相关连
 *
 */
@Controller
public class ProjectConsumerController {

	@Autowired
	MysqlRemoteService mysqlRemoteService;
	
	@Autowired
	OSSProperties ossProperties;
	
	
	private static Logger logger = LoggerFactory.getLogger(ProjectConsumerController.class);

	/**
	 * @param projectVO         // 封装了一系列上传的表单里面的属性
	 * @param headerPicture     // 上传的头信息文件或图片
	 * @param detailPictureList // 上传的多个详情的文件或图片
	 * @param session           当信息传过来后，将信息存储在session域中（redis中）
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/to/save/form/message/to/session")
	public String saveFormMessageToSession(ProjectVO projectVO,
			@RequestParam("headerPicture") MultipartFile headerPicture,
			@RequestParam("detailPictureList") List<MultipartFile> detailPictureList, HttpSession session,
			ModelMap modelMap) throws IOException {

		logger.info("projectVO====" + projectVO);
		logger.info("headerPicture======" + headerPicture.getName());	// headerPicture
		logger.info("headerPicture======" + headerPicture.getContentType());	// image/jpeg
		logger.info("detailPictureList======" + detailPictureList.get(0).getResource().toString());	// MultipartFile resource [detailPictureList]

		// 判断接受对象是否为空
		if (projectVO == null) {

			return "launch-project-page";

		}

		// 判断是否上传了文件或者图片
		boolean headerPictureEmpty = headerPicture.isEmpty();

		// 如果判断为true，表示没有上传文件或者图片
		if (headerPictureEmpty) {

			// 将信息存储到request域中，返回到页面显示错误信息
			modelMap.addAttribute(ManagerConstant.MESSAGE_TO_HTML_HEADER_PICTURE, ManagerConstant.PICTURE_IS_EMPTY);

			return "launch-project-page";
		}

		// 判断上传的多个详情的文件或图片是否为空
		if (detailPictureList == null || detailPictureList.size() == 0) {

			// 将信息存储到request域中，返回到页面显示错误信息
			modelMap.addAttribute(ManagerConstant.MESSAGE_TO_HTML_PICTURE_LIST, ManagerConstant.PICTURE_IS_EMPTY);

			return "launch-project-page";
		}

		/*
		 * 如果以上判断都没出错，表示表单上传成功，进行以下代码
		 */

		// 上传项目头文件或者图片的原始文件名
		String originalFilename = headerPicture.getOriginalFilename();

		// 得到上传文件特定的inputStream
		InputStream inputStream = headerPicture.getInputStream();

		// 从自定义的配置文件中得到自己上传到阿里云上的OSS服务器上的相关参数
		
		logger.info(ossProperties.getBucketDomain());
		logger.info(ossProperties.getAccessKeyId());
		
		// 使用工具类将问文件上传到OSS上,如果上传成功，将返回上传到oss服务器上能够访问oss上传的文件的url（网址）
		AjaxResult<String> uploadAjaxResult = AliyunOSSUtil.uploadFileToAliyunOSS(ossProperties.getEndPoint(),
				ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret(), ossProperties.getBucketName(),
				ossProperties.getBucketDomain(), originalFilename, inputStream);

		// 如果上传失败，返回失败信息
		if (AjaxResult.FAILED.equals(uploadAjaxResult.getOperationResult())) {
			
			// 得到上传失败的信息
			String message=uploadAjaxResult.getOperationMessage();
			
			// 将信息存储到request域中，返回到页面显示错误信息
			modelMap.addAttribute(ManagerConstant.MESSAGE_TO_HTML_HEADER_PICTURE,message);

			return "launch-project-page";
		}
		
		// 如果上传成功，将将返回上传到oss服务器上能够访问oss上传的文件的url（网址）
		String OSSHeaderFileUrl = uploadAjaxResult.getQueryData();
		logger.info("OSSfileUrl======"+OSSHeaderFileUrl);	// com-hys.oss-cn-shenzhen.aliyuncs.com/2020-04-01/d427f828b.jpg
		
		// 将上传成功的项目头文件set到面向工程视图对象中
		projectVO.setHeaderPicturePath(OSSHeaderFileUrl);
		
		// 得到上传多个图片的list集合
		List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
		
		if (detailPicturePathList==null||detailPicturePathList.size()==0) {
			
			// 给其一个实列化集合对象
			detailPicturePathList=new ArrayList<String>();
		}
		
		for (MultipartFile detailPicture : detailPictureList) {
			
			// 上传文件到阿里云上的OSS服务器上
			AjaxResult<String> uploadDetailPicture = AliyunOSSUtil.uploadFileToAliyunOSS(ossProperties.getEndPoint(),
					ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret(), ossProperties.getBucketName(),
					ossProperties.getBucketDomain(), detailPicture.getOriginalFilename(), detailPicture.getInputStream());
			
			// 如果上传失败，返回到上传页面重新上传或者其它操作
			if (AjaxResult.FAILED.equals(uploadDetailPicture.getOperationResult())) {
				
				// 将信息存储到request域中，返回到页面显示错误信息
				modelMap.addAttribute(ManagerConstant.MESSAGE_TO_HTML_PICTURE_LIST, uploadDetailPicture.getOperationMessage());

				return "launch-project-page";
			}
			
			// 将成功上传后得到的文件或者图片的url存入到集合中
			detailPicturePathList.add(uploadDetailPicture.getQueryData());
		}
		
		// 将成功上传的项目详情的图片或者文件的url传入到projectVO对象中
		projectVO.setDetailPicturePathList(detailPicturePathList);
		
		// 将projectVO存入到session域中，实现数据共享，以便后面工作的实现
		session.setAttribute(ManagerConstant.MESSAGE_FROM_FORM_PROJECTVO, projectVO);
		
		return "redirect:http://www.hys.com/project/to/return/project/page";
	}
	
	/**
	 * @param returnPicture		通过ajax上传过来的文件或者图片
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/get/upload/file/url/from/OSS.json")
	public AjaxResult<String> getUploadFileUrlFromOSS(@RequestParam("returnPicture")MultipartFile returnPicture) throws IOException{
		
		// 判断是否上传了文件或者图片
		if (returnPicture.isEmpty()) {
			
			// 没有上传图片，返回错误信息
			AjaxResult<String> successWithData = AjaxResult.successWithData(ManagerConstant.PICTURE_IS_EMPTY);
			
			return successWithData;
		}
		
		logger.info("returnPicture======"+returnPicture.toString());
		
		// 如果图片上传不为空,得到上传原始文件名
		String originalFilename = returnPicture.getOriginalFilename();
		
		// 得到MultipartFile文件上传类的特定io流
		InputStream inputStream = returnPicture.getInputStream();
		
		// 从自定义的配置文件中得到自己上传到阿里云上的OSS服务器上的相关参数

		// 使用工具类将问文件上传到OSS上,如果上传成功，将返回上传到oss服务器上能够访问oss上传的文件的url（网址）
		AjaxResult<String> uploadAjaxResult = AliyunOSSUtil.uploadFileToAliyunOSS(ossProperties.getEndPoint(),
						ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret(), ossProperties.getBucketName(),
						ossProperties.getBucketDomain(), originalFilename, inputStream);
		
		// 无论上传失败还是成功，都返回
		// 成功上传，返回该上传文件的url
		// 失败上传，返回失败信息
		return uploadAjaxResult;
	}
	
	/**
	 * @param returnVO	封装了回报设置中的表单信息
	 * @param session	
	 * @return
	 * 			将回报表单的信息封装到returnVO对象中，并将其添加到ProjectVO中
	 * 				ProjectVO：对象从session域中拿到，在讲returnVO封装到内部，在添加到session域中
	 */
	@ResponseBody
	@RequestMapping("/save/return/info/to/session.json")
	public AjaxResult<String> saveReturnVOToSession(ReturnVO returnVO,HttpSession session){
		
		try {
			if (returnVO==null) {
				
				return AjaxResult.failed(ManagerConstant.MESSAGE_UNKNOW_EXCEPTION);
			}
			logger.info("returnVO======"+returnVO);
			
			// 从session域中得到ProjectVO这个对象
			ProjectVO projectVO = (ProjectVO) session.getAttribute(ManagerConstant.MESSAGE_FROM_FORM_PROJECTVO);
			
			// 得到ReturnVO的list集合
			List<ReturnVO> returnVOList = projectVO.getReturnVOList();
			
			// 判断ReturnVO的list集合是否为空或者长度是否为0
			if (returnVOList==null||returnVOList.size()==0) {
				
				// 如果判定成功，则给ReturnVO的list集合实列对象
				returnVOList=new ArrayList<ReturnVO>();
				
				// 必须要set回去，不然new ArrayList<ReturnVO>()这个实列只能在这个if中有效
				projectVO.setReturnVOList(returnVOList);
			}
			
			// 将封装了回报设置中的表单信息的对象returnVO添加到集合中
			returnVOList.add(returnVO);
			
			// 再将projectVO对象存储到session中
			session.setAttribute(ManagerConstant.MESSAGE_FROM_FORM_PROJECTVO, projectVO);
			
			// 操作成功返回不带数据的
			return AjaxResult.successWithoutData();

		} catch (Exception e) {
			e.printStackTrace();
			
			// 出现异常，返回异常信息
			return AjaxResult.failed(e.getMessage());
		}
		
	}
	
	/**
	 * @param memberConfirmInfoVO	成员确认信息页面提交的信息（请填写易付宝企业账号用于收款及身份核实）
	 * @param session		用于取出前面几个页面中的ProjectVO和登录用户的部分信息MemberLoginVO
	 * @param modelMap		相当于request域
	 * @return		失败，跳转到跳转过来的页面
	 * 				成功，重定向到成功页面
	 */
	@RequestMapping("/to/save/all/info/to/mysql")
	public String saveAllInfoToMysql(MemberConfirmInfoVO memberConfirmInfoVO,HttpSession session,ModelMap modelMap) {
		
		try {
			
			logger.info("memberConfirmInfoVO======"+memberConfirmInfoVO);
			
			// 从session域中得到ProjectVO对象
			ProjectVO projectVO = (ProjectVO) session.getAttribute(ManagerConstant.MESSAGE_FROM_FORM_PROJECTVO);
			
			if (projectVO==null) {
				
				throw new RuntimeException(ManagerConstant.MESSAGE_UNKNOW_EXCEPTION);
			}
			
			// 将memberConfirmInfoVO对象存储在ProjectVO对象中
			projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);
			
			// 从session中得到登录用户的部分信息，其中里面的id关联该用户在mysql中存储的所有信息表中的主键
			MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(ManagerConstant.MESSAGE_TO_HTML_WITH_MEMBERLOGINVO);
			
			// 得到登录用户的id，将ProjectVO存储到mysql中需要这个登录用户id
			Integer memberId = memberLoginVO.getId();
			
			// 将id存储到projectVO对象中，在mysql中通过用户主键id进行一系列的保存
			projectVO.setMemberId(memberId);
			
			// 调用mysql远程服务器，众筹的所有信息存储到mysql中
			AjaxResult<String> mysqlAjaxResult=mysqlRemoteService.saveProjectVOToMysql(projectVO);
			
			// 判断在调用mysql中的方法时是否出现了错误，是执行if中的代码
			if (AjaxResult.FAILED.equals(mysqlAjaxResult.getOperationResult())) {
				
				// 得到异常信息
				String errorMessage = mysqlAjaxResult.getOperationMessage();
				
				// 将异常信息存储到request域中
				modelMap.addAttribute(ManagerConstant.MESSAGE_TO_HTML, errorMessage);
				
				// 返回到跳转之前的页面，并将异常信息打印到页面中
				return "confirm-project-page";
			}
			
			// 如果在mysql中插入数据成功，执行如下代码,,将session域中存储的ProjectVO对象删除
			session.removeAttribute(ManagerConstant.MESSAGE_FROM_FORM_PROJECTVO);
			
			logger.info("projectVO==="+projectVO);
			
			// 8.如果远程保存成功则重定向到最终完成页面
			return "redirect:http://www.hys.com/project/to/success/page";
			
		} catch (Exception e) {
			e.printStackTrace();
			
			// 将异常信息存储到request域中
			modelMap.addAttribute(ManagerConstant.MESSAGE_TO_HTML, e.getMessage());
			
			// 返回到跳转之前的页面，并将异常信息打印到页面中
			return "confirm-project-page";
		 }
		
	}
	
	
}
