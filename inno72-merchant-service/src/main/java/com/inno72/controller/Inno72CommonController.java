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
import com.inno72.service.Inno72MerchantTotalCountByUserService;


@RestController
@RequestMapping(method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS})
public class Inno72CommonController {

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72CommonController.class);

	@Resource
	private MsgUtil msgUtil;

	@Resource
	private IRedisUtil redisUtil;

	@RequestMapping(value = "/common/code/{type}")
	public Result<String> code(@PathVariable(value = "type") String type, String phone) {
		if (StringUtil.isEmpty(type) || StringUtil.isEmpty(phone)) {
			return Results.failure("无发送类型!");
		}
		String code = "";
		String redisHost = "";
		String msg = "";
		switch (type) {
			case "1":
				code = CommonBean.PHONE_CODE.MOBILE_CODE;
				redisHost = CommonBean.REDIS_MERCHANT_MOBILE_CODE_ALTER_PHONE;
				msg = "修改手机号码";
				break;
			case "2":
				code = CommonBean.PHONE_CODE.MOBILE_CODE;
				redisHost = CommonBean.REDIS_MERCHANT_MOBILE_CODE_BINDING_PHONE;
				msg = "绑定新手机号码";
				break;
			case "3":
				code = CommonBean.PHONE_CODE.MOBILE_CODE;
				redisHost = CommonBean.REDIS_MERCHANT_MOBILE_CODE_RESET_PWD;
				msg = "找回密码";
				break;
			default:
				return Results.failure("确认发送类型!");
		}

		String s = CommonBean.genCode(4);

		String cacheCode = redisUtil.setex(redisHost + phone, CommonBean.REDIS_MERCHANT_MOBILE_CODE_TIMEOUT, s);

		Map<String, String> param = new HashMap<>(1);
		param.put("code", s);
		param.put("msg", msg);
		LOGGER.info("发送信息 code -> {}, param -> {}, phone -> {}, AppName -> {}", code, param, phone,
				CommonBean.APP_NAME);
		msgUtil.sendSMS(code, param, phone, CommonBean.APP_NAME);

		return Results.success();
	}

	@Resource
	private CommonService commonService;

	@RequestMapping(value = "/common/api/{type}")
	public Result baseApi(@PathVariable(value = "type") String type, String merchantId) {
		return Results.success(commonService.baseApi(type, merchantId));
	}


	@Resource
	private Inno72MerchantTotalCountByUserService inno72MerchantTotalCountByUserService;

	@RequestMapping(value = "/test")
	public Result<Map<String, Object>> test(String actId, String start, String end) {
		Result<Map<String, Object>> mapResult = inno72MerchantTotalCountByUserService
				.selectByActivityId(actId, start, end);
		return mapResult;
	}




}
