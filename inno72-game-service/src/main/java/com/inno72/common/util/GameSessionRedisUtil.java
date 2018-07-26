package com.inno72.common.util;


import com.alibaba.fastjson.JSON;
import com.inno72.common.utils.StringUtil;
import com.inno72.vo.UserSessionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class GameSessionRedisUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(GameSessionRedisUtil.class);

	public static final String SESSION_KEY = "session:";
	public static final Integer SESSION_EX = 1600;

	@Autowired
	private StringRedisTemplate template;

	public UserSessionVo getSessionKey(String sessionUuid){
		String sessionKey = SESSION_KEY + sessionUuid;
		String s = string().get(sessionKey);
		UserSessionVo userSessionVo = null;
		if (StringUtil.isNotEmpty(s)){
			userSessionVo = JSON.parseObject(s, UserSessionVo.class);
		}
		return userSessionVo;
	}

	public void setSessionEx(String sessionUuid, String value){
		LOGGER.info("存入session ===> sessionUuid[{}]、 value[{}];", sessionUuid, value);
		string().set(SESSION_KEY + sessionUuid, value , GameSessionRedisUtil.SESSION_EX, TimeUnit.SECONDS);
	}

	private ValueOperations<String, String> string() {
		return template.opsForValue();
	}
	
	public void setSessionEx(String sessionUuid, String value, Integer outtime){
		LOGGER.info("存入session ===> sessionUuid[{}]、 value[{}];", sessionUuid, value);
		string().set(SESSION_KEY + sessionUuid, value , outtime, TimeUnit.SECONDS);
	}

	public String getKey(String key) {
		String s = string().get(key);
		return s;
	}
	
