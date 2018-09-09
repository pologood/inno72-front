package com.inno72.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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

			return inno72GameApiService.prepareLoginQrCode(req.getMachineCode(), req.getLoginType(), req.getExt());

		} else {

			return inno72GameApiService.prepareLoginNologin(req.getMachineCode());

		}
	}

	@RequestMapping(value = "/redirectLogin", method = {RequestMethod.GET})
	public void redirectLogin(String sessionUuid, HttpServletResponse response) {
		try {
			response.sendRedirect(inno72GameApiService.redirectLogin(sessionUuid));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			return inno72GameApiService.standardOrder(vo);
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
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findActivity", method = {RequestMethod.POST})
	public Result findActivity(@RequestParam(name = "machineId") String mid, String planId, String version,
			String versionInno72) {
		return inno72MachineService.findGame(mid, planId, version, versionInno72);
	}

	/**
	 * polling 用户登录信息
	 * @param sessionUuid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sessionPolling", method = {RequestMethod.POST})
	public Result<Object> sessionPolling(String sessionUuid) {
		return inno72AuthInfoService.sessionPolling(sessionUuid);
	}

	/**
	 * polling 订单支付状态
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/orderPolling", method = {RequestMethod.POST})
	public Result<Object> orderPolling(MachineApiVo vo) {
		return inno72GameApiService.orderPolling(vo);
	}

	/**
	 * 设置用户已登录
	 */
	@ResponseBody
	@RequestMapping(value = "/setLogged", method = {RequestMethod.POST})
	public Result<Boolean> setLogged (String sessionUuid) {
		boolean b = inno72AuthInfoService.setLogged(sessionUuid);
		if (!b) {
			Results.failure("登录失败");
		}
		return Results.success();
	}

	/**
	 * 登录前的操作（目前聚石塔回调）
	 */
	@ResponseBody
	@RequestMapping(value = "/processBeforeLogged", method = {RequestMethod.POST})
	public Result<Object> processBeforeLogged (String sessionUuid, String authInfo) {
		Result<Object> result = inno72AuthInfoService.processBeforeLogged(sessionUuid, authInfo);
		return Results.success(result);
	}


}
