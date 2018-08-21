package com.inno72.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.model.Inno72Activity;
import com.inno72.service.Inno72GameApiService;
import com.inno72.vo.MachineApiVo;

@RestController
@RequestMapping(value = "api")
public class Inno72GameApiController {

	@Resource
	private Inno72GameApiService inno72GameApiService;

	/**
	 *
	 * @param vo
	 *  machineId 机器ID
	 *  gameId 游戏ID
	 *  report 游戏结果
	 * @return Result<Object>
	 */
	@RequestMapping(value = "/goods/findProduct", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<Object> findProduct(MachineApiVo vo) {
		return inno72GameApiService.findProduct(vo);
	}

	/**
	 * @param vo
	 *  sessionUuid
	 *  activityId 活动ID，表明活动的来源和品牌
	 *  machineId 售货机ID
	 *  itemId 商品ID
	 *  gameId 游戏ID
	 * @return Result<Object>
	 */
	@RequestMapping(value = "/qroauth/order", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<Object> order(MachineApiVo vo) {
		return inno72GameApiService.order(vo);
	}

	/**
	 * @param vo
	 *  sessionUuid
	 *  orderId
	 * @return Result<Boolean>
	 */
	@RequestMapping(value = "/qroauth/order-polling", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<Boolean> orderPolling(MachineApiVo vo) {
		return inno72GameApiService.orderPolling(vo);
	}

	/**
	 * @param vo
	 *  userId
	 *  gameId
	 * @return Result<Object>
	 */
	@RequestMapping(value = "/special/luckyDraw", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<Object> luckyDraw(MachineApiVo vo) {
		return inno72GameApiService.luckyDraw(vo);
	}

	/**
	 * @param vo
	 *  machineId
	 *  gameId
	 *  goodsId
	 * @return Result<String>
	 */
	@RequestMapping(value = "/goods/shipmentReport", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<String> shipmentReport(MachineApiVo vo) {
		return inno72GameApiService.shipmentReport(vo);
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
	public Result<String> sessionRedirect(String sessionUuid, String mid, String token, String code, String userId) {
		return inno72GameApiService.sessionRedirect(sessionUuid, mid, token, code, userId);
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
	 * @param machineId
	 * @return Result
	 */
	@RequestMapping(value = "/malfunctionLog", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<String> malfunctionLog(String machineId, String channelCode) {
		return inno72GameApiService.malfunctionLog(machineId, channelCode);
	}

	/**
	 * 掉货失败接口
	 * @param machineId
	 * @return Result
	 */
	@RequestMapping(value = "/shipmentFail", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<String> shipmentFail(String machineId, String channelCode, String describtion) {
		return inno72GameApiService.shipmentFail(machineId, channelCode, describtion);
	}

	/**
	 * 用户互动时长接口
	 * @param machineId
	 * @return Result
	 */
	@RequestMapping(value = "/userDuration", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<String> userDuration(String token, String itemId, String sellerId, String userId, String machineCode,
			String playTime) {
		return inno72GameApiService.userDuration(token, itemId, sellerId, userId, machineCode, playTime);
	}

	/**
	 * 获取派样商品
	 * @param machineId
	 * @return Result
	 */
	@RequestMapping(value = "/sampling", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<List<Inno72Activity>> sampling() {
		return inno72GameApiService.sampling();
	}
}
