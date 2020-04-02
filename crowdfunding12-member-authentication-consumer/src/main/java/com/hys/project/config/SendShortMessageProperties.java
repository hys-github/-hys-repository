package com.hys.project.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author 86191
 *		自定义配置文件
 *\			自动从application.properties中获取相关属性值		
 *
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "short.message")
public class SendShortMessageProperties {
	
	// 短信接口的调用地址,第三方调用接口会提供，前提是你要购买
	private String host;
	
	// 具体发送短信功能地址
	private String path;
	
	//  请求方式
	private String method;
	
	// 登录到aliyun在控制台上查看已购买的短信接口的appcode
	private String appcode;
	
	// 签名编号【联系旺旺客服申请，测试请用1
	private String sign;
	
	// 模板编号【联系旺旺客服申请，测试请用1~21】
	private String skin;
	
	
	
	
}
