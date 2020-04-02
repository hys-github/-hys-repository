package com.hys.project.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author 86191
 *		aliyun上开通的oss创建一个bucket，并开启accessKey
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class OSSProperties {
	
	// 地域节点
	private String endPoint;

	// 创建的Bucket名
	private String bucketName;

	//
	private String accessKeyId;

	//
	private String accessKeySecret;

	// Bucket 域名
	private String bucketDomain;

}
