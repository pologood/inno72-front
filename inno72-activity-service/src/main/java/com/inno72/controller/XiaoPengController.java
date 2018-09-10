package com.inno72.controller;

import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.inno72.vo.XiaoPengReq;

@RestController
@RequestMapping("/xiaopeng")
public class XiaoPengController {

	private static final Logger LOGGER = LoggerFactory.getLogger(XiaoPengController.class);

	@Autowired
	private CommonService commonService;

	@Autowired
	private XiaoPengService xiaoPengService;

	@CrossOrigin(origins = "*", maxAge = 3600)
	@ResponseBody
	@RequestMapping(value = "/getVerificationCode", method = {RequestMethod.POST})
	public Result<Object> getVerificationCode(@RequestBody XiaoPengReq reqBean) {
		String code = makeVerifiedCode();
		try {
			return commonService.sendSMSVerificationCode(reqBean.getPhone(), code);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Results.failure("短信发送异常");
		}
	}

	private String makeVerifiedCode() {
		Random random = new Random(System.nanoTime());
		return String.format("%04d", random.nextInt(10000));
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@ResponseBody
	@RequestMapping(value = "/makeQrCode", method = {RequestMethod.POST})
	public Result<Object> makeQrCode(@RequestBody XiaoPengReq reqBean) {
		return xiaoPengService.makeQrCode(reqBean.getMachinedCode(), reqBean.getSessionUuid());
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@ResponseBody
	@RequestMapping(value = "/save", method = {RequestMethod.POST})
	public Result<Object> save(@RequestBody XiaoPeng xiaoPeng) {
		try {
			if(!checkSaveParam(xiaoPeng)) {
				return Results.warn("提交信息不完整", -2);
			}
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
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@ResponseBody
	@RequestMapping(value = "/feedBackPolling", method = {RequestMethod.POST})
	public Result<Object> feedBackPolling(@RequestBody XiaoPengReq reqBean) {
		return xiaoPengService.feedBackPolling(reqBean.getSessionUuid());
	}

	private boolean checkSaveParam(XiaoPeng xiaoPeng) throws ParamException {
		if (xiaoPeng == null || StringUtils.isBlank(xiaoPeng.getCode()) || StringUtils.isBlank(xiaoPeng.getName())
				|| StringUtils.isBlank(xiaoPeng.getSessionUuid()) || StringUtils.isBlank(xiaoPeng.getCode())
				|| StringUtils.isBlank(xiaoPeng.getQuestion1()) || StringUtils.isBlank(xiaoPeng.getQuestion2())
				|| StringUtils.isBlank(xiaoPeng.getQuestion3()) || StringUtils.isBlank(xiaoPeng.getQuestion4())) {
			return false;
		}
		return true;
	}

}
