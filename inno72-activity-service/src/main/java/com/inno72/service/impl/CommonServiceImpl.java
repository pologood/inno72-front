package com.inno72.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.inno72.common.utils.StringUtil;
import com.inno72.msg.MsgUtil;
import com.inno72.service.CommonService;
import com.inno72.vo.FeedbackCash;
import com.inno72.vo.Result;
import com.inno72.vo.Results;

@Service
public class CommonServiceImpl implements CommonService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72FileServiceImpl.class);

	private String SMS_CODE = "yp_validate_code";

	private String APPNAME = "inno72-activity-service";

	private Long EXPIRE_TIME = 120L;

	private static final long FEEDBACK_EX = 3000;

	@Autowired
	private MsgUtil msgUtil;

	private String VERIFICATIONCODE_KEY = "verification:" + APPNAME + ":";

	private static final String SESSION_KEY = "session:";

	private static final String FEEDBACK_KEY = "feedback:";

	@Autowired
	private StringRedisTemplate template;

	@Override
	public boolean isSessionExist(String sessionUuid) {
		return template.hasKey(SESSION_KEY + sessionUuid);
	}

	@Override
	public void setFeedBackEx(String sessionUuid, String value) {
		LOGGER.debug("存入feedBack ===> sessionUuid[{}]、 value[{}];", sessionUuid, value);
		template.opsForValue().set(FEEDBACK_KEY + sessionUuid, value, FEEDBACK_EX, TimeUnit.SECONDS);
	}

	@Override
	public FeedbackCash getFeedBack(String sessionUuid) {
		String value = template.opsForValue().get(FEEDBACK_KEY + sessionUuid);
		FeedbackCash feedBackCash = null;
		if (StringUtil.isNotEmpty(value)) {
			feedBackCash = JSON.parseObject(value, FeedbackCash.class);
		}
		return feedBackCash;
	}

	@Override
	public Result<Object> sendSMSVerificationCode(String phone, String code) {

		String redisCode = template.opsForValue().get(VERIFICATIONCODE_KEY + phone);
		if (StringUtils.isEmpty(redisCode)) {

			LOGGER.info("发送短信验证码phone={},code={},appName=");
			template.opsForValue().set(VERIFICATIONCODE_KEY + phone, code, EXPIRE_TIME, TimeUnit.SECONDS);
			Map<String, String> param = new HashMap<>();
			param.put("code", code);
			msgUtil.sendSMS(SMS_CODE, param, phone, APPNAME);
			return Results.success();

		} else {
			LOGGER.info("验证码仍然有效,有效时间为：{},phone={}", template.getExpire(VERIFICATIONCODE_KEY + phone), phone);
			return Results.failure("验证码仍然有效，请查收短信");
		}
	}

	@Override
	public boolean verificationCode(String phone, String code) {

		String redisCode = template.opsForValue().get(VERIFICATIONCODE_KEY + phone);

		LOGGER.info("校验验证码phone={},code={},redisCode={}", phone, code, redisCode);
		if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
			LOGGER.error("参数错误");
			return false;
		}
		if (code.equalsIgnoreCase(redisCode)) {
			return true;
		} else {
			return false;
		}
	}
}
