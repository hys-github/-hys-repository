package com.hys.project.test;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedis {
	
	@Autowired
	StringRedisTemplate redisTemplate;
	
	Logger logger=LoggerFactory.getLogger(TestRedis.class);
	
	@Test
	public void testRedis() {
		
		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		
		//opsForValue.set("aaa", "哈巴河", 4, TimeUnit.MINUTES);
		
		String string = opsForValue.get("aaa");
		
		logger.info("\t"+string);
	}
	
	
}
