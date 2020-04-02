package com.hys.project.service;

import java.util.concurrent.TimeUnit;

import com.hys.project.util.AjaxResult;

public interface RedisService {

	String getRedisValueBykey(String key);

	AjaxResult<String> setRedisKeyValueWithTimeout(String key, String value, Long time, TimeUnit timeUnit);

	void deleteRedisValueByKey(String key);

}
