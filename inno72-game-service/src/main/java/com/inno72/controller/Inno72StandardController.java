package com.inno72.controller;

import java.util.Map;

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
import com.inno72.service.Inno72MachineService;
import com.inno72.vo.MachineApiVo;
import com.inno72.vo.StandardOrderReqVo;
import com.inno72.vo.StandardPrepareLoginReqVo;
import com.inno72.vo.StandardRedirectLoginReqVo;
import com.inno72.vo.StandardShipmentReqVo;
import com.inno72.vo.UserSessionVo;

/**
 * 标准接口
 */
@RestController
@RequestMapping(value = "/api/standard")
public class Inno72StandardController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());


	@Resource
	private Inno72AuthInfoService inno72AuthInfoService;

	@Resource
	private Inno72GameApiService inno72GameApiService;

	@Resource
	private Inno72MachineService inno72MachineService;

	@Resource
	private GameSessionRedisUtil gameSessionRedisUtil;

	/**
	 * 登录（包括需要登录和非登录的场景）
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/prepareLogin", method = {RequestMethod.POST})
	public Result<Object> prepareLogin(StandardPrepareLoginReqVo req) {
		if (StringUtils.isBlank(req.getMachineCode())) {
			logger.warn("机器Code为空 " + req.toString());
			return Results.failure("机器Code为空");
		}

		if (!StandardLoginTypeEnum.isExist(req.getLoginType())) {
			logger.warn("登陆类型错误 " + req.toString());
			return Results.failure("登陆类型错误");
		}

		if (StandardLoginTypeEnum.ALIBABA.getValue().equals(req.getLoginType())) {

			return inno72GameApiService.prepareLoginQrCode(req.getMachineCode(), req.getLoginType());

		} else {

			return inno72GameApiService.prepareLoginNologin(req.getMachineCode());

		}
	}

	@RequestMapping(value = "/redirectLogin", method = {RequestMethod.GET})
	public String redirectLogin(StandardRedirectLoginReqVo req) {
		return "redirect:" + inno72GameApiService.redirectLogin(req);
	}


/**
 * 下单（包括订单及优惠券）
 * @param req
 * @return
 */
	@ResponseBody
	@RequestMapping(value = "/order", method = {RequestMethod.POST})
	public Result<Object> order(StandardOrderReqVo req) {

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

	/**
	 * 出货（包括正常异常流程）
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/shipment", method = {RequestMethod.POST})
	public Result<String> shipment(StandardShipmentReqVo req) {

		MachineApiVo vo = new MachineApiVo();
		vo.setMachineId(req.getMachineCode());
		vo.setChannelId(req.getChannelId());
		vo.setFailChannelIds(req.getFailChannelIds());
		vo.setSessionUuid(req.getSessionUuid());
		return inno72GameApiService.shipmentReportV2(vo);

	}

	/**
	 * 获得活动信息
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findGame", method = {RequestMethod.POST})
	public Result findGame(@RequestBody Map<String, String> map) {
		String mid = map.get("machineId");
		String planId = map.get("planId");
		String version = map.get("version");
		String versionInno72 = map.get("versionInno72");
		return inno72MachineService.findGame(mid, planId, version, versionInno72);
	}

	/**
	 * polling 用户登录信息
	 * @param sessionUuid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sessionPolling", method = {RequestMethod.POST})
	public Result<Object> sessionPolling(@RequestBody String sessionUuid) {
		return inno72AuthInfoService.sessionPolling(sessionUuid);
	}

	/**
	 * polling 订单支付状态
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/orderPolling", method = {RequestMethod.POST})
	public Result<Boolean> orderPolling(@RequestBody MachineApiVo vo) {
		return inno72GameApiService.orderPolling(vo);
	}


}