package com.inno72.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.StringUtils;
import com.inno72.feign.MachineBackgroundFeignClient;
import com.inno72.mapper.*;
import com.inno72.model.*;
import com.inno72.plugin.http.HttpClient;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.Inno72GameApiService;
import com.inno72.vo.GoodsVo;
import com.inno72.vo.MachineApiVo;
import com.inno72.vo.UserSessionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

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
	private Inno72OrderMapper inno72OrderMapper;

	@Resource
	private IRedisUtil redisUtil;

	@Resource
	private Inno72GameUserMapper inno72GameUserMapper;

	@Resource
	private Inno72MachineMapper inno72MachineMapper;

	@Resource
	private MachineBackgroundFeignClient machineBackgroundFeignClient;


	/**
	 * {
	 goodsId: 1,
	 goodsNum: 1,
	 goodsName: '哈啤',
	 chanelId: [1,2]
	 }
	 */
	@SuppressWarnings({"rawtypes"})
	@Override
	public Result<Object> findProduct(MachineApiVo vo) {

		Map<String, String> requestParam = new HashMap<>();
		requestParam.put("machineId", vo.getMachineId());
		requestParam.put("gameId", vo.getGameId());
		requestParam.put("report", vo.getReport());

		List<String> resultGoodsId = inno72GameResultGoodsMapper.findGoodsId(requestParam);
		//TODO 请求接口 获取出货 货道号
		Result supplyChannel = machineBackgroundFeignClient.getSupplyChannel(
				new Inno72SupplyChannel(vo.getMachineId(), resultGoodsId.toArray(new String[resultGoodsId.size()]),
						""));

		if (supplyChannel.getCode() == Result.FAILURE) {
			return Results.failure(supplyChannel.getMsg());
		}
		String jsonString = JSON.toJSONString(supplyChannel.getData());
		LOGGER.info(jsonString);

		List<Inno72SupplyChannel> parseArray = JSON
				.parseArray(JSON.toJSONString(supplyChannel.getData()), Inno72SupplyChannel.class);
		LOGGER.info("查询 货道号 结果 ==> {}", JSON.toJSONString(parseArray));
		if (parseArray == null || parseArray.size() == 0) {
			return Results.failure("没有商品!");
		}

		Map<String, GoodsVo> goodsVoMap = new HashMap<>();

		for (Inno72SupplyChannel inno72SupplyChannel : parseArray) {

			String goodsCode = inno72SupplyChannel.getGoodsCode();
			String code = inno72SupplyChannel.getCode();
			Integer goodsCount = inno72SupplyChannel.getGoodsCount();
			String goodsName2 = inno72SupplyChannel.getGoodsName();

			GoodsVo goodsVo = goodsVoMap.get(goodsCode);

			if (goodsVo == null) {
				goodsVo = new GoodsVo(goodsCode, 0, goodsName2);
			}

			int goodsNum = goodsVo.getGoodsNum();

			List<String> chanelIds = goodsVo.getChannelIds();

			if (goodsCount != null && goodsCount > 0) {
				goodsVo.setGoodsNum(goodsNum += goodsCount);
				chanelIds.add(code);
				goodsVo.setChannelIds(chanelIds);
			}

			goodsVoMap.put(goodsCode, goodsVo);

		}

		Collection<GoodsVo> values = goodsVoMap.values();

		return Results.success(values);
	}

	/**
	 * String machineId, 
	 * String gameId, 
	 * String report
	 */
	@Override
	public Result<Object> order(MachineApiVo vo) {

		String jstUrl = inno72GameServiceProperties.get("jstUrl");

		if (StringUtils.isEmpty(jstUrl)) {
			return Results.failure("配置中心无聚石塔配置路径!");
		}

		String machineId = vo.getMachineId();
		String activityId = vo.getActivityId();
		String sessionUuid = vo.getSessionUuid();
		String itemId = vo.getItemId();
		String gameId = vo.getGameId();
		//新增
		String channelId = vo.getChannelId();

		String sessionUUIDObjectJSON = redisUtil.get(sessionUuid);
		if (StringUtils.isEmpty(sessionUUIDObjectJSON)) {
			return Results.failure("登录失效!");
		}

		JSONObject sessionObject = JSON.parseObject(sessionUUIDObjectJSON);
		String accessToken = Optional.ofNullable(sessionObject.get("accessToken")).map(Object::toString).orElse("");

		Map<String, String> requestForm = new HashMap<>();
		requestForm.put("accessToken", accessToken);
		requestForm.put("activityId", activityId);
		requestForm.put("mid", machineId);
		requestForm.put("goodsId", itemId);

		String respJson = HttpClient.form(jstUrl + "/api/top/order", requestForm, null);

		if (StringUtils.isEmpty(respJson)) {
			return Results.failure("聚石塔无返回数据!");
		}

		LOGGER.info("调用聚石塔接口  【下单】返回 ===> {}", JSON.toJSONString(respJson));

		try {

			JSONObject parseObjectRoot = JSON.parseObject(respJson);
			String tmall_fans_automachine_order_createorderbyitemid_response = Optional
					.ofNullable(parseObjectRoot.get("tmall_fans_automachine_order_createorderbyitemid_response"))
					.map(Object::toString).orElse("");
			JSONObject parseObject = JSON.parseObject(tmall_fans_automachine_order_createorderbyitemid_response);
			String result = Optional.ofNullable(parseObject.get("result")).map(Object::toString).orElse("");
			JSONObject parseResultObject = JSON.parseObject(result);
			String msg_code = Optional.ofNullable(parseResultObject.get("msg_code")).map(Object::toString).orElse("");
			//			String result = Optional.ofNullable(parseObject.get("result")).map(Object::toString).orElse("");
			if (!msg_code.equals("SUCCESS")) {
				String msg_info = Optional.ofNullable(parseObject.get("msg_info")).map(Object::toString).orElse("");
				return Results.failure(msg_info);
			}

			String model = Optional.ofNullable(parseResultObject.get("model")).map(Object::toString).orElse("");
			JSONObject parseModelObject = JSON.parseObject(model);
			LocalDateTime now = LocalDateTime.now();

			String orderId = Optional.ofNullable(parseModelObject.getString("order_id")).map(Object::toString)
					.orElse("");

			Inno72Order inno72Order = inno72OrderMapper.selectByRefOrderId(orderId);
			if (inno72Order == null) {
				inno72Order = new Inno72Order();
				String userId = Optional.ofNullable(sessionObject.get("userId")).map(Object::toString).orElse("");

				Map<String, Object> infoMap = new HashMap<>();
				if (StringUtils.isNotEmpty(userId)) {
					Inno72GameUser selectByChannelUserKey = inno72GameUserMapper.selectByChannelUserKey(userId);
					if (selectByChannelUserKey != null) {
						infoMap.put("inno72GameUser", selectByChannelUserKey);
						inno72Order.setUserId(selectByChannelUserKey.getId());//session中userId查询inno72_game_user主键
					}
				}

				Inno72Game inno72Game = inno72GameMapper.selectByPrimaryKey(gameId);
				infoMap.put("inno72Game", inno72Game);

				Inno72Machine inno72Machine = inno72MachineMapper.selectByPrimaryKey(machineId);
				infoMap.put("inno72Machine", inno72Machine);

				inno72Order.setActivityId(activityId);
				inno72Order.setChannelId(channelId);
				inno72Order.setGameId(gameId);
				inno72Order.setMachineId(machineId);
				inno72Order.setOrderTime(now);
				//				inno72Order.setOrderNum(orderNum);
				inno72Order.setPayStatus("0");
				inno72Order.setOrderType("1000");
				inno72Order.setRefOrderId(orderId);

				inno72OrderMapper.insert(inno72Order);
				Inno72OrderDetail inno72OrderDetail = new Inno72OrderDetail();
				inno72OrderDetail.setDetail(JSON.toJSONString(infoMap));
				inno72OrderDetail.setId(inno72Order.getId());
				inno72OrderDetail.setOrderNum(inno72Order.getOrderNum());
				inno72OrderDetailMapper.insert(inno72OrderDetail);

			}

			return Results.success(mapToUpperCase(parseModelObject));

		} catch (Exception e) {

			LOGGER.info("解析聚石塔返回数据异常! ===>  {}", e.getMessage(), e);
			return Results.failure("解析聚石塔返回数据异常!");

		}

	}

	private Map<String, Object> mapToUpperCase(JSONObject jObject) {
		Map<String, Object> result = new HashMap<>();
		if (jObject != null) {
			for (Map.Entry<String, Object> element : jObject.entrySet()) {
				result.put(StringUtils.strToUpperCase(element.getKey()), element.getValue());
			}
		}
		return result;
	}



	@Resource
	private Inno72OrderDetailMapper inno72OrderDetailMapper;

	/**
	 *
	 * @param vo
	 * sessionUuid
	 * orderId
	 * @return string
	 */
	@Override
	public Result<Boolean> orderPolling(MachineApiVo vo) {
		String jstUrl = inno72GameServiceProperties.get("jstUrl");
		if (StringUtils.isEmpty(jstUrl)) {
			return Results.failure("配置中心无聚石塔配置路径!");
		}

		String sessionUuid = vo.getSessionUuid();
		String orderId = vo.getOrderId();

		String sessionUUIDObjectJSON = redisUtil.get(sessionUuid);
		if (StringUtils.isEmpty(sessionUUIDObjectJSON)) {
			return Results.failure("登录失效!");
		}

		JSONObject sessionObject = JSON.parseObject(sessionUUIDObjectJSON);
		String accessToken = Optional.ofNullable(sessionObject.get("accessToken")).map(Object::toString).orElse("");

		Map<String, String> requestForm = new HashMap<>();

		requestForm.put("accessToken", accessToken);
		requestForm.put("orderId", orderId);

		String respJson = HttpClient.form(jstUrl + "/api/top/order-polling", requestForm, null);
		if (StringUtils.isEmpty(respJson)) {
			return Results.failure("聚石塔无返回数据!");
		}

		LOGGER.info("调用聚石塔接口  【下单支付状态】返回 ===> {}", JSON.toJSONString(respJson));

		try {
			JSONObject parseObjectRoot = JSON.parseObject(respJson);
			String tmall_fans_automachine_order_createorderbyitemid_response = Optional
					.ofNullable(parseObjectRoot.get("tmall_fans_automachine_order_checkpaystatus_response"))
					.map(Object::toString).orElse("");
			JSONObject parseObject = JSON.parseObject(tmall_fans_automachine_order_createorderbyitemid_response);
			String msg_code = Optional.ofNullable(parseObject.get("msg_code")).map(Object::toString).orElse("");
			if (!msg_code.equals("SUCCESS")) {
				String msg_info = Optional.ofNullable(parseObject.get("msg_info")).map(Object::toString).orElse("");
				return Results.failure(msg_info);
			}
			boolean model = (boolean) parseObject.get("model");
			if (model) {
				Inno72Order inno72Order = inno72OrderMapper.selectByRefOrderId(orderId);
				if (inno72Order != null) {
					inno72Order.setPayStatus("1");
					inno72Order.setPayTime(LocalDateTime.now());
					inno72OrderMapper.updateByPrimaryKeySelective(inno72Order);
				}
			}
			return Results.success(model);


		} catch (Exception e) {
			LOGGER.info("解析聚石塔返回数据异常! ===>  {}", e.getMessage(), e);
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
		if (StringUtils.isEmpty(jstUrl)) {
			return Results.failure("配置中心无聚石塔配置路径!");
		}

		String sessionUuid = vo.getSessionUuid();
		String shopId = vo.getShopId();
		String ua = vo.getUa();
		String umid = vo.getUmid();
		String interactId = vo.getInteractId();

		String sessionUUIDObjectJSON = redisUtil.get(sessionUuid);
		if (StringUtils.isEmpty(sessionUUIDObjectJSON)) {
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

		String respJson = HttpClient.form(jstUrl + "/api/top/lottery", requestForm, null);
		if (StringUtils.isEmpty(respJson)) {
			return Results.failure("聚石塔无返回数据!");
		}

		LOGGER.info("调用聚石塔接口  【抽奖】返回 ===> {}", JSON.toJSONString(respJson));

		try {

			JSONObject parseObjectRoot = JSON.parseObject(respJson);
			String tmall_interact_isvlottery_draw_response = Optional
					.ofNullable(parseObjectRoot.get("tmall_interact_isvlottery_draw_response")).map(Object::toString)
					.orElse("");
			JSONObject parseObject = JSON.parseObject(tmall_interact_isvlottery_draw_response);

			String msg_code = Optional.ofNullable(parseObject.get("code")).map(Object::toString).orElse("");
			if (!msg_code.equals("CE001")) {
				String msg_info = Optional.ofNullable(parseObject.get("msg_info")).map(Object::toString).orElse("");
				return Results.failure(msg_info);
			}

			String data = Optional.ofNullable(parseObject.get("data")).map(Object::toString).orElse("");
			JSONObject parseDataObject = JSON.parseObject(data);

			return Results.success(mapToUpperCase(parseDataObject));

		} catch (Exception e) {
			return Results.failure(e.getMessage());
		}
	}

	/**
	 * @param vo
	 * 	machineId 机器id
	 *  gameId    游戏id
	 *  goodsId   商品ID
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result<String> shipmentReport(MachineApiVo vo) {

		String machineId = vo.getMachineId();
		String channelId = vo.getChannelId();
		String sessionUuid = vo.getSessionUuid();
		String orderId = vo.getOrderId();

		if (StringUtils.isEmpty(machineId) || StringUtils.isEmpty(channelId) || StringUtils.isEmpty(sessionUuid)
				|| StringUtils.isEmpty(orderId)) {
			return Results.failure("参数缺失！");
		}

		String jstUrl = inno72GameServiceProperties.get("jstUrl");
		if (StringUtils.isEmpty(jstUrl)) {
			return Results.failure("配置中心无聚石塔配置路径!");
		}

		String sessionUUIDObjectJSON = redisUtil.get(sessionUuid);
		if (StringUtils.isEmpty(sessionUUIDObjectJSON)) {
			return Results.failure("登录失效!");
		}

		JSONObject sessionObject = JSON.parseObject(sessionUUIDObjectJSON);
		String accessToken = Optional.ofNullable(sessionObject.get("accessToken")).map(Object::toString).orElse("");

		Map<String, String> requestForm = new HashMap<>();

		requestForm.put("accessToken", accessToken);
		requestForm.put("orderId", orderId); //安全ua
		requestForm.put("mid", machineId);//umid
		requestForm.put("channelId", channelId);//互动实例ID

		String respJson = HttpClient.form(jstUrl + "/api/top/deliveryRecord", requestForm, null);
		if (StringUtils.isEmpty(respJson)) {
			return Results.failure("聚石塔无返回数据!");
		}

		LOGGER.info("调用聚石塔接口  【通知出货】返回 ===> {}", JSON.toJSONString(respJson));

		JSONObject parseObjectRoot = JSON.parseObject(respJson);
		String tmall_fans_automachine_deliveryrecord_response = Optional
				.ofNullable(parseObjectRoot.get("tmall_fans_automachine_deliveryrecord_response")).map(Object::toString)
				.orElse("");
		JSONObject parseObject = JSON.parseObject(tmall_fans_automachine_deliveryrecord_response);

		String msg_code = Optional.ofNullable(parseObject.get("msg_code")).map(Object::toString).orElse("");
		if (!msg_code.equals("SUCCESS")) {
			String msg_info = Optional.ofNullable(parseObject.get("msg_info")).map(Object::toString).orElse("");
			return Results.failure(msg_info);
		}

		Result subCount = machineBackgroundFeignClient.subCount(new Inno72SupplyChannel(machineId, null, channelId));

		if (subCount.getCode() == Result.FAILURE) {
			return Results.failure(subCount.getMsg());
		}

		LOGGER.info("减货结果 ==> {}", JSON.toJSONString(subCount));

		return Results.success();
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
		LOGGER.info("session 回执请求 => sessionUuid:{}; mid:{}; token:{}; code:{}; userId:{}", sessionUuid, mid, token,
				code, userId);

		JSONObject parseTokenObject = JSON.parseObject(token);
		String access_token = Optional.ofNullable(parseTokenObject.get("access_token")).map(Object::toString)
				.orElse("");

		if (StringUtils.isEmpty(access_token)) {
			return Results.failure("access_token 参数缺失！");
		}

		List<Inno72MachineGame> inno72MachineGames = inno72MachineGameMapper.selectByMachineId(mid);
		String gameId = "";

		if (inno72MachineGames.size() > 0) {
			gameId = inno72MachineGames.get(0).getGameId();
		}

		if (StringUtils.isEmpty(gameId)) {
			return Results.failure("没有绑定的游戏！");
		}

		Inno72Game inno72Game = inno72GameMapper.selectByPrimaryKey(gameId);
		if (inno72Game == null) {
			return Results.failure("不存在的游戏！");
		}
		Long sellerId = inno72Game.getSellerId();

		String jstUrl = inno72GameServiceProperties.get("jstUrl");
		if (StringUtils.isEmpty(jstUrl)) {
			return Results.failure("配置中心无聚石塔配置路径!");
		}

		Map<String, String> requestForm = new HashMap<>();
		requestForm.put("accessToken", access_token);
		requestForm.put("mid", mid);
		requestForm.put("sellerId", sellerId + "");
		/**
		 * <tmall_fans_automachine_getmaskusernick_response>
		 *     <msg_code>200</msg_code>
		 *     <msg_info>用户不存在</msg_info>
		 *     <model>e****丫</model>
		 * </tmall_fans_automachine_getmaskusernick_response>
		 */
		String respJson = HttpClient.form(jstUrl + "/api/top/getMaskUserNick", requestForm, null);
		LOGGER.info("调用聚石塔接口  【请求nickName】返回 ===> {}", JSON.toJSONString(respJson));

		JSONObject jsonNikeNameObject = JSON.parseObject(respJson);
		String tmall_fans_automachine_getmaskusernick_response = Optional
				.ofNullable(jsonNikeNameObject.get("tmall_fans_automachine_getmaskusernick_response"))
				.map(Object::toString).orElse("");

		if (StringUtils.isEmpty(tmall_fans_automachine_getmaskusernick_response)) {
			return Results.failure("请求用户名失败!");
		}
		JSONObject responseJson = JSON.parseObject(tmall_fans_automachine_getmaskusernick_response);
		//		String msg_code = Optional.ofNullable(responseJson.get(
		//				"msg_code")).map(Object::toString).orElse("");

		//		if (!msg_code.equals("200")){
		//			LOGGER.info("请求NickName失败");
		//			return Results.failure("请求NickName失败");
		//		}
		String nickName = Optional.ofNullable(responseJson.get("model")).map(Object::toString).orElse("");

		UserSessionVo sessionVo = new UserSessionVo(mid, nickName, userId, access_token, gameId, sessionUuid);

		LocalDateTime now = LocalDateTime.now();
		redisUtil.setex(sessionUuid, 1800, JSON.toJSONString(sessionVo));

		Inno72GameUser inno72GameUser = inno72GameUserMapper.selectByChannelUserKey(userId);
		if (inno72GameUser == null) {
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

}
