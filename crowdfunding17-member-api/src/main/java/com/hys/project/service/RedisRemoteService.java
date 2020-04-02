package com.hys.project.service;

import java.util.concurrent.TimeUnit;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hys.project.util.AjaxResult;

@FeignClient("PROJECT-REDIS-PROVIDER")
public interface RedisRemoteService {
	
	@RequestMapping("/get/redis/value/by/key")
	public AjaxResult<String> getRedisValueBykey(@RequestParam("key")String key);
	
	@RequestMapping("/set/redis/key/value")
	public AjaxResult<String> setRedisKeyValueWithTimeout(
			@RequestParam("key")String key, 
			@RequestParam("value")String value, 
			@RequestParam("time")Long time, 
			@RequestParam("timeUnit")TimeUnit timeUnit);
	
	@RequestMapping("/delete/redis/value/by/key")
	public void deleteRedisValueByKey(@RequestParam("key")String key);
	
}
