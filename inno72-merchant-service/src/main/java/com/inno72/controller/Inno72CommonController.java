package com.inno72.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.CommonBean;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.utils.StringUtil;
import com.inno72.msg.MsgUtil;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.CommonService;


@RestController
public class Inno72CommonController {

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72CommonController.class);

	@Resource
	private MsgUtil msgUtil;

	@Resource
	private IRedisUtil redisUtil;

	@RequestMapping(value = "/common/code/{type}", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<String> code(@PathVariable(value = "type") String type, String phone) {
		if (StringUtil.isEmpty(type) || StringUtil.isEmpty(phone)) {
			return Results.failure("无发送类型!");
		}
		String code = "";
		String redisHost = "" ;
		switch (type) {
			case "1":
				code = CommonBean.PHONE_CODE.ALTER_PHONE;
				redisHost = CommonBean.REDIS_MERCHANT_MOBILE_CODE_ALTER_PHONE;
				break;
			case "2":
				code = CommonBean.PHONE_CODE.BINDING_PHONE;
				redisHost = CommonBean.REDIS_MERCHANT_MOBILE_CODE_BINDING_PHONE;
				break;
			case "3":
				code = CommonBean.PHONE_CODE.REST_PASSWORD;
				redisHost = CommonBean.REDIS_MERCHANT_MOBILE_CODE_RESET_PWD;
				break;
			default:
				return Results.failure("确认发送类型!");
		}

		String s = CommonBean.genCode(4);

		String cacheCode = redisUtil.setex(redisHost + phone,
				CommonBean.REDIS_MERCHANT_MOBILE_CODE_TIMEOUT, s);

		Map<String, String> param = new HashMap<>(1);
		param.put("code", s);
		LOGGER.info("发送信息 code -> {}, param -> {}, phone -> {}, AppName -> {}", code, param, phone,
				CommonBean.APP_NAME);
		msgUtil.sendSMS(code, param, phone, CommonBean.APP_NAME);

		return Results.success();
	}

	@Resource
	private CommonService commonService;

	@RequestMapping(value = "/common/api/{type}", method = {RequestMethod.POST, RequestMethod.GET})
	public Result baseApi(@PathVariable(value = "type") String type, String sellerId){
		return Results.success(commonService.baseApi(type, sellerId));
	}



}
