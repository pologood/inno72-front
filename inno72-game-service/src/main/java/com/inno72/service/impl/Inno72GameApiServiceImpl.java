package com.inno72.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import com.inno72.mapper.Inno72GameMapper;
import com.inno72.model.Inno72Game;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.common.CommonBean;
import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.mapper.Inno72GameResultGoodsMapper;
import com.inno72.mapper.Inno72GameUserMapper;
import com.inno72.mapper.Inno72MachineGameMapper;
import com.inno72.model.Inno72GameUser;
import com.inno72.model.Inno72MachineGame;
import com.inno72.plugin.http.HttpClient;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.Inno72GameApiService;
import com.inno72.vo.MachineApiVo;
import com.inno72.vo.UserSessionVo;

@Service
public class Inno72GameApiServiceImpl implements Inno72GameApiService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72GameApiServiceImpl.class);

	@Resource
	private Inno72GameResultGoodsMapper inno72GameResultGoodsMapper;
	
	@Resource
	private Inno72GameServiceProperties inno72GameServiceProperties;
	
	@Resource
	private Inno72MachineGameMapper inno72MachineGameMapper;

	@Resource
	private Inno72GameMapper inno72GameMapper;
	
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
		
		String sessionUUIDObjectJSON = redisUtil.get(CommonBean.SESSION_KEY + sessionUuid);
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
	 * @param vo
	 * sessionUuid
	 * orderId
	 * @return string
	 */
	@Override
	public Result<String> orderPolling(MachineApiVo vo) {
		String jstUrl = inno72GameServiceProperties.get("jstUrl");
		if ( StringUtils.isEmpty(jstUrl)) {
			return Results.failure("配置中心无聚石塔配置路径!");
		}
		
		String sessionUuid = vo.getSessionUuid();
		String orderId = vo.getSessionUuid();
		
		String sessionUUIDObjectJSON = redisUtil.get(CommonBean.SESSION_KEY + sessionUuid);
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
	 * @param vo
	 * userId 用户id
	 * gameId 游戏id
	 * @return Object
	 */
	@Override
	public Result<Object> luckyDraw(MachineApiVo vo) {
		String jstUrl = inno72GameServiceProperties.get("jstUrl");
		if ( StringUtils.isEmpty(jstUrl)) {
			return Results.failure("配置中心无聚石塔配置路径!");
		}
		
		String sessionUuid = vo.getSessionUuid();
		String shopId = vo.getShopId();
		String ua = vo.getUa();
		String umid = vo.getUmid();
		String interactId = vo.getInteractId();
		
		String sessionUUIDObjectJSON = redisUtil.get(CommonBean.SESSION_KEY + sessionUuid);
		if ( StringUtils.isEmpty(sessionUUIDObjectJSON) ) {
			return Results.failure("登录失效!");
		}
		
		JSONObject sessionObject = JSON.parseObject(sessionUUIDObjectJSON);
		String accessToken = Optional.ofNullable(sessionObject.get("accessToken")).map(Object::toString).orElse("");
		
		Map<String, String> requestForm = new HashMap<>();
		
		requestForm.put("accessToken", accessToken);
		requestForm.put("ua", ua); //安全ua
		requestForm.put("umid", umid);//umid
		requestForm.put("interactId", interactId);//互动实例ID
		requestForm.put("shopId", shopId);//店铺ID
		
		String respJson = HttpClient.form(jstUrl+"/api/qroauth/lottery", requestForm, null);
		if (StringUtils.isEmpty(respJson)) {
			return Results.failure("聚石塔无返回数据!");
		}
		try {
			JSONObject parseObjectRoot = JSON.parseObject(respJson);
			String tmall_interact_isvlottery_draw_response = 
					Optional.ofNullable(parseObjectRoot.get("tmall_interact_isvlottery_draw_response")).map(Object::toString).orElse("");
			JSONObject parseObject = JSON.parseObject(tmall_interact_isvlottery_draw_response);
			String msg_code = Optional.ofNullable(parseObject.get("code")).map(Object::toString).orElse("");
			if (!msg_code.equals("CE001")) {
				String msg_info = Optional.ofNullable(parseObject.get("msg_info")).map(Object::toString).orElse("");
				return Results.failure(msg_info);
			}
			String data = Optional.ofNullable(parseObject.get("data")).map(Object::toString).orElse("");
			JSONObject parseDataObject = JSON.parseObject(data);
			
			return Results.success(parseDataObject);
		} catch (Exception e) {
			return Results.failure(e.getMessage());
		}
	}

	/**
	 * 
	 *
	 * @param vo
	 * 	machineId 机器id
	 *  gameId    游戏id
	 *  goodsId   商品ID
	 * @return String
	 */
	@Override
	public Result<String> shipmentReport(MachineApiVo vo) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {
		    "sessionUuid":"sessionUuid",
		    "mid":"mid",
		    "authInfo":{
		        "code":"code",
		        "userNick":"null",
		        "userId":"result.taobao_user_id",
		        "token":{
		            "w1_expires_in":"1494",
		            "refresh_token_valid_time":"1530521339000",
		            "taobao_user_nick":"t01sNMnCfkSGKksyq9su9nKdqToIqawc1J4IkhX131bUgk%3D",
		            "re_expires_in":0,
		            "expire_time":1531385339000,
		            "token_type":"Bearer",
		            "access_token":"6202b145dbdead2fbf6c3dfhde257f5215fec00d68b5d36525671323",
		            "taobao_open_uid":"AAGxHGf-AF4DUnGwDrNbhtyW",
		            "w1_valid":1530523139844,
		            "refresh_token":"62022142256fba186bbb0bdf30463037218905f210646d6525671323",
		            "w2_expires_in":0,
		            "w2_valid":1530521339844,
		            "r1_expires_in":1494,
		            "r2_expires_in":0,
		            "r2_valid":1530521339844,
		            "r1_valid":1530523139844,
		            "taobao_user_id":"-1",
		            "expires_in":863693
		        }
		    }
		}
	 */
	@Override
	public Result<String> sessionRedirect(String sessionUuid, String mid, String token, String code, String userId) {
		LOGGER.info("session 回执请求 => sessionUuid:{}; mid:{}; token:{}; code:{}; userId:{}", sessionUuid, mid, token ,code, userId);
//		LOGGER.info("redirect session : paramJSON ===> {}", json);
//		if ( StringUtils.isEmpty(json) ) {
//			return Results.failure("参数不存在！");
//		}
//
//		JSONObject parseRootObject = JSON.parseObject(json);
//		String sessionUuid = Optional.ofNullable(parseRootObject.get("sessionUuid")).map(Object::toString).orElse("");
//		String mid = Optional.ofNullable(parseRootObject.get("mid")).map(Object::toString).orElse("");
//		String authInfo = Optional.ofNullable(parseRootObject.get("authInfo")).map(Object::toString).orElse("");
//
//		if (StringUtils.isEmpty(sessionUuid) || StringUtils.isEmpty(mid) || StringUtils.isEmpty(authInfo)) {
//			return Results.failure("参数缺失！");
//		}
//
//		JSONObject parseAuthInfoObject = JSON.parseObject(authInfo);
//
//		String userNick = Optional.ofNullable(parseAuthInfoObject.get("userNick")).map(Object::toString).orElse("");
//		String userId = Optional.ofNullable(parseAuthInfoObject.get("userId")).map(Object::toString).orElse("");
//
//		String token = Optional.ofNullable(parseAuthInfoObject.get("token")).map(Object::toString).orElse("");
//		if ( StringUtils.isEmpty(userId) || StringUtils.isEmpty(token) ) {
//			return Results.failure("Token参数缺失！");
//		}
		LOGGER.info("session 回执请求 => ");
		JSONObject parseTokenObject = JSON.parseObject(token);
		String access_token = Optional.ofNullable(parseTokenObject.get("access_token")).map(Object::toString).orElse("");

		if ( StringUtils.isEmpty(access_token)) {
			return Results.failure("access_token 参数缺失！");
		}
		List<Inno72MachineGame> inno72MachineGames = inno72MachineGameMapper.selectByMachineId(mid);
		String gameId = "";
		if (inno72MachineGames.size() > 0) {
			gameId = inno72MachineGames.get(0).getGameId();
		}

		if ( StringUtils.isEmpty(gameId) ){
			return Results.failure("没有绑定的游戏！");
		}

		Inno72Game inno72Game = inno72GameMapper.selectByPrimaryKey(gameId);
		if ( inno72Game == null ){
			return Results.failure("不存在的游戏！");
		}
		Long sellerId = inno72Game.getSellerId();

		String jstUrl = inno72GameServiceProperties.get("jstUrl");
		if ( StringUtils.isEmpty(jstUrl)) {
			return Results.failure("配置中心无聚石塔配置路径!");
		}
		Map<String, String> requestForm = new HashMap<>();
		requestForm.put("accessToken", access_token);
		requestForm.put("mid", mid);
		requestForm.put("sellerId", sellerId+"");
		/**
		 * <tmall_fans_automachine_getmaskusernick_response>
		 *     <msg_code>200</msg_code>
		 *     <msg_info>用户不存在</msg_info>
		 *     <model>e****丫</model>
		 * </tmall_fans_automachine_getmaskusernick_response>
		 */
		String respJson = HttpClient.form(jstUrl + "/api/top/getMaskUserNick", requestForm, null);
		LOGGER.info("请求NikeName ==> {}", respJson);
		JSONObject jsonNikeNameObject = JSON.parseObject(respJson);
		String tmall_fans_automachine_getmaskusernick_response = Optional.ofNullable(jsonNikeNameObject.get(
				"tmall_fans_automachine_getmaskusernick_response")).map(Object::toString).orElse("");
		if (StringUtils.isEmpty(tmall_fans_automachine_getmaskusernick_response)){
			return Results.failure("请求用户名失败!");
		}
		JSONObject responseJson = JSON.parseObject(tmall_fans_automachine_getmaskusernick_response);
		String msg_code = Optional.ofNullable(responseJson.get(
				"msg_code")).map(Object::toString).orElse("");

//		if (!msg_code.equals("200")){
//			LOGGER.info("请求NickName失败");
//			return Results.failure("请求NickName失败");
//		}
		String nickName = Optional.ofNullable(responseJson.get(
				"model")).map(Object::toString).orElse("");

		UserSessionVo sessionVo = new UserSessionVo(mid, nickName, userId, access_token, gameId,  sessionUuid);
		
		LocalDateTime now = LocalDateTime.now();
		redisUtil.set(CommonBean.SESSION_KEY + sessionUuid, JSON.toJSONString(sessionVo));
		redisUtil.expire(CommonBean.SESSION_KEY + sessionUuid, 1800);//超时
		
		Inno72GameUser inno72GameUser = inno72GameUserMapper.selectByChannelUserKey(userId);
		if (inno72GameUser == null ) {
			inno72GameUser = new Inno72GameUser();
			inno72GameUser.setUserNick(nickName);
			inno72GameUser.setPhone("");
			inno72GameUser.setChannel("1000");
			inno72GameUser.setChannelUserKey(userId);
			inno72GameUser.setCreateTime(now);
			inno72GameUser.setUpdateTime(now);
			inno72GameUserMapper.insert(inno72GameUser);
		}
		
		return Results.success();
	}
	
	@Resource
	private Inno72GameUserMapper inno72GameUserMapper;

}
