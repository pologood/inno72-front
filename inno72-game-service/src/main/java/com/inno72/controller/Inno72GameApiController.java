package com.inno72.controller;

import com.inno72.common.Result;
import com.inno72.service.Inno72GameApiService;
import com.inno72.vo.MachineApiVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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


}
