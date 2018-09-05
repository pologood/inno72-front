package com.inno72.controller;

import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.BizException;
import com.inno72.common.ParamException;
import com.inno72.service.CommonService;
import com.inno72.service.XiaoPengService;
import com.inno72.vo.Result;
import com.inno72.vo.Results;
import com.inno72.vo.XiaoPeng;

@RestController
@RequestMapping("/xiaopeng")
public class XiaoPengController {

	private static final Logger LOGGER = LoggerFactory.getLogger(XiaoPengController.class);

	@Autowired
	private CommonService commonService;

	@Autowired
	private XiaoPengService xiaoPengService;

	@ResponseBody
	@RequestMapping(value = "/getVerificationCode", method = {RequestMethod.GET, RequestMethod.POST})
	public Result<Object> getVerificationCode(String phone) {
		String code = makeVerifiedCode();
		try {
			return commonService.sendSMSVerificationCode(phone, code);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Results.failure("短信发送异常");
		}
	}

	private String makeVerifiedCode() {
		Random random = new Random(System.nanoTime());
		return String.format("%04d", random.nextInt(10000));
	}

	@ResponseBody
	@RequestMapping(value = "/save", method = {RequestMethod.GET, RequestMethod.POST})
	public Result<Object> save(XiaoPeng xiaoPeng) {
		try {
			checkSaveParam(xiaoPeng);
			// 校验验证码
			if (commonService.verificationCode(xiaoPeng.getPhone(), xiaoPeng.getCode())) {
				try {
					xiaoPengService.save(xiaoPeng);

				} catch (BizException e) {
					return Results.warn(e.getMessage(), -2);
				}
				return Results.success();
			} else {
				return Results.warn("验证码错误", -1);
			}
		} catch (ParamException e) {
			LOGGER.error("参数异常", e);
			return Results.failure("参数异常");
		} catch (Exception e) {
			LOGGER.error("系统异常", e);
			return Results.failure("系统异常");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/feedBackPolling", method = {RequestMethod.GET, RequestMethod.POST})
	public Result<Object> feedBackPolling(String sessionUuid) {
		return xiaoPengService.feedBackPolling(sessionUuid);
	}

	private void checkSaveParam(XiaoPeng xiaoPeng) throws ParamException {
		if (xiaoPeng == null || StringUtils.isBlank(xiaoPeng.getCode()) || StringUtils.isBlank(xiaoPeng.getName())
				|| StringUtils.isBlank(xiaoPeng.getSessionUuid()) || StringUtils.isBlank(xiaoPeng.getCode())
				|| StringUtils.isBlank(xiaoPeng.getQuestion1()) || StringUtils.isBlank(xiaoPeng.getQuestion2())
				|| StringUtils.isBlank(xiaoPeng.getQuestion3()) || StringUtils.isBlank(xiaoPeng.getQuestion4())) {
			throw new ParamException("参数异常");
		}
	}

}
