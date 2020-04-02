package com.hys.project.service.Impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.hys.project.service.RedisService;
import com.hys.project.util.AjaxResult;

@Service
public class RedisServiceImpl implements RedisService{
	
	@Autowired
	StringRedisTemplate redisTemplate;

	@Override
	public String getRedisValueBykey(String key) {
		
			ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
			
			String value = opsForValue.get(key);
			
			return value;
	}
	
	/**
	 * 
	 * 		存储键值对在redis中，并设置过期时间
	 */
	@Override
	public AjaxResult<String> setRedisKeyValueWithTimeout(String key, String value, Long time, TimeUnit timeUnit) {
		
		try {
			
			ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
			
			// 存储到redis中
			opsForValue.set(key, value, time, timeUnit);
			
			// 成功返回成功，没有返回数据
			AjaxResult<String> successWithoutData = AjaxResult.successWithoutData();
			
			return successWithoutData;
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			// 出现异常返回失败信息
			AjaxResult<String> failed = AjaxResult.failed(e.getMessage());
			
			return failed;
		}
		
	}
	
	/**
	 * 		删除键值
	 */
	@Override
	public void deleteRedisValueByKey(String key) {
		
		try {
			
			//ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
			
			redisTemplate.delete(key);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
