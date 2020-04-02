package com.hys.project.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.hys.project.entity.MemberPO;
import com.hys.project.mapper.MemberPOMapper;
import com.hys.project.util.ShortMessageVerifyUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMysql {

	Logger logger = LoggerFactory.getLogger(TestMysql.class);

	@Autowired
	MemberPOMapper memberPOMapper;

	@Test
	public void insertData() {

		// BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		String pswd = "123456";

		// String encode = passwordEncoder.encode(pswd);

		// MemberPO memberPO = new
		// MemberPO(null,"123456",encode,"大水怪","123456@qq.com",0,1,"小米粒","123",2);

		// memberPOMapper.insert(memberPO);

	}

	@Test
	public void testShortMessage() {

		//ShortMessageVerifyUtil.sendShortMessageToPhone();
		// {"Message":"OK","Code":"OK","RequestId":"","BizId":""}
		String codeString="'Message':'OK','Code':'OK','RequestId':'','BizId':''";
		System.out.println(codeString);
	
	}

}
