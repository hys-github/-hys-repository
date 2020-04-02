package com.hys.project.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hys.project.service.RedisService;
import com.hys.project.util.AjaxResult;

@RestController
public class RedisController {

	@Autowired
	RedisService redisService;

	@RequestMapping("/get/redis/value/by/key")
	public AjaxResult<String> getRedisValueBykey(@RequestParam("key") String key) {

		try {
			// 查询redis，通过的键查询值
			String value = redisService.getRedisValueBykey(key);

			// 成功，将值封装
			AjaxResult<String> successWithData = AjaxResult.successWithData(value);

			// 返回
			return successWithData;

		} catch (Exception e) {

			e.printStackTrace();
			
			// 抛出异常，将异常信息封装到AjaxResult
			AjaxResult<String> failed = AjaxResult.failed(e.getMessage());

			return failed;
		}
	}
	
	/**
	 * @param key  要设置到redis中的键
	 * @param value	设置到redis中的值
	 * @param time	设置的键值对的时间，Long类型
	 * @param timeUnit	设置的时间单位
	 * @return
	 */
	@RequestMapping("/set/redis/key/value")
	public AjaxResult<String> setRedisKeyValueWithTimeout(@RequestParam("key") String key,
			@RequestParam("value") String value, @RequestParam("time") Long time,
			@RequestParam("timeUnit") TimeUnit timeUnit) {

		AjaxResult<String> redisAjaxResult = redisService.setRedisKeyValueWithTimeout(key, value, time, timeUnit);

		return redisAjaxResult;
	}
	
	/**
	 * @param key	删除redis中的值通过键
	 * 		
	 */
	@RequestMapping("/delete/redis/value/by/key")
	public void deleteRedisValueByKey(@RequestParam("key")String key) {
		
		
		redisService.deleteRedisValueByKey(key);
		
		
	}
	
	
	
	
	
	
	
}
