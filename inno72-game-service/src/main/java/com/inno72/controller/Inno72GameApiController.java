package com.inno72.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.inno72.common.RedisConstants;
import com.inno72.common.Results;
import com.inno72.common.json.JsonUtil;
import com.inno72.common.util.DateUtil;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.service.Inno72PaiYangService;
import com.inno72.vo.UserSessionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.service.Inno72GameApiService;
import com.inno72.vo.Inno72SamplingGoods;
import com.inno72.vo.MachineApiVo;

import persist.RedisSessionDao;
import session.ShareHttpSession;
import utils.SessionUtils;

@RestController
@RequestMapping(value = "api")
public class Inno72GameApiController {

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72GameApiController.class);

	@Resource
	private Inno72GameApiService inno72GameApiService;

	@Resource
	private GameSessionRedisUtil gameSessionRedisUtil;

	@Resource
	private Inno72PaiYangService paiYangService;

	@Resource
	private RedisSessionDao redisSessionDao;

	@RequestMapping(value = "/sessionTest", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<Object> test(String session){
		ShareHttpSession shareHttpSession = redisSessionDao.get(session);
		if (shareHttpSession == null){

		}
		HttpSession session1 = SessionUtils.getSession(session, true);
		session1.setAttribute("sss","s1");
		return Results.success(session1);
	}

	/**
	 * @param sessionUuid sessionUuid
	 * @param mid mid
	 * @param token token
	 * @param code code
	 * @param userId userId
	 * @return Result
	 */
	@RequestMapping(value = "/sessionRedirect", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<String> sessionRedirect(String sessionUuid, String mid, String token, String code, String userId,
			String itemId) {
		return inno72GameApiService.sessionRedirect(sessionUuid, mid, token, code, userId, itemId);
	}

	/**
	 * 设置用户已为已登录
	 * @param sessionUuid
	 * @return
	 */
	@RequestMapping(value = "/setUserLogged", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<String> setUserLogged(String sessionUuid) {
		try {
			UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
			userSessionVo.setLogged(true);
			gameSessionRedisUtil.setSession(sessionUuid, JsonUtil.toJson(userSessionVo));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return Results.success();
	}

	/**
	 *
	 * @param sessionUuid 用户登录信息
	 * @param mid  ID
	 * @param code
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/log", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<String> log(String sessionUuid, String mid, String code, String userId) {
		return null;
	}

	/**
	 * 获取派样商品
	 * @return Result
	 */
	@RequestMapping(value = "/getSampling", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<List<Inno72SamplingGoods>> getSampling(String machineCode) {
		return inno72GameApiService.getSampling(machineCode);
	}


	/**
	 * 获取派样商品
	 * @return Result
	 */
	@RequestMapping(value = "/getSamplingNew", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<List<Inno72SamplingGoods>> getSamplingNew(String machineCode) {
		return paiYangService.getSampling(machineCode);
	}

	/**
	 * 心跳接口
	 * @return Result
	 */
	@RequestMapping(value = "/setHeartbeat", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<String> setHeartbeat(String machineCode, String page, String planCode, String activity, String desc) {
		return inno72GameApiService.setHeartbeat(machineCode, page, planCode, activity, desc);
	}
}
