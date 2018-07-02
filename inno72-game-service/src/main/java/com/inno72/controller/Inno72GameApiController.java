package com.inno72.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.service.Inno72GameApiService;
import com.inno72.vo.MachineApiVo;

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
	public Result<Map<String, String>> findProduct(@RequestBody MachineApiVo vo){
		return inno72GameApiService.findProduct(vo);
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
	public Result<Object>  order(@RequestBody MachineApiVo vo){
		return inno72GameApiService.order(vo);
	}
	
	/**
	 * 
	 * @param sessionUuid
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/qroauth/order-polling", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result<String>  orderPolling(@RequestBody MachineApiVo vo){
		return inno72GameApiService.orderPolling(vo);
	}
	
	/**
	 * 
	 * @param userId
	 * @param gameId
	 * @return
	 */
	@RequestMapping(value = "/special/luckyDraw", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result<String>  luckyDraw(@RequestBody MachineApiVo vo){
		return inno72GameApiService.luckyDraw(vo);
	}
	
	/**
	 * 
	 * @param machineId
	 * @param gameId
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value = "/api/shipmentReport", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result<String>  shipmentReport(@RequestBody MachineApiVo vo){
		return inno72GameApiService.shipmentReport(vo);
	}
	
	
}
