package com.inno72.common.util;


import java.util.concurrent.TimeUnit;

import com.inno72.sessionshare.support.context.SessionContextInitializer;
import com.inno72.sessionshare.utils.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.inno72.common.utils.StringUtil;
import com.inno72.vo.UserSessionVo;

@Component
public class GameSessionRedisUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(GameSessionRedisUtil.class);

	private static final String SESSION_KEY = "session:";
	private static final Integer SESSION_EX = 1600;

	@Autowired
	private StringRedisTemplate template;

	public UserSessionVo getSessionKey(String sessionUuid) {
//		String sessionKey = SESSION_KEY + sessionUuid;
//		String s = string().get(sessionKey);
//		UserSessionVo userSessionVo = null;
//		if (StringUtil.isNotEmpty(s)) {
//			userSessionVo = JSON.parseObject(s, UserSessionVo.class);
//		}
		UserSessionVo userSessionVo = new UserSessionVo(sessionUuid);
		return userSessionVo;
	}

	/**
	 * 删除session
	 * @param sessionUuid
	 */
	public void delSession(String sessionUuid) {
		LOGGER.debug("delSession sessionUuid is {}", sessionUuid);
//		template.delete(SESSION_KEY + sessionUuid);
		SessionUtils.removeSession(sessionUuid);
	}

	/**
	 * 设置session 不带失效时间
	 * @param sessionUuid
	 * @param value
	 */
//	public void setSession(String sessionUuid, String value) {
//		LOGGER.debug("setSession sessionUuid is {}, value is {}", sessionUuid, value);
//		string().set(SESSION_KEY + sessionUuid, value);
//	}

	public void setSessionEx(String sessionUuid, String value) {
		LOGGER.debug("存入session ===> sessionUuid[{}]、 value[{}];", sessionUuid, value);
		string().set(SESSION_KEY + sessionUuid, value, GameSessionRedisUtil.SESSION_EX, TimeUnit.SECONDS);
	}

	private ValueOperations<String, String> string() {
		return template.opsForValue();
	}

	public void setSessionEx(String sessionUuid, String value, Integer outtime) {
		LOGGER.debug("存入session ===> sessionUuid[{}]、 value[{}];", sessionUuid, value);
		string().set(sessionUuid, value, outtime, TimeUnit.SECONDS);
	}

	public Boolean exists(String key) {
		return template.hasKey(key);
	}

	public Boolean hasKey(String key) {
		return template.hasKey(SESSION_KEY + key);
	}

}
