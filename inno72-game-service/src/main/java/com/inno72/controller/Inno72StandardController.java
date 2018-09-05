package com.inno72.controller;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.StandardLoginTypeEnum;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.service.Inno72AuthInfoService;
import com.inno72.service.Inno72GameApiService;
import com.inno72.vo.MachineApiVo;
import com.inno72.vo.StandardLoginReqVo;
import com.inno72.vo.StandardOrderReqVo;
import com.inno72.vo.StandardShipmentReqVo;
import com.inno72.vo.UserSessionVo;

@RestController
@RequestMapping(value = "/api/standard")
public class Inno72StandardController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());


	@Resource
	private Inno72AuthInfoService inno72AuthInfoService;

	@Resource
	private Inno72GameApiService inno72GameApiService;

	@Resource
	private GameSessionRedisUtil gameSessionRedisUtil;

	@ResponseBody
	@RequestMapping(value = "/login", method = {RequestMethod.POST})
	public Result<Object> Login(@RequestBody StandardLoginReqVo req) {
		if (StringUtils.isBlank(req.getMachineCode())) {
			logger.warn("机器Code为空 " + req.toString());
			return Results.failure("机器Code为空");
		}

		if (!StandardLoginTypeEnum.isExist(req.getLoginType())) {
			logger.warn("登陆类型错误 " + req.toString());
			return Results.failure("登陆类型错误");
		}

		if (StandardLoginTypeEnum.ALIBABA.getValue().equals(req.getLoginType())) {

			return inno72AuthInfoService.createQrCode(req.getMachineCode());

		} else {

			return inno72GameApiService.sessionNologin(req.getMachineCode(), req.getIsNeedQrCode());

		}
	}

	@ResponseBody
	@RequestMapping(value = "/order", method = {RequestMethod.POST})
	public Result<Object> order(@RequestBody StandardOrderReqVo req) {

		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(req.getSessionUuid());
		if (userSessionVo == null) {
			return Results.failure("会话不存在!" + req.toString());
		}

		if (StandardLoginTypeEnum.ALIBABA.getValue().equals(userSessionVo.getLoginType())) {
			MachineApiVo vo = new MachineApiVo();
			vo.setSessionUuid(req.getSessionUuid());
			vo.setReport(req.getReport());
			return inno72GameApiService.oneKeyOrder(vo);
		} else {
			MachineApiVo vo = new MachineApiVo();
			vo.setSessionUuid(req.getSessionUuid());
			vo.setReport(req.getReport());
			return inno72GameApiService.oneKeyOrderNologin(vo);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/shipment", method = {RequestMethod.POST})
	public Result<String> shipment(@RequestBody StandardShipmentReqVo req) {

		MachineApiVo vo = new MachineApiVo();
		vo.setMachineId(req.getMachineCode());
		vo.setChannelId(req.getChannelId());
		vo.setFailChannelIds(req.getFailChannelIds());
		vo.setSessionUuid(req.getSessionUuid());
		return inno72GameApiService.shipmentReportV2(vo);

	}
}
