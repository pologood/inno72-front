package com.inno72.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.inno72.service.Inno72PaiYangService;
import com.inno72.vo.*;
import com.alibaba.fastjson.JSON;
import com.inno72.common.*;
import com.inno72.common.util.UuidUtil;
import com.inno72.redis.IRedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.StandardLoginTypeEnum;
import com.inno72.common.TopH5ErrorTypeEnum;
import com.inno72.common.datetime.LocalDateTimeUtil;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.log.PointLogContext;
import com.inno72.log.vo.LogType;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.Inno72AuthInfoService;
import com.inno72.service.Inno72GameApiService;
import com.inno72.service.Inno72MachineService;
import com.inno72.vo.Inno72MachineVo;
import com.inno72.vo.MachineApiVo;
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

	@Resource
	private Inno72PaiYangService inno72PaiYangService;

	@Resource
	private Inno72GameServiceProperties inno72GameServiceProperties;

	@Resource
	private IRedisUtil redisUtil;

	@Value("${top_h5_err_url}")
	private String topH5ErrUrl;

	@Value("${env}")
	private String env;

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

			return inno72GameApiService.prepareLoginQrCode(req);

		} else {

			return inno72GameApiService.prepareLoginNologin(req.getMachineCode());

		}
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72StandardController.class);

	/**
	 * 测试埋点接口
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/logger", method = {RequestMethod.POST})
	public void logger(StandardPrepareLoginReqVo req) {
		new PointLogContext(LogType.POINT).machineCode("ceshimachinecode123")
				.pointTime(LocalDateTimeUtil.transfer(LocalDateTime.now(),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
				.type("31").detail("测试").tag("测试tag").bulid();
		LOGGER.info("记录埋点数据 [测试]");
	}

	/**
	 * 下单（包括订单及优惠券）
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/order", method = {RequestMethod.POST})
	public Result<Object> order(MachineApiVo vo) {

		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(vo.getSessionUuid());
		if (userSessionVo == null) {
			return Results.failure("会话不存在!" + vo.toString());
		}

		return inno72GameApiService.standardOrder(vo);
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
	public Result<Object> setLogged(String sessionUuid, String traceId) {
		LOGGER.info("setLogged sessionUuid is {}, traceId is {}", sessionUuid, traceId);
		boolean result = inno72AuthInfoService.setLogged(sessionUuid);
		LOGGER.info("setLogged result is {}", result);
		if (result) {
			return Results.success();
		} else {
			return Results.failure("登录失败");
		}
	}

	/**
	 * 登录前的操作（目前聚石塔回调）
	 */
	@ResponseBody
	@RequestMapping(value = "/processBeforeLogged", method = {RequestMethod.POST})
	public Result<Object> processBeforeLogged(String sessionUuid, String authInfo, String traceId) {
		Result<Object> result = inno72AuthInfoService.processBeforeLogged(sessionUuid, authInfo, traceId);
		return result;
	}

	/**
	 * 登录跳转
	 */
	@ResponseBody
	@RequestMapping(value = "/loginRedirect", method = {RequestMethod.GET})
	public void loginRedirect(HttpServletResponse response, String sessionUuid, String env) {
		LOGGER.info("loginRedirect sessionUuid is {}, env is {}", sessionUuid, env);
		try {
			synchronized (this) {
				String redirectUrl = "";
				// 判断是否已经有人扫过了，如果扫过 直接跳转
				UserSessionVo sessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);

				if (sessionVo != null) {
					LOGGER.info("loginRedirect isScanned is {}", sessionVo.getIsScanned());

					// 判断二维码是否已经超时, 恢复isScanned 状态 为false，允许二维码继续被扫
					boolean qrCode = gameSessionRedisUtil.exists(sessionUuid + "qrCode");
					LOGGER.info("loginRedirect qrCode is {}", qrCode);
					if (!qrCode) {
						sessionVo.setIsScanned(false);
					}

					if (sessionVo.getIsScanned()) {
						LOGGER.info("loginRedirect 二维码已经被扫描");
						redirectUrl = String.format(topH5ErrUrl, env) + "/?status="+ TopH5ErrorTypeEnum.IS_SCANNED.getValue();
					} else {
						sessionVo.setIsScanned(true);
						gameSessionRedisUtil.setSession(sessionUuid, JSON.toJSONString(sessionVo));
						// 设置15秒内二维码不能被扫
						gameSessionRedisUtil.setSessionEx(sessionUuid + "qrCode", sessionUuid, 15);
						String traceId = UuidUtil.getUUID32();
						redirectUrl = String.format("%s%s/%s/%s", inno72GameServiceProperties.get("tmallUrl"), sessionUuid, env, traceId);
					}
				}
				LOGGER.info("loginRedirect redirectUrl is {} ", redirectUrl);
				response.sendRedirect(redirectUrl);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 入会操作（目前聚石塔回调）
	 */
	@ResponseBody
	@RequestMapping(value = "/newRetailmemberJoin", method = {RequestMethod.POST})
	public Result<Object> newRetailmemberJoin(String sessionUuid,String sellSessionKey,String taobaoUserId,String meberJoinCallBackUrl) {
		return inno72GameApiService.newRetailmemberJoin(sessionUuid,sellSessionKey,taobaoUserId,meberJoinCallBackUrl);
	}

}
