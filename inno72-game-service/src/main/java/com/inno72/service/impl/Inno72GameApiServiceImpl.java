package com.inno72.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.common.util.Inno72OrderNumGenUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.*;
import com.inno72.model.*;
import com.inno72.plugin.http.HttpClient;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.Inno72GameApiService;
import com.inno72.vo.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Inno72GameApiServiceImpl implements Inno72GameApiService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72GameApiServiceImpl.class);

	@Resource
	private Inno72GameServiceProperties inno72GameServiceProperties;
	@Resource
	private Inno72GameMapper inno72GameMapper;
	@Resource
	private Inno72OrderMapper inno72OrderMapper;
	@Resource
	private GameSessionRedisUtil gameSessionRedisUtil;
	@Resource
	private Inno72GameUserMapper inno72GameUserMapper;
	@Resource
	private Inno72MachineMapper inno72MachineMapper;
	@Resource
	private Inno72ActivityPlanGameResultMapper inno72ActivityPlanGameResultMapper;
	@Resource
	private Inno72GameUserChannelMapper inno72GameUserChannelMapper;
	@Resource
	private Inno72ActivityPlanMapper inno72ActivityPlanMapper;
	@Resource
	private Inno72MerchantMapper inno72MerchantMapper;
	@Resource
	private Inno72ActivityMapper inno72ActivityMapper;
	@Resource
	private Inno72ChannelMapper inno72ChannelMapper;
	@Resource
	private Inno72ShopsMapper inno72ShopsMapper;
	@Resource
	private Inno72GoodsMapper inno72GoodsMapper;
	@Resource
	private Inno72OrderGoodsMapper inno72OrderGoodsMapper;
	@Resource
	private Inno72OrderHistoryMapper inno72OrderHistoryMapper;
	@Resource
	private Inno72LocaleMapper inno72LocaleMapper;
	@Resource
	private Inno72GameUserLifeMapper inno72GameUserLifeMapper;
	@Resource
	private Inno72SupplyChannelMapper inno72SupplyChannelMapper;
	@Autowired
	private IRedisUtil redisUtil;

	private static final String QRSTATUS_NORMAL = "0"; // 二维码正常
	private static final String QRSTATUS_INVALID = "-1"; // 二维码失效
	private static final String QRSTATUS_EXIST_USER = "-2"; // 存在用户登录

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

		String activityPlanId = vo.getActivityPlanId();
		String report = vo.getReport();
		String sessionUuid = vo.getSessionUuid();
		String machineId = vo.getMachineId();

		String channelId = vo.getChannelId();

		if ( StringUtil.isEmpty(sessionUuid) || StringUtil.isEmpty(activityPlanId) ) {
			LOGGER.info("查询商品参数错误 ! ===> {}", JSON.toJSONString(vo));
			return Results.failure("参数错误!");
		}
		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
		if (userSessionVo == null){
			return Results.failure("登录过期啦 !");
		}
		userSessionVo.setGameReport(report);
		gameSessionRedisUtil.setSessionEx(sessionUuid, JSON.toJSONString(userSessionVo));

		Map<String, String> params = new HashMap<>();
		params.put("activityPlanId", activityPlanId);
		params.put("report", report);

		List<String> resultGoodsId = inno72ActivityPlanGameResultMapper.selectByActivityPlanId(params);

		if ( resultGoodsId.size() == 0 ){
			return Results.failure("没有奖品哦.");
		}

		Inno72Machine inno72Machine = inno72MachineMapper.findMachineByCode(machineId);
		if ( inno72Machine == null ){
			return Results.failure("机器信息错误!");
		}

		//请求接口 获取出货 货道号
		Map<String, Object> map = new HashMap<>();
		map.put("machineId", inno72Machine.getId());
		map.put("goodsCodes", resultGoodsId);
		List<Inno72SupplyChannel> inno72SupplyChannels = inno72SupplyChannelMapper.selectListByParam(map);

		LOGGER.info("查询 货道号 结果 ==> {}", JSON.toJSONString(inno72SupplyChannels));

		if (inno72SupplyChannels.size() == 0) {
			return Results.failure("没有商品!");
		}

		Map<String, GoodsVo> goodsVoMap = new HashMap<>();

		for (Inno72SupplyChannel inno72SupplyChannel : inno72SupplyChannels) {

			String goodsCode = inno72SupplyChannel.getGoodsCode();
			String code = inno72SupplyChannel.getCode();
			Integer goodsCount = inno72SupplyChannel.getGoodsCount();

			GoodsVo goodsVo = goodsVoMap.get(goodsCode);

			if (goodsVo == null) {
				goodsVo = new GoodsVo(goodsCode, 0, inno72SupplyChannel.getGoodsName());
			}

			int goodsNum = goodsVo.getGoodsNum();

			List<String> chanelIds = goodsVo.getChannelIds();

			if (goodsCount != null && goodsCount > 0) {
				goodsVo.setGoodsNum(goodsNum + goodsCount);
				chanelIds.add(code);
				goodsVo.setChannelIds(chanelIds);
			}

			goodsVoMap.put(goodsCode, goodsVo);

		}

		boolean canOrder = this.countSuccOrder(channelId, userSessionVo.getUserId(), activityPlanId);
		Collection<GoodsVo> values = goodsVoMap.values();

		Map<String, Object> result = new HashMap<>();
		result.put("canOrder", canOrder);
		result.put("goods", values);

		LOGGER.debug("查询商品结果 ==> {}", JSON.toJSONString(result));
		return Results.success(result);

	}

	/**
	 * String machineId,
	 * String gameId,
	 * String report
	 */
	@Override
	public Result<Object> order(MachineApiVo vo) {

		String jstUrl = inno72GameServiceProperties.get("jstUrl");

		if (StringUtil.isEmpty(jstUrl)) {
			return Results.failure("配置中心无聚石塔配置路径!");
		}
		//新增参数
		String activityPlanId = vo.getActivityPlanId();
		String channelId = vo.getChannelId();
		String machineId = vo.getMachineId();
		String activityId = vo.getActivityId();
		String sessionUuid = vo.getSessionUuid();
		String itemId = vo.getItemId();

		Inno72Machine inno72Machine = inno72MachineMapper.findMachineByCode(machineId);
		if ( inno72Machine == null ){
			return Results.failure("机器信息错误!");
		}
		String _machineId = inno72Machine.getId();

		//新增
		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
		if (userSessionVo == null) {
			return Results.failure("登录失效!");
		}
		LOGGER.info("将下单的session =====> {}" , JSON.toJSONString(userSessionVo));
		//下单 inno72_Order
		String inno72OrderId = genInno72Order(channelId, activityPlanId, _machineId, itemId, userSessionVo.getUserId());
		userSessionVo.setInno72OrderId(inno72OrderId);
		gameSessionRedisUtil.setSessionEx(sessionUuid, JSON.toJSONString(userSessionVo));
		String accessToken = userSessionVo.getAccessToken();
		LOGGER.info("更新的session =====> {}" , JSON.toJSONString(userSessionVo));
		if (inno72OrderId.equals("0")){
			LOGGER.info("已经超过最大游戏数量啦 QAQ!");
			return Results.failure("已经超过最大游戏数量啦 QAQ!");
		}

		Map<String, String> requestForm = new HashMap<>();
		requestForm.put("accessToken", accessToken);
		requestForm.put("activityId", activityId);
		requestForm.put("mid", machineId); // 实际为code
		requestForm.put("goodsId", itemId);

		String respJson;
		try {
			LOGGER.info("调用聚石塔下单接口 参数 ======》 {}" ,JSON.toJSONString(requestForm));
			respJson = HttpClient.form(jstUrl + "/api/top/order", requestForm, null);
		} catch (Exception e) {
			LOGGER.info("调用聚石塔失败 ! {}", e.getMessage(), e);
			return Results.failure("聚石塔调用失败!");
		}

		if (StringUtil.isEmpty(respJson)) {
			return Results.failure("聚石塔无返回数据!");
		}

		LOGGER.info("调用聚石塔接口  【下单】返回 ===> {}", respJson);

		try {

			/*
			 * {
					"tmall_fans_automachine_order_createorderbyitemid_response": {
						"result": {
							"model": {
								"actual_fee": 1,
								"order_id": "185028768691768199",
								"pay_qrcode_image": "https:\/\/img.alicdn.com\/tfscom\/TB1lElXE9tYBeNjSspkwu2U8VXa.png"
							},
							"msg_code": "SUCCESS"
						},
						"request_id": "43ecpzeb5fdn"
					}
				}
			 */

			String msg_code = FastJsonUtils.getString(respJson, "msg_code");
			if (!msg_code.equals("SUCCESS")) {
				String msg_info = FastJsonUtils.getString(respJson, "msg_info");
				return Results.failure(msg_info);
			}

			//更新第三方订单号进inno72 order
			this.updateRefOrderId(inno72OrderId, respJson, userSessionVo);
			Map<String, Object> mapToUpperCase = mapToUpperCase(JSON.parseObject(FastJsonUtils.getString(respJson, "model")));
			mapToUpperCase.put("time", new Date().getTime());
			mapToUpperCase.put("inno72OrderId", inno72OrderId);
			return Results.success(mapToUpperCase);

		} catch (Exception e) {

			LOGGER.error("解析聚石塔返回数据异常! ===>  {}", e.getMessage(), e);
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
	public Result<Boolean> orderPolling(MachineApiVo vo) {
		String jstUrl = inno72GameServiceProperties.get("jstUrl");
		if (StringUtil.isEmpty(jstUrl)) {
			return Results.failure("配置中心无聚石塔配置路径!");
		}

		String sessionUuid = vo.getSessionUuid();
		String orderId = vo.getOrderId();

		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
		if (userSessionVo == null) {
			return Results.failure("登录失效!");
		}

		String accessToken = userSessionVo.getAccessToken();
		Map<String, String> requestForm = new HashMap<>();

		requestForm.put("accessToken", accessToken);
		requestForm.put("orderId", orderId);

		String respJson = HttpClient.form(jstUrl + "/api/top/order-polling", requestForm, null);
		if (StringUtil.isEmpty(respJson)) {
			return Results.failure("聚石塔无返回数据!");
		}

		LOGGER.info("调用聚石塔接口  【下单支付状态】返回 ===> {}", JSON.toJSONString(respJson));

		try {
			String msg_code = FastJsonUtils.getString(respJson, "msg_code");

			if (!msg_code.equals("SUCCESS")) {
				String msg_info = FastJsonUtils.getString(respJson, "msg_info");
				return Results.failure(msg_info);
			}

			boolean model = Boolean.valueOf(FastJsonUtils.getString(respJson, "model"));

			if (model) {
				Inno72Order inno72Order = inno72OrderMapper.selectByRefOrderId(orderId);
				if (inno72Order != null) {
					inno72Order.setPayStatus(Inno72Order.INNO72ORDER_PAYSTATUS.SUCC.getKey());
					inno72Order.setPayTime(LocalDateTime.now());
					inno72OrderMapper.updateByPrimaryKeySelective(inno72Order);
					inno72OrderHistoryMapper.insert(
							new Inno72OrderHistory(inno72Order.getId(), inno72Order.getOrderNum(), JSON.toJSONString(inno72Order), "修改状态为已支付")
							);
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
		if (StringUtil.isEmpty(jstUrl)) {
			return Results.failure("配置中心无聚石塔配置路径!");
		}

		String sessionUuid = vo.getSessionUuid();
		String ua = vo.getUa();
		String umid = vo.getUmid();
		String report = vo.getReport();
		String activityPlanId = vo.getActivityPlanId();

		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
		if (userSessionVo == null) {
			return Results.failure("登录失效!");
		}

		// 查奖池ID
		Map<String,String> selectCouponParam = new HashMap<>();
		selectCouponParam.put("activityPlanId",activityPlanId);
		selectCouponParam.put("report",report);
		String interactId = inno72ActivityPlanMapper.selectCouponCodeByParam(selectCouponParam);
		if ( StringUtil.isEmpty(interactId)){
			return Results.failure("没有有效的奖券了!");
		}

		// 查商户CODE
		String shopId = inno72MerchantMapper.selectShopCodeByPlanId(activityPlanId);
		if ( StringUtil.isEmpty(shopId)){
			return Results.failure("商户好像出了点问题!");
		}

		String accessToken = userSessionVo.getAccessToken();

		Map<String, String> requestForm = new HashMap<>();

		requestForm.put("accessToken", accessToken);
		requestForm.put("ua", ua); //安全ua
		requestForm.put("umid", umid);//umid
		requestForm.put("interactId", interactId);//互动实例ID
		requestForm.put("shopId", shopId);//店铺ID

		String requestUrl = jstUrl + "/api/top/lottory";

		LOGGER.info("请求聚石塔 url ===> {} , 参数 ===> {}", requestUrl, JSON.toJSONString(requestForm));

		String respJson = HttpClient.form(requestUrl, requestForm, null);

		if (StringUtil.isEmpty(respJson)) {
			return Results.failure("聚石塔无返回数据!");
		}

		LOGGER.info("调用聚石塔接口  【抽奖】返回 ===> {}", JSON.toJSONString(respJson));

		try {

			boolean msg_code = Boolean.parseBoolean(FastJsonUtils.getString(respJson,"succ"));
			if (!msg_code) {
				String msg_info = FastJsonUtils.getString(respJson,"sub_msg");
				LOGGER.info("抽奖失败 ===> {}", msg_info);
				return Results.failure(msg_info);
			}

			String data = FastJsonUtils.getString(respJson,"data");
			LOGGER.info("结果数据 ====> {}", data);
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

		String machineCode = vo.getMachineId();
		String channelId = vo.getChannelId();
		String sessionUuid = vo.getSessionUuid();
		String orderId = vo.getOrderId();

		if (StringUtil.isEmpty(machineCode) || StringUtil.isEmpty(channelId) || StringUtil.isEmpty(sessionUuid)
				|| StringUtil.isEmpty(orderId)) {
			return Results.failure("参数缺失！");
		}

		Inno72Machine machineByCode = inno72MachineMapper.findMachineByCode(machineCode);

		if (machineByCode == null){
			LOGGER.info("机器不存在! ===> {}" , JSON.toJSONString(vo));
		}

		String machineId = machineByCode.getId();

		String jstUrl = inno72GameServiceProperties.get("jstUrl");
		if (StringUtil.isEmpty(jstUrl)) {
			return Results.failure("配置中心无聚石塔配置路径!");
		}

		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
		if (userSessionVo == null) {
			return Results.failure("登录失效!");
		}

		String accessToken = userSessionVo.getAccessToken();

		Map<String, String> requestForm = new HashMap<>();

		requestForm.put("accessToken", accessToken);
		requestForm.put("orderId", orderId); //安全ua
		requestForm.put("mid", machineCode);//umid 实际为code
		requestForm.put("channelId", channelId);//互动实例ID

		String respJson = "";
		try {
			respJson = HttpClient.form(jstUrl + "/api/top/deliveryRecord", requestForm, null);
		} catch (Exception e) {
			LOGGER.info("");
		}
		if (StringUtil.isEmpty(respJson)) {
			return Results.failure("聚石塔无返回数据!");
		}

		LOGGER.info("调用聚石塔接口  【通知出货】返回 ===> {}", JSON.toJSONString(respJson));

		String msg_code = FastJsonUtils.getString(respJson, "msg_code");
		if (!msg_code.equals("SUCCESS")) {
			String msg_info = FastJsonUtils.getString(respJson, "msg_info");
			return Results.failure(msg_info);
		}


		int i = inno72SupplyChannelMapper.subCount(new Inno72SupplyChannel(machineId, null, channelId));

		LOGGER.info("减货结果 ==> {}", i);

		this.updateOrderReport(userSessionVo);

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

		if (StringUtil.isEmpty(access_token)) {
			return Results.failure("access_token 参数缺失！");
		}

		//判断是否有他人登录以及二维码是否过期
		String qrStatus = QRSTATUS_NORMAL;
		if (!StringUtil.isEmpty(sessionUuid)) {	
			LOGGER.info("sessionUuid is {}", sessionUuid);
			// 判断二维码是否过期
			boolean result = gameSessionRedisUtil.hasKey(sessionUuid + "_qrCode");
			LOGGER.info("qrCode hasKey result {} ", result);
			if (!result) {
				qrStatus = QRSTATUS_INVALID;
				LOGGER.info("二维码已经过期");
			} else {
				UserSessionVo sessionStr = gameSessionRedisUtil.getSessionKey(sessionUuid);
				if (sessionStr != null) {
					qrStatus = QRSTATUS_EXIST_USER;
					LOGGER.info("已经有用户正在操作");
				}
			}
		} else {
			return Results.failure("参数缺失！");
		}

		List<Inno72ActivityPlan> inno72ActivityPlans = inno72ActivityPlanMapper.selectByMachineId(mid);

		String gameId = "";
		String playCode = "";

		Inno72ActivityPlan inno72ActivityPlan = null;
		if (inno72ActivityPlans.size() > 0) {
			inno72ActivityPlan = inno72ActivityPlans.get(0);
			gameId = inno72ActivityPlan.getGameId();
		}

		if (StringUtil.isEmpty(gameId)) {
			return Results.failure("没有绑定的游戏！");
		}

		Inno72Game inno72Game = inno72GameMapper.selectByPrimaryKey(gameId);
		if (inno72Game == null) {
			return Results.failure("不存在的游戏！");
		}

		assert inno72ActivityPlan != null;
		Inno72Activity inno72Activity = inno72ActivityMapper.selectByPrimaryKey(inno72ActivityPlan.getActivityId());
		String sellerId = inno72Activity.getSellerId();
		playCode = inno72Activity.getCode();
		LOGGER.info("sessionRedirect layCode is {}", playCode);

		Inno72Merchant inno72Merchant = inno72MerchantMapper.selectByPrimaryKey(sellerId);


		String jstUrl = inno72GameServiceProperties.get("jstUrl");
		if (StringUtil.isEmpty(jstUrl)) {
			return Results.failure("配置中心无聚石塔配置路径!");
		}

		Map<String, String> requestForm = new HashMap<>();
		requestForm.put("accessToken", access_token);
		requestForm.put("mid", mid);
		requestForm.put("sellerId", inno72Merchant.getMerchantCode());
		/*
		 * <tmall_fans_automachine_getmaskusernick_response>
		 *     <msg_code>200</msg_code>
		 *     <msg_info>用户不存在</msg_info>1dd77fc18f3a409196de23baedcf8ce1
		 *     <model>e****丫</model>
		 * </tmall_fans_automachine_getmaskusernick_response>
		 */
		String respJson = HttpClient.form(jstUrl + "/api/top/getMaskUserNick", requestForm, null);
		LOGGER.info("调用聚石塔接口  【请求nickName】返回 ===> {}", JSON.toJSONString(respJson));

		if (StringUtil.isEmpty(respJson)) {
			return Results.failure("请求用户名失败!");
		}

		String nickName = FastJsonUtils.getString(respJson,"model");

		UserSessionVo sessionVo = new UserSessionVo(mid, nickName, userId, access_token, gameId, sessionUuid);

		gameSessionRedisUtil.setSessionEx(sessionUuid, JSON.toJSONString(sessionVo));

		String channelId = inno72Merchant.getChannelId();

		Inno72Channel inno72Channel = inno72ChannelMapper.selectByPrimaryKey(channelId);

		Map<String, String> userChannelParams = new HashMap<>();
		userChannelParams.put("channelId", channelId);
		userChannelParams.put("channelUserKey", userId);
		Inno72GameUserChannel userChannel = inno72GameUserChannelMapper.selectByChannelUserKey(userChannelParams);

		if (userChannel == null){
			Inno72GameUser inno72GameUser = new Inno72GameUser();
			inno72GameUserMapper.insert(inno72GameUser);
			LOGGER.info("插入游戏用户表 完成 ===> {}", JSON.toJSONString(inno72GameUser));
			userChannel = new Inno72GameUserChannel(nickName
			, "", channelId, inno72GameUser.getId(), inno72Channel.getChannelName(), userId);
			inno72GameUserChannelMapper.insert(userChannel);
			LOGGER.info("插入游戏用户渠道表 完成 ===> {}", JSON.toJSONString(userChannel));

		}
		Inno72Machine inno72Machine = inno72MachineMapper.selectByPrimaryKey(mid);
		this.startGameLife(userChannel, inno72Activity, inno72ActivityPlan, inno72Game, inno72Machine, userId);

		LOGGER.info("playCode is" + playCode);

		//调用聚石塔日志
		Map<String, String> requestLogForm = new HashMap<String,String>();
		requestLogForm.put("accessToken", token);
		LogReqrest logReqrest = getLogReqrest(null, null, Long.valueOf(inno72Merchant.getMerchantCode()), "login",
				-1l,inno72Machine.getMachineCode(), null, null, null);
		requestLogForm.put("logReqrest", JSON.toJSONString(logReqrest));
		LOGGER.info("聚石塔日志接口参数 requestLogForm ："+JSONObject.toJSONString(requestLogForm));
		String result = HttpClient.form(jstUrl + "/api/top/addLog", requestLogForm, null);
		LOGGER.info("聚石塔日志接口返回 ", JSON.toJSONString(result));
		String msg_logCode = FastJsonUtils.getString(result, "msg_code");
		if (!"SUCCESS".equals(msg_logCode)) {
		   String msg_info = FastJsonUtils.getString(result, "msg_info");
		   LOGGER.info("调用聚石塔日志接口 ===> {}", JSON.toJSONString(msg_info));
		}
		
		Map<String, Object> resultMap = new HashMap<String,Object>();
		resultMap.put("playCode", playCode);
		resultMap.put("qrStatus", qrStatus);
		resultMap.put("sellerId", inno72Merchant.getMerchantCode());

		return Results.success(JSONObject.toJSONString(resultMap));
	}

	@Override
	public Result<String> malfunctionLog(String machineId,String channelCode){

		Inno72Machine inno72Machine = inno72MachineMapper.selectByPrimaryKey(machineId);
		//将货道故障信息推送到预警系统
		AlarmMessageBean<String> alarmMessageBean = new AlarmMessageBean<>();
		alarmMessageBean.setSystem("machineChannel");
		alarmMessageBean.setType("machineChannelException");
		alarmMessageBean.setData(inno72Machine.getMachineCode());
		Map<String, Object> map = new HashMap<>();
		map.put("channelCode", channelCode);
		map.put("alarmMessage", alarmMessageBean);
		redisUtil.publish("moniterAlarm",JSONObject.toJSONString(map));

		return Results.success();
	}

	@Override
	public Result<String> shipmentFail(String machineId,String channelCode,String describtion){

		AlarmMessageBean<Object> alarmMessageBean = new AlarmMessageBean<Object>();
		MachineDropGoodsBean machineDropGoodsBean = new MachineDropGoodsBean();
		LOGGER.info("掉货失败参数 => machineId:{}; channelCode:{}; describtion:{}", machineId, channelCode, describtion);
		Inno72Machine inno72Machine = inno72MachineMapper.findMachineByCode(machineId);
		if(inno72Machine==null) {
			return Results.failure("机器号错误");
		}
		if(inno72Machine.getMachineStatus()==4&&inno72Machine.getOpenStatus()==0) {
			machineDropGoodsBean.setMachineCode(machineId);  // 实际为code
			machineDropGoodsBean.setChannelNum(channelCode);
			alarmMessageBean.setSystem("machineDropGoods");
			alarmMessageBean.setType("machineDropGoodsException");
			alarmMessageBean.setData(machineDropGoodsBean);
			redisUtil.publish("moniterAlarm",JSONObject.toJSONString(alarmMessageBean));
		}
		
		return Results.success();
	}
	private void startGameLife(Inno72GameUserChannel userChannel, Inno72Activity inno72Activity, Inno72ActivityPlan inno72ActivityPlan,
			Inno72Game inno72Game, Inno72Machine inno72Machine, String userId) {
		Inno72Locale inno72Locale = inno72LocaleMapper.selectByPrimaryKey(inno72Machine.getLocaleId());
		Inno72GameUserLife life = new Inno72GameUserLife(userChannel.getGameUserId(), userChannel.getId(), inno72Machine.getMachineCode(),
				userChannel.getUserNick(), inno72ActivityPlan.getActivityId(), inno72Activity.getName(),
				inno72ActivityPlan.getId(), inno72Game.getId(), inno72Game.getName(), inno72Machine.getLocaleId(), inno72Locale == null ? "" : inno72Locale.getMall(),
						null, "", null,null, userId);
		LOGGER.info("插入用户游戏记录 ===> {}", JSON.toJSONString(life));
		inno72GameUserLifeMapper.insert(life);

	}

	private void updateRefOrderId(String inno72OrderId, String respJson, UserSessionVo userSessionVo) {
		//更新订单
		Inno72Order inno72Order = inno72OrderMapper.selectByPrimaryKey(inno72OrderId);
		String orderId = FastJsonUtils.getString(respJson, "order_id");
		inno72Order.setId(inno72OrderId);
		inno72Order.setRefOrderId(orderId);
		inno72OrderMapper.updateByPrimaryKeySelective(inno72Order);

		//插入订单历史
		Inno72OrderHistory history = new Inno72OrderHistory();
		history.setDetails("更新第三方订单号");
		history.setHistoryOrder(JSON.toJSONString(inno72Order));
		history.setOrderId(inno72Order.getId());
		history.setOrderNum(inno72Order.getOrderNum());
		inno72OrderHistoryMapper.insert(history);

		Inno72GameUserLife userLife = inno72GameUserLifeMapper.selectByUserChannelIdLast(userSessionVo.getUserId());
		userLife.setOrderId(inno72OrderId);
		inno72GameUserLifeMapper.updateByPrimaryKeySelective(userLife);
	}
	private void updateOrderReport( UserSessionVo userSessionVo) {
		//更新订单
		String inno72OrderId = userSessionVo.getInno72OrderId();
		Inno72Order inno72Order = inno72OrderMapper.selectByPrimaryKey(inno72OrderId);
		inno72Order.setId(inno72OrderId);
		inno72Order.setGoodsStatus(Inno72Order.INNO72ORDER_GOODSSTATUS.SUCC.getKey());
		inno72OrderMapper.updateByPrimaryKeySelective(inno72Order);

		//插入订单历史
		Inno72OrderHistory history = new Inno72OrderHistory();
		history.setDetails("更新订单游戏结果");
		history.setHistoryOrder(JSON.toJSONString(inno72Order));
		history.setOrderId(inno72Order.getId());
		history.setOrderNum(inno72Order.getOrderNum());
		inno72OrderHistoryMapper.insert(history);

		Inno72GameUserLife userLife = inno72GameUserLifeMapper.selectByUserChannelIdLast(userSessionVo.getUserId());
		userLife.setGameResult("1");
		inno72GameUserLifeMapper.updateByPrimaryKeySelective(userLife);
	}

	private boolean countSuccOrder(String channelId, String channelUserKey, String activityPlanId){

		Map<String, String> paramsChannel = new HashMap<>();
		paramsChannel.put("channelId", channelId);
		paramsChannel.put("channelUserKey", channelUserKey);
		Inno72GameUserChannel userChannel = inno72GameUserChannelMapper.selectByChannelUserKey(paramsChannel);
		String gameUserId = userChannel.getGameUserId();

		Map<String, String> orderParams = new HashMap<>();
		orderParams.put("activityPlanId", activityPlanId);
		orderParams.put("gameUserId", gameUserId);
		List<Inno72Order> inno72Orders = inno72OrderMapper.findGoodsStatusSucc(orderParams);

		Inno72ActivityPlan inno72ActivityPlan = inno72ActivityPlanMapper.selectByPrimaryKey(activityPlanId);
		Integer userMaxTimes = inno72ActivityPlan.getUserMaxTimes();

		return userMaxTimes > inno72Orders.size();
	}

	private String genInno72Order(String channelId, String activityPlanId, String machineId, String goodsId, String channelUserKey) {

		Map<String, String> paramsChannel = new HashMap<>();
		paramsChannel.put("channelId", channelId);
		paramsChannel.put("channelUserKey", channelUserKey);
		Inno72GameUserChannel userChannel = inno72GameUserChannelMapper.selectByChannelUserKey(paramsChannel);
		String gameUserId = userChannel.getGameUserId();

		//活动计划
		Inno72ActivityPlan inno72ActivityPlan = inno72ActivityPlanMapper.selectByPrimaryKey(activityPlanId);
		// 活动
		Inno72Activity inno72Activity = inno72ActivityMapper.selectByPrimaryKey(inno72ActivityPlan.getActivityId());

		Inno72Machine inno72Machine = inno72MachineMapper.selectByPrimaryKey(machineId);

		Inno72Channel inno72Channel = inno72ChannelMapper.selectByPrimaryKey(channelId);

		Inno72Shops inno72Shops = inno72ShopsMapper.selectByPrimaryKey(inno72Activity.getShopId());

		String orderNum = Inno72OrderNumGenUtil.genOrderNum(inno72Channel.getChannelCode(),inno72Machine.getMachineCode());
		LocalDateTime now = LocalDateTime.now();

		Map<String, String> orderParams = new HashMap<>();
		orderParams.put("activityPlanId", activityPlanId);
		orderParams.put("gameUserId", gameUserId);
		List<Inno72Order> inno72Orders = inno72OrderMapper.findGoodsStatusSucc(orderParams);
		Integer rep = inno72Orders.size() > inno72ActivityPlan.getUserMaxTimes() ?
				Inno72Order.INNO72ORDER_REPETITION.REPETITION.getKey() :
				Inno72Order.INNO72ORDER_REPETITION.NOT.getKey();
		Inno72Order inno72Order = new Inno72Order();
		inno72Order.setChannelId(channelId);
		inno72Order.setGoodsStatus(Inno72Order.INNO72ORDER_GOODSSTATUS.WAIT.getKey());
		inno72Order.setInno72ActivityId(inno72ActivityPlan.getActivityId());
		inno72Order.setInno72ActivityPlanId(activityPlanId);
		inno72Order.setMachineId(machineId);
		inno72Order.setMerchantId(inno72Activity.getSellerId());
		inno72Order.setOrderNum(orderNum);
		inno72Order.setOrderPrice(BigDecimal.ZERO);
		inno72Order.setOrderTime(now);
		inno72Order.setOrderType(Inno72Order.INNO72ORDER_ORDERTYPE.DEFAULT.getKey());
		inno72Order.setPayPrice(BigDecimal.ZERO);
		inno72Order.setPayStatus(Inno72Order.INNO72ORDER_PAYSTATUS.WAIT.getKey());
		inno72Order.setPayTime(null);
		inno72Order.setRefOrderId(null);
		inno72Order.setRefOrderStatus(null);
		inno72Order.setRepetition(rep);
		inno72Order.setShopsId(inno72Shops.getId());
		inno72Order.setShopsName(inno72Shops.getShopName());
		inno72Order.setUserId(gameUserId);
		inno72OrderMapper.insert(inno72Order);

		Inno72Goods inno72Goods = inno72GoodsMapper.selectByCode(goodsId);
		Inno72OrderGoods orderGoods = new Inno72OrderGoods();
		orderGoods.setGoodsCode(inno72Goods.getCode());
		orderGoods.setGoodsId(inno72Goods.getId());
		orderGoods.setGoodsName(inno72Goods.getName());
		orderGoods.setGoodsPrice(inno72Goods.getPrice());
		orderGoods.setGoodsType(Inno72OrderGoods.INNO72ORDERGOODS_GOODSTYPE.PRODUCT.getKey());
		orderGoods.setOrderId(inno72Order.getId());
		orderGoods.setOrderNum(inno72Order.getOrderNum());
		orderGoods.setStatus(Inno72Order.INNO72ORDER_GOODSSTATUS.WAIT.getKey());
		inno72OrderGoodsMapper.insert(orderGoods);

		inno72OrderHistoryMapper.insert(
				new Inno72OrderHistory(inno72Order.getId(), inno72Order.getOrderNum(), JSON.toJSONString(inno72Order), "初始化插入订单!")
				);

		return rep == 0 ? rep+"" : inno72Order.getId();
	}

	private Map<String, Object> mapToUpperCase(JSONObject jObject) {
		Map<String, Object> result = new HashMap<>();
		if (jObject != null) {
			for (Map.Entry<String, Object> element : jObject.entrySet()) {
				result.put(StringUtil.strToUpperCase(element.getKey()), element.getValue());
			}
		}
		return result;
	}


	private LogReqrest getLogReqrest(String bizCode, Long itemId, Long sellerId, String type, Long userId,
			String value1, String value2, Long value3, Long value4) {
		LogReqrest logReqrest = new LogReqrest(bizCode, itemId, sellerId, type, -1L, value1, value2, value3, value4);
		return logReqrest;
	}

	@Override
	public Result<String> userDuration(String token, String itemId, String sellerId, String userId, String machineCode, String playTime) {
		LOGGER.info("用户互动时长 => itemId:{}; sellerId:{}; userId:{}; token:{}", itemId, sellerId, userId, token);
		//调用聚石塔日志
		Map<String, String> requestLogForm = new HashMap<String,String>();
		requestLogForm.put("accessToken", token);
		LogReqrest logReqrest = getLogReqrest("", Long.valueOf(itemId), Long.valueOf(sellerId),
				"用户互动时常", Long.valueOf(userId), machineCode, playTime, null, null);
		requestLogForm.put("logReqrest", JSON.toJSONString(logReqrest));
		LOGGER.info("聚石塔日志接口参数 requestLogForm ："+JSONObject.toJSONString(requestLogForm));

		String jstUrl = inno72GameServiceProperties.get("jstUrl");
		if (StringUtil.isEmpty(jstUrl)) {
			return Results.failure("配置中心无聚石塔配置路径!");
		}
		String result = HttpClient.form(jstUrl + "/api/top/addLog", requestLogForm, null);
		LOGGER.info("聚石塔日志接口返回 ", JSON.toJSONString(result));
		String msg_logCode = FastJsonUtils.getString(result, "msg_code");
		System.out.println("++++++++++++++++++"+msg_logCode);
		String msg_info = FastJsonUtils.getString(result, "msg_info");
		if (!"SUCCESS".equals(msg_logCode)) {
		   LOGGER.info("调用聚石塔日志接口 ===> {}", JSON.toJSONString(msg_info));
		   return Results.failure(msg_info);
		}
		return Results.success(msg_info);
	}

}
