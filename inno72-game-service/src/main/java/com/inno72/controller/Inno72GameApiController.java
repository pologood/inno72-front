package com.inno72.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.service.Inno72GameApiService;

@RestController
@RequestMapping(value = "api")
public class Inno72GameApiController {
	
	@Autowired
	private Inno72GameApiService inno72GameApiService;

	/**
	 * 
	 * @param machineId 机器ID
	 * @param gameId 游戏ID
	 * @param report 游戏结果
	 * @return
	 */
	@RequestMapping(value = "/goods/findProduct", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result<String>  findProduct(String machineId, String gameId, String report){
		return inno72GameApiService.findProduct(machineId, gameId, report);
	}
	
	/**
	 * 
	 * @param sessionUuid 
	 * @param activityId 活动ID，表明活动的来源和品牌
	 * @param machineId 售货机ID
	 * @param itemId 商品ID
	 * @param gameId 游戏ID
	 * @return
	 */
	@RequestMapping(value = "/qroauth/order", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result<String>  order(String sessionUuid, String activityId, String machineId, String itemId, String gameId){
		return inno72GameApiService.order(sessionUuid, activityId, machineId, itemId, gameId);
	}
	
	@RequestMapping(value = "/qroauth/order-polling", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result<String>  orderPolling(String sessionUuid, String orderId){
		return inno72GameApiService.orderPolling(sessionUuid, orderId);
	}
	
	
	@RequestMapping(value = "/special/luckyDraw", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result<String>  luckyDraw(String userId, String gameId){
		return inno72GameApiService.luckyDraw(userId, gameId);
	}
	
	@RequestMapping(value = "/api/shipmentReport", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result<String>  shipmentReport(String machineId, String gameId, String goodsId){
		return inno72GameApiService.shipmentReport(machineId, gameId, goodsId);
	}
	
	
}
