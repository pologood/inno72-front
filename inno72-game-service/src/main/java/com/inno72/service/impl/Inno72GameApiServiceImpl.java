package com.inno72.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.mapper.Inno72GameResultGoodsMapper;
import com.inno72.plugin.http.HttpClient;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.Inno72GameApiService;
import com.inno72.vo.MachineApiVo;

@Service
public class Inno72GameApiServiceImpl implements Inno72GameApiService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72GameApiServiceImpl.class);

	@Resource
	private Inno72GameResultGoodsMapper inno72GameResultGoodsMapper;
	
	@Resource
	private Inno72GameServiceProperties inno72GameServiceProperties;
	
	@Resource
	private IRedisUtil redisUtil;

	@Override
	public Result<Map<String, String>> findProduct(MachineApiVo vo) {
		
		Map<String, String> requestParam = new HashMap<>();
		requestParam.put("machineId", vo.getMachineId());
		requestParam.put("gameId", vo.getGameId());
		requestParam.put("report", vo.getReport());
		
		String resultGoodsId = inno72GameResultGoodsMapper.findGoodsId(requestParam);
		//TODO 请求接口 获取出货 货道号
		requestParam.put("chnnelId", resultGoodsId);
		LOGGER.info("查询 货道号 结果 ==> {}", JSON.toJSONString(requestParam));
		return Results.success(requestParam);
	}

	/**
	 * String machineId, 
	 * String gameId, 
	 * String report
	 */
	@Override
	public Result<Object> order(MachineApiVo vo) {
		String jstUrl = inno72GameServiceProperties.get("jstUrl");
		if ( StringUtils.isEmpty(jstUrl)) {
			return Results.failure("配置中心无聚石塔配置路径!");
		}
		String machineId = vo.getMachineId();
		String activityId = vo.getActivityId();
		String sessionUuid = vo.getSessionUuid();
		String itemId = vo.getItemId();
		
		String sessionUUIDObjectJSON = redisUtil.get(sessionUuid);
		if ( StringUtils.isEmpty(sessionUUIDObjectJSON) ) {
			return Results.failure("登录失效!");
		}
		
		JSONObject sessionObject = JSON.parseObject(sessionUUIDObjectJSON);
		String accessToken = Optional.ofNullable(sessionObject.get("")).map(Object::toString).orElse("");
		Map<String, String> requestForm = new HashMap<>();
		requestForm.put("accessToken", accessToken);
		requestForm.put("activityId", activityId);
		requestForm.put("machineId", machineId);
		requestForm.put("itemId", itemId);
		String respJson = HttpClient.form(jstUrl+"/api/qroauth/order", requestForm, null);
		if (StringUtils.isEmpty(respJson)) {
			return Results.failure("聚石塔无返回数据!");
		}
		try {
			JSONObject parseObjectRoot = JSON.parseObject(respJson);
			String tmall_fans_automachine_order_createorderbyitemid_response = 
					Optional.ofNullable(parseObjectRoot.get("tmall_fans_automachine_order_createorderbyitemid_response")).map(Object::toString).orElse("");
			JSONObject parseObject = JSON.parseObject(tmall_fans_automachine_order_createorderbyitemid_response);
			String msg_code = Optional.ofNullable(parseObject.get("msg_code")).map(Object::toString).orElse("");
			if (!msg_code.equals("SUCCESS")) {
				String msg_info = Optional.ofNullable(parseObject.get("msg_info")).map(Object::toString).orElse("");
				return Results.failure(msg_info);
			}
			String model = Optional.ofNullable(parseObject.get("model")).map(Object::toString).orElse("");
			JSONObject parseModelObject = JSON.parseObject(model);
			return Results.success(parseModelObject);
		} catch (Exception e) {
			LOGGER.info("解析聚石塔返回数据异常! ===>  {}",e.getMessage(), e);
			return Results.failure("解析聚石塔返回数据异常!");
		}
	}

	/**
	 * 
	 * @param sessionUuid
	 * @param orderId
	 * @return
	 */
	@Override
	public Result<String> orderPolling(MachineApiVo vo) {
		String jstUrl = inno72GameServiceProperties.get("jstUrl");
		if ( StringUtils.isEmpty(jstUrl)) {
			return Results.failure("配置中心无聚石塔配置路径!");
		}
		
		String sessionUuid = vo.getSessionUuid();
		String orderId = vo.getSessionUuid();
		
		String sessionUUIDObjectJSON = redisUtil.get(sessionUuid);
		if ( StringUtils.isEmpty(sessionUUIDObjectJSON) ) {
			return Results.failure("登录失效!");
		}
		
		JSONObject sessionObject = JSON.parseObject(sessionUUIDObjectJSON);
		String accessToken = Optional.ofNullable(sessionObject.get("")).map(Object::toString).orElse("");
		
		Map<String, String> requestForm = new HashMap<>();
		
		requestForm.put("accessToken", accessToken);
		requestForm.put("orderId", orderId);
		
		String respJson = HttpClient.form(jstUrl+"/api/qroauth/order-polling", requestForm, null);
		if (StringUtils.isEmpty(respJson)) {
			return Results.failure("聚石塔无返回数据!");
		}
		
		try {
			JSONObject parseObjectRoot = JSON.parseObject(respJson);
			String tmall_fans_automachine_order_createorderbyitemid_response = 
					Optional.ofNullable(parseObjectRoot.get("tmall_fans_automachine_order_checkpaystatus_response")).map(Object::toString).orElse("");
			JSONObject parseObject = JSON.parseObject(tmall_fans_automachine_order_createorderbyitemid_response);
			String msg_code = Optional.ofNullable(parseObject.get("msg_code")).map(Object::toString).orElse("");
			if (!msg_code.equals("SUCCESS")) {
				String msg_info = Optional.ofNullable(parseObject.get("msg_info")).map(Object::toString).orElse("");
				return Results.failure(msg_info);
			}
			return Results.success();
		} catch (Exception e) {
			LOGGER.info("解析聚石塔返回数据异常! ===>  {}",e.getMessage(), e);
			return Results.failure("解析聚石塔返回数据异常!");
		}
	}

	/**
	 * 
	 * @param userId
	 * @param gameId
	 * @return
	 */
	@Override
	public Result<String> luckyDraw(MachineApiVo vo) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param machineId
	 * @param gameId
	 * @param goodsId
	 * @return
	 */
	@Override
	public Result<String> shipmentReport(MachineApiVo vo) {
		// TODO Auto-generated method stub
		return null;
	}

}
