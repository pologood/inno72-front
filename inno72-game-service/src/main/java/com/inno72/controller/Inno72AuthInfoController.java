package com.inno72.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.service.Inno72AuthInfoService;

/**
 * Created by CodeGenerator on 2018/06/27.
 */
@RestController
@RequestMapping("/session")
public class Inno72AuthInfoController {
	@Resource
	private Inno72AuthInfoService inno72AuthInfoService;

	@RequestMapping(value = "/createQrCode", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public Result<Object> createQrCode(@RequestParam String machineId) {

		return inno72AuthInfoService.createQrCode(machineId);
	}

	@RequestMapping(value = "/polling", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public Result<Object> sessionPolling(@RequestParam String sessionUuid) {
		return inno72AuthInfoService.sessionPolling(sessionUuid);
	}

	/**
	 * 生成派样活动二维码
	 * @param machineCode
	 * @return Result
	 */
	@RequestMapping(value = "/createSamplingQrCode", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public Result<Object> createSamplingQrCode(@RequestParam String machineCode, @RequestParam String itemId,
			@RequestParam String isVip, @RequestParam String sessionKey) {

		return inno72AuthInfoService.createSamplingQrCode(machineCode, itemId, isVip, sessionKey);

	}
}
