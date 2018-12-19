package com.inno72.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.inno72.common.datetime.LocalDateTimeUtil;
import com.inno72.common.util.Encrypt;
import com.inno72.log.PointLogContext;
import com.inno72.log.vo.LogType;

@Component
public class CommonBean {

	@PostConstruct
	public void initClient() {
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonBean.class);
	/**
	 * 商户登录session
	 */
	public static final String REDIS_MERCHANT_LOGIN_SESSION_KEY = "inno72_merchant_service:session:";
	public static final String REDIS_MERCHANT_RESET_PWD_KEY = "inno72_merchant_service:reset_password:";
	public static final String REDIS_MERCHANT_RESET_MOBILE_KEY = "inno72_merchant_service:reset_mobile:";
	public static final int REDIS_MERCHANT_LOGIN_SESSION_KEY_TIMES = 60 * 60 * 10;


	public static final int REDIS_MERCHANT_MOBILE_CODE_TIMEOUT = 60 * 3;
	public static final String REDIS_MERCHANT_MOBILE_CODE_RESET_PWD = "inno72_merchant_service:mobile_code:reset_pwd:";
	public static final String REDIS_MERCHANT_MOBILE_CODE_BINDING_PHONE = "inno72_merchant_service:mobile_code:binding_phone:";
	public static final String REDIS_MERCHANT_MOBILE_CODE_ALTER_PHONE = "inno72_merchant_service:mobile_code:alter_phone:";

	/**
	 * 短信验证码模板CODE
	 */
	public static class PHONE_CODE {
		/**
		 * 修改手机号
		 */
		public static final String MOBILE_CODE = "INNO72_MERCHANT_MOBILE_CODE";
	}

	/**
	 * @param msg 消息体
	 *            msg[0] type 日志类型
	 *            msg[1] machineCode 机器code
	 *            msg[2] detail 详情
	 */
	public static void logger(String... msg) {
		new PointLogContext(LogType.POINT).machineCode(msg[1]).pointTime(
				LocalDateTimeUtil.transfer(LocalDateTime.now(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
				.type(msg[0]).detail(msg[2]).tag(msg[3]).bulid();
		LOGGER.info("记录埋点数据 [{}]", JSON.toJSONString(msg));
	}

	/*
	 * ************************************************ -> 埋点 <- *****************************************************
	 */

	public static String getActive() {
		String active = System.getenv("spring_profiles_active");
		LOGGER.info("获取spring_profiles_active：{}", active);
		if (active == null || active.equals("")) {
			LOGGER.info("未读取到spring_profiles_active的环境变量,使用默认值: dev");
			active = "dev";
		}
		return active;
	}

	public static final String APP_NAME = "inno72_merchant_service";

	public static String pwd(String pwd) {
		return Encrypt.md5AndSha(pwd);
	}

	public static String genCode(int s) {

		if (s == 0) {
			s = 6;
		}
		StringBuilder r = new StringBuilder();
		for (int i = 0; i < s; i++) {
			r.append((int) (Math.random() * 10));
		}
		return r.toString().toString();
	}

}
