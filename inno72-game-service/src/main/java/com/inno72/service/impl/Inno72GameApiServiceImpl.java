package com.inno72.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.common.CommonBean;
import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.common.util.Inno72OrderNumGenUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.Inno72ActivityMapper;
import com.inno72.mapper.Inno72ActivityPlanGameResultMapper;
import com.inno72.mapper.Inno72ActivityPlanMapper;
import com.inno72.mapper.Inno72ChannelMapper;
import com.inno72.mapper.Inno72CouponMapper;
import com.inno72.mapper.Inno72GameMapper;
import com.inno72.mapper.Inno72GameUserChannelMapper;
import com.inno72.mapper.Inno72GameUserLifeMapper;
import com.inno72.mapper.Inno72GameUserMapper;
import com.inno72.mapper.Inno72GoodsMapper;
import com.inno72.mapper.Inno72LocaleMapper;
import com.inno72.mapper.Inno72MachineMapper;
import com.inno72.mapper.Inno72MerchantMapper;
import com.inno72.mapper.Inno72OrderGoodsMapper;
import com.inno72.mapper.Inno72OrderHistoryMapper;
import com.inno72.mapper.Inno72OrderMapper;
import com.inno72.mapper.Inno72ShopsMapper;
import com.inno72.mapper.Inno72SupplyChannelMapper;
import com.inno72.model.Inno72Activity;
import com.inno72.model.Inno72ActivityPlan;
import com.inno72.model.Inno72ActivityPlanGameResult;
import com.inno72.model.Inno72Channel;
import com.inno72.model.Inno72Coupon;
import com.inno72.model.Inno72Game;
import com.inno72.model.Inno72GameUser;
import com.inno72.model.Inno72GameUserChannel;
import com.inno72.model.Inno72GameUserLife;
import com.inno72.model.Inno72Goods;
import com.inno72.model.Inno72Locale;
import com.inno72.model.Inno72Machine;
import com.inno72.model.Inno72Merchant;
import com.inno72.model.Inno72Order;
import com.inno72.model.Inno72OrderGoods;
import com.inno72.model.Inno72OrderHistory;
import com.inno72.model.Inno72Shops;
import com.inno72.model.Inno72SupplyChannel;
import com.inno72.model.MachineDropGoodsBean;
import com.inno72.plugin.http.HttpClient;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.Inno72GameApiService;
import com.inno72.service.Inno72GameService;
import com.inno72.vo.AlarmMessageBean;
import com.inno72.vo.GoodsVo;
import com.inno72.vo.Inno72SamplingGoods;
import com.inno72.vo.LogReqrest;
import com.inno72.vo.MachineApiVo;
import com.inno72.vo.UserSessionVo;

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
	@Resource
	private Inno72CouponMapper inno72CouponMapper;
	@Resource
	private IRedisUtil redisUtil;
	@Resource
	private Inno72GameService inno72GameService;

	private static final String QRSTATUS_NORMAL = "0"; // 二维码正常
	private static final String QRSTATUS_INVALID = "-1"; // 二维码失效
	private static final String QRSTATUS_EXIST_USER = "-2"; // 存在用户登录

	private static final Integer SAMPLING_TYPE = 1; // 类型（派样）

	/**
	 * {
	 goodsId: 1,
	 goodsNum: 1,
	 goodsName: '哈啤',
	 chanelId: [1,2]
	 }
	 */
	@Override
	public Result<Object> findProduct(MachineApiVo vo) {

		String activityPlanId = vo.getActivityPlanId();
		String report = vo.getReport();
		String sessionUuid = vo.getSessionUuid();
		String machineId = vo.getMachineId();

		String channelId = vo.getChannelId();

		if (StringUtil.isEmpty(sessionUuid) || StringUtil.isEmpty(activityPlanId)) {
			LOGGER.info("查询商品参数错误 ! ===> {}", JSON.toJSONString(vo));
			return Results.failure("参数错误!");
		}
		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
		if (userSessionVo == null) {
			return Results.failure("登录过期啦 !");
		}
		userSessionVo.setGameReport(report);
		gameSessionRedisUtil.setSessionEx(sessionUuid, JSON.toJSONString(userSessionVo));

		Map<String, String> params = new HashMap<>();
		params.put("activityPlanId", activityPlanId);
		params.put("report", report);

		List<String> resultGoodsId = inno72ActivityPlanGameResultMapper.selectByActivityPlanId(params);

		if (resultGoodsId.size() == 0) {
			return Results.failure("没有奖品哦.");
		}

		Inno72Machine inno72Machine = inno72MachineMapper.findMachineByCode(machineId);
		if (inno72Machine == null) {
			return Results.failure("机器信息错误!");
		}

		// 请求接口 获取出货 货道号
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

		boolean canOrder = inno72GameService.countSuccOrder(channelId, userSessionVo.getUserId(), activityPlanId);
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
		// 新增参数
		String activityPlanId = vo.getActivityPlanId();
		String channelId = vo.getChannelId();
		String machineId = vo.getMachineId();
		String activityId = vo.getActivityId();
		String sessionUuid = vo.getSessionUuid();
		String itemId = vo.getItemId();

		Inno72Machine inno72Machine = inno72MachineMapper.findMachineByCode(machineId);
		if (inno72Machine == null) {
			return Results.failure("机器信息错误!");
		}
		String _machineId = inno72Machine.getId();

		// 新增
		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
		if (userSessionVo == null) {
			return Results.failure("登录失效!");
		}
		LOGGER.debug("将下单的session =====> {}", JSON.toJSONString(userSessionVo));
		// 下单 inno72_Order TODO 商品下单 itemId 对应的类型？
		String inno72OrderId = genInno72Order(channelId, activityPlanId, _machineId, itemId, userSessionVo.getUserId(),
				Inno72Order.INNO72ORDER_GOODSTYPE.PRODUCT);

		String accessToken = userSessionVo.getAccessToken();
		LOGGER.info("更新的session =====> {}", JSON.toJSONString(userSessionVo));
		if (inno72OrderId.equals("0")) {
			LOGGER.info("已经超过最大游戏数量啦 QAQ!");
			return Results.failure("已经超过最大游戏数量啦 QAQ!");
		}

		new Thread(new SendOrder(inno72OrderId, itemId, activityId, machineId, sessionUuid, userSessionVo)).run();

		Map<String, Object> param = new HashMap<>(1);
		param.put("time", new Date().getTime());
		return Results.success(param);
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

		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
		if (userSessionVo == null) {
			return Results.failure("登录失效!");
		}

		String orderId = userSessionVo.getRefOrderId();
		if (StringUtil.isEmpty(orderId)) {
			LOGGER.info("第三方订单号不存在!");
			return Results.failure("请求失败");
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
					inno72OrderHistoryMapper.insert(new Inno72OrderHistory(inno72Order.getId(),
							inno72Order.getOrderNum(), JSON.toJSONString(inno72Order), "修改状态为已支付"));

					Map<String, Object> goodsParams = new HashMap<>();
					goodsParams.put("goodsType", Inno72Order.INNO72ORDER_GOODSTYPE.PRODUCT.getKey());
					goodsParams.put("orderId", inno72Order.getId());
					Inno72OrderGoods goods = inno72OrderGoodsMapper.selectByOrderIdAndGoodsType(goodsParams);
					goods.setStatus(Inno72Order.INNO72ORDER_PAYSTATUS.SUCC.getKey());
					inno72OrderGoodsMapper.updateByPrimaryKeySelective(goods);
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
		// 新增 TODO
		String channelId = vo.getChannelId();
		String machineId = vo.getMachineId();

		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
		if (userSessionVo == null) {
			return Results.failure("登录失效!");
		}

		Inno72Machine inno72Machine = inno72MachineMapper.findMachineByCode(machineId);
		if (inno72Machine == null) {
			return Results.failure("机器信息错误!");
		}
		String _machineId = inno72Machine.getId();

		// 查奖池ID
		Map<String, String> selectCouponParam = new HashMap<>();
		selectCouponParam.put("activityPlanId", activityPlanId);
		selectCouponParam.put("report", report);
		String prizeId = inno72ActivityPlanGameResultMapper.selectPrizeId(selectCouponParam);
		// String interactId = inno72ActivityPlanMapper.selectCouponCodeByParam(selectCouponParam);
		if (StringUtil.isEmpty(prizeId)) {
			return Results.failure("没有有效的奖券了!");
		}

		Inno72Coupon inno72Coupon = inno72CouponMapper.selectByPrimaryKey(prizeId);
		if (inno72Coupon == null || StringUtil.isEmpty(inno72Coupon.getCode())) {
			return Results.failure("奖券飞了!");
		}

		// 查商户CODE
		String shopId = inno72MerchantMapper.selectShopCodeByPlanId(activityPlanId);
		if (StringUtil.isEmpty(shopId)) {
			return Results.failure("商户好像出了点问题!");
		}

		// TODO 奖券下单
		String orderId = "";
		try {
			orderId = this.genInno72Order(channelId, activityPlanId, _machineId, prizeId, userSessionVo.getUserId(),
					Inno72Order.INNO72ORDER_GOODSTYPE.COUPON);
		} catch (Exception e) {
			LOGGER.info("获取优惠券下单失败 ==> {}", e.getMessage(), e);
			return Results.failure("下单失败!");
		}

		String accessToken = userSessionVo.getAccessToken();

		Map<String, String> requestForm = new HashMap<>();

		requestForm.put("accessToken", accessToken);
		requestForm.put("ua", ua); // 安全ua
		requestForm.put("umid", umid);// umid
		requestForm.put("interactId", inno72Coupon.getCode());// 互动实例ID
		requestForm.put("shopId", shopId);// 店铺ID

		String requestUrl = jstUrl + "/api/top/lottory";

		LOGGER.info("请求聚石塔 url ===> {} , 参数 ===> {}", requestUrl, JSON.toJSONString(requestForm));

		String respJson = HttpClient.form(requestUrl, requestForm, null);

		if (StringUtil.isEmpty(respJson)) {
			return Results.failure("聚石塔无返回数据!");
		}

		LOGGER.info("调用聚石塔接口  【抽奖】返回 ===> {}", JSON.toJSONString(respJson));


		try {

			boolean msg_code = Boolean.parseBoolean(FastJsonUtils.getString(respJson, "succ"));
			if (!msg_code) {
				String msg_info = FastJsonUtils.getString(respJson, "sub_msg");
				LOGGER.info("抽奖失败 ===> {}", msg_info);
				return Results.failure(msg_info);
			}

			String is_success = FastJsonUtils.getString(respJson, "succ");
			if (is_success.equals("true")) {
				Inno72Order inno72Order = new Inno72Order();
				inno72Order.setId(orderId);
				inno72Order.setGoodsStatus(Inno72Order.INNO72ORDER_GOODSSTATUS.SUCC.getKey());
				inno72Order.setPayTime(LocalDateTime.now());
				inno72OrderMapper.updateByPrimaryKeySelective(inno72Order);
				inno72OrderHistoryMapper.insert(new Inno72OrderHistory(inno72Order.getId(), inno72Order.getOrderNum(),
						JSON.toJSONString(inno72Order), "修改状态为已发放优惠券"));
			}
			String data = FastJsonUtils.getString(respJson, "data");
			LOGGER.info("结果数据 ====> {}", data);
			JSONObject parseDataObject = JSON.parseObject(data);

			return Results.success(mapToUpperCase(parseDataObject));

		} catch (Exception e) {
			return Results.failure(e.getMessage());
		}
	}

	/**
	 * 下单，商品and优惠券
	 * @param vo
	 * @return
	 */
	@Override
	public Result<Object> oneKeyOrder(MachineApiVo vo) {

		String sessionUuid = vo.getSessionUuid();
		if (StringUtil.isEmpty(sessionUuid)) {
			return Results.success("参数错误!");
		}
		// 新增
		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(vo.getSessionUuid());
		if (userSessionVo == null) {
			return Results.failure("登录失效!");
		}
		String report = vo.getReport();
		String activityPlanId = vo.getActivityPlanId();

		Map<String, String> params = new HashMap<>();
		params.put("activityPlanId", activityPlanId);
		params.put("report", report);

		List<Inno72ActivityPlanGameResult> resultGoodsId = inno72ActivityPlanGameResultMapper
				.selectAllResultByCode(params);

		if (resultGoodsId.size() == 0) {
			return Results.failure("无配置商品!");
		}
		List<String> orderIds = new ArrayList<>();
		for (Inno72ActivityPlanGameResult result : resultGoodsId) {
			String prizeType = result.getPrizeType();
			switch (prizeType) {
				case "1":
					// 下商品订单
					String prizeId = result.getPrizeId();
					Inno72Goods inno72Goods = inno72GoodsMapper.selectByPrimaryKey(prizeId);
					if (inno72Goods == null) {
						LOGGER.info("讲真的。配置商品..它不见了。。。! 不信你看配置 ==> {}", JSON.toJSONString(result));
						return Results.failure("讲真的。配置商品..它不见了。。。!");
					}
					String code = inno72Goods.getCode();
					Result<String> orderResult = this.sendOrder(userSessionVo, code);
					if (orderResult.getCode() == Result.SUCCESS) {
						orderIds.add(orderResult.getData());
					}

					break;
				case "2":

					Result<Object> lottery = this.lottery(userSessionVo, vo.getUa(), vo.getUmid(), report);
					LOGGER.debug("抽取奖券 结果 ==> {}", JSON.toJSONString(lottery));
					// 下优惠券订单
					break;
				default:
					return Results.failure("无商品类型");
			}
		}
		Map<String, Object> result = new HashMap<>(2);
		result.put("orderIds", orderIds);
		result.put("time", new Date().getTime());
		return Results.success(result);
	}

	/**
	 * 重构合并抽奖和下单接口为一个接口。这是下单接口
	 * @param userSessionVo
	 * @param itemId
	 * @return
	 */
	private Result<String> sendOrder(UserSessionVo userSessionVo, String itemId) {

		String channelId = userSessionVo.getChannelId();
		String machineId = userSessionVo.getMachineCode();
		String activityPlanId = userSessionVo.getActivityPlanId();
		String activityId = userSessionVo.getActivityId();
		String sessionUuid = userSessionVo.getSessionUuid();
		String jstUrl = inno72GameServiceProperties.get("jstUrl");


		Inno72Machine inno72Machine = inno72MachineMapper.findMachineByCode(machineId);
		if (inno72Machine == null) {
			return Results.failure("机器信息错误!");
		}
		String _machineId = inno72Machine.getId();


		LOGGER.info("将下单的session =====> {}", JSON.toJSONString(userSessionVo));
		// 下单 inno72_Order TODO 商品下单 itemId 对应的类型？
		String inno72OrderId = genInno72Order(channelId, activityPlanId, _machineId, itemId, userSessionVo.getUserId(),
				Inno72Order.INNO72ORDER_GOODSTYPE.PRODUCT);

		userSessionVo.setInno72OrderId(inno72OrderId);
		gameSessionRedisUtil.setSessionEx(sessionUuid, JSON.toJSONString(userSessionVo));
		String accessToken = userSessionVo.getAccessToken();
		LOGGER.info("更新的session =====> {}", JSON.toJSONString(userSessionVo));
		if (inno72OrderId.equals("0")) {
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
			LOGGER.info("调用聚石塔下单接口 参数 ======》 {}", JSON.toJSONString(requestForm));
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
			 * { "tmall_fans_automachine_order_createorderbyitemid_response": { "result": { "model": { "actual_fee": 1,
			 * "order_id": "185028768691768199", "pay_qrcode_image":
			 * "https:\/\/img.alicdn.com\/tfscom\/TB1lElXE9tYBeNjSspkwu2U8VXa.png" }, "msg_code": "SUCCESS" },
			 * "request_id": "43ecpzeb5fdn" } }
			 */

			String msg_code = FastJsonUtils.getString(respJson, "msg_code");
			if (!msg_code.equals("SUCCESS")) {
				String msg_info = FastJsonUtils.getString(respJson, "msg_info");
				return Results.failure(msg_info);
			}

			// 更新第三方订单号进inno72 order
			Result<String> stringResult = inno72GameService.updateRefOrderId(inno72OrderId, respJson,
					userSessionVo.getUserId());
			LOGGER.info("修改第三方订单进inno72——order 结果 {}", JSON.toJSONString(stringResult));
			return Results.success(inno72OrderId);

		} catch (Exception e) {
			LOGGER.error("解析聚石塔返回数据异常! ===>  {}", e.getMessage(), e);
			return Results.failure("解析聚石塔返回数据异常!");
		}
	}

	/**
	 * 重构合并抽奖和下单接口为一个接口。这是抽奖接口
	 * @param vo
	 * @param ua
	 * @param umid
	 * @param report
	 * @return
	 */
	private Result<Object> lottery(UserSessionVo vo, String ua, String umid, String report) {

		String jstUrl = inno72GameServiceProperties.get("jstUrl");
		if (StringUtil.isEmpty(jstUrl)) {
			return Results.failure("配置中心无聚石塔配置路径!");
		}

		String sessionUuid = vo.getSessionUuid();
		String activityPlanId = vo.getActivityPlanId();
		String channelId = vo.getChannelId();
		String machineId = vo.getMachineId();

		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
		if (userSessionVo == null) {
			return Results.failure("登录失效!");
		}

		Inno72Machine inno72Machine = inno72MachineMapper.findMachineByCode(machineId);
		if (inno72Machine == null) {
			return Results.failure("机器信息错误!");
		}
		String _machineId = inno72Machine.getId();

		// 查奖池ID
		Map<String, String> selectCouponParam = new HashMap<>();
		selectCouponParam.put("activityPlanId", activityPlanId);
		selectCouponParam.put("report", report);
		Inno72Coupon inno72Coupon = inno72CouponMapper.selectCouponCodeByParam(selectCouponParam);
		if (inno72Coupon == null) {
			return Results.failure("没有有效的奖券了!");
		}

		// 查商户CODE
		String shopId = inno72MerchantMapper.selectShopCodeByPlanId(activityPlanId);
		if (StringUtil.isEmpty(shopId)) {
			return Results.failure("商户好像出了点问题!");
		}

		String accessToken = userSessionVo.getAccessToken();

		Map<String, String> requestForm = new HashMap<>();

		requestForm.put("accessToken", accessToken);
		requestForm.put("ua", ua); // 安全ua
		requestForm.put("umid", umid);// umid
		requestForm.put("interactId", inno72Coupon.getCode());// 互动实例ID
		requestForm.put("shopId", shopId);// 店铺ID

		String requestUrl = jstUrl + "/api/top/lottory";

		LOGGER.info("请求聚石塔 url ===> {} , 参数 ===> {}", requestUrl, JSON.toJSONString(requestForm));

		String respJson = HttpClient.form(requestUrl, requestForm, null);

		if (StringUtil.isEmpty(respJson)) {
			return Results.failure("聚石塔无返回数据!");
		}

		LOGGER.info("调用聚石塔接口  【抽奖】返回 ===> {}", JSON.toJSONString(respJson));
		// TODO 奖券下单
		String orderId = "";
		try {
			orderId = this.genInno72Order(channelId, activityPlanId, _machineId, inno72Coupon.getId(), userSessionVo.getUserId(),
					Inno72Order.INNO72ORDER_GOODSTYPE.COUPON);
		} catch (Exception e) {
			LOGGER.info("获取优惠券下单失败 ==> {}", e.getMessage(), e);
			return Results.failure("下单失败!");
		}

		try {

			boolean msg_code = Boolean.parseBoolean(FastJsonUtils.getString(respJson, "succ"));
			if (!msg_code) {
				String msg_info = FastJsonUtils.getString(respJson, "sub_msg");
				LOGGER.info("抽奖失败 ===> {}", msg_info);
				return Results.failure(msg_info);
			}

			String is_success = FastJsonUtils.getString(respJson, "is_success");
			if (is_success.equals("true")) {
				Inno72Order inno72Order = new Inno72Order();
				inno72Order.setId(orderId);
				inno72Order.setGoodsStatus(Inno72Order.INNO72ORDER_GOODSSTATUS.SUCC.getKey());
				inno72Order.setPayTime(LocalDateTime.now());
				inno72OrderMapper.updateByPrimaryKeySelective(inno72Order);
				inno72OrderHistoryMapper.insert(new Inno72OrderHistory(inno72Order.getId(), inno72Order.getOrderNum(),
						JSON.toJSONString(inno72Order), "修改状态为已发放优惠券"));
			}
			String data = FastJsonUtils.getString(respJson, "data");
			LOGGER.info("结果数据 ====> {}", data);
			JSONObject parseDataObject = JSON.parseObject(data);

			return Results.success(mapToUpperCase(parseDataObject));

		} catch (Exception e) {
			return Results.failure(e.getMessage());
		}
	}


	/**
	 * 出货减货接口
	 *
	 * @param vo
	 * 	machineId 机器id
	 *  gameId    游戏id
	 *  goodsId   商品ID
	 * @return String
	 */
	@Override
	public Result<String> shipmentReport(MachineApiVo vo) {

		String machineCode = vo.getMachineId();
		String channelId = vo.getChannelId();
		String sessionUuid = vo.getSessionUuid();
		String orderId = vo.getOrderId();

		if (StringUtil.isEmpty(machineCode) || StringUtil.isEmpty(channelId) || StringUtil.isEmpty(sessionUuid)) {
			return Results.failure("参数缺失！");
		}

		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
		if (userSessionVo == null) {
			return Results.failure("登录失效!");
		}

		Inno72Machine machineByCode = inno72MachineMapper.findMachineByCode(machineCode);

		if (machineByCode == null) {
			LOGGER.info("机器不存在! ===> {}", JSON.toJSONString(vo));
		}

		assert machineByCode != null;
		String machineId = machineByCode.getId();
		int i = inno72SupplyChannelMapper.subCount(new Inno72SupplyChannel(machineId, null, channelId));

		LOGGER.info("减货 参数 ===》 【machineId=>{}，channelId=>{}】;结果 ==> {}", machineId, channelId, i);

		if (StringUtil.isNotEmpty(orderId)) {
			new Thread(new DeliveryRecord(orderId, orderId, machineCode, channelId, userSessionVo)).start();
		} else {
			LOGGER.info("调用出货无orderId 请求参数=>{}", JSON.toJSONString(vo));
		}
		return Results.success();
	}

	class DeliveryRecord implements Runnable {

		private String orderId;
		private String mid;
		private String channelId;
		private UserSessionVo userSessionVo;
		private String machineCode;

		public DeliveryRecord(String orderId, String mid, String channelId, String machineCode,
				UserSessionVo userSessionVo) {
			this.orderId = orderId;
			this.mid = mid;
			this.channelId = channelId;
			this.machineCode = machineCode;
			this.userSessionVo = userSessionVo;
		}

		@Override
		public void run() {
			Map<String, String> requestForm = new HashMap<>();

			requestForm.put("accessToken", userSessionVo.getAccessToken());
			requestForm.put("orderId", orderId); // 安全ua
			requestForm.put("mid", machineCode);// umid 实际为code
			requestForm.put("channelId", channelId);// 互动实例ID

			String respJson = "";
			try {
				respJson = HttpClient.form(inno72GameServiceProperties.get("jstUrl") + "/api/top/deliveryRecord",
						requestForm, null);
			} catch (Exception e) {
				LOGGER.info("");
			}
			if (StringUtil.isEmpty(respJson)) {
				LOGGER.info("聚石塔无返回数据!");
			}

			LOGGER.info("调用聚石塔接口  【通知出货】返回 ===> {}", JSON.toJSONString(respJson));

			String msg_code = FastJsonUtils.getString(respJson, "msg_code");
			if (!msg_code.equals("SUCCESS")) {
				String msg_info = FastJsonUtils.getString(respJson, "msg_info");
				LOGGER.info("返回非成功状态，不能更细订单状态为成功 {}", respJson);
			}

			inno72GameService.updateOrderReport(userSessionVo);
		}
	}

	/**
	 * 等位二维码回执
	 *
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

		// 判断是否有他人登录以及二维码是否过期
		String qrStatus = QRSTATUS_NORMAL;
		if (!StringUtil.isEmpty(sessionUuid)) {
			LOGGER.info("sessionUuid is {}", sessionUuid);
			// 判断二维码是否过期
			boolean result = gameSessionRedisUtil.hasKey(sessionUuid);
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

		Inno72Machine inno72Machine = inno72MachineMapper.selectByPrimaryKey(mid);
		if (inno72Machine == null){
			return Results.failure("机器错误！");
		}

		List<Inno72ActivityPlan> inno72ActivityPlans = inno72ActivityPlanMapper.selectByMachineId(mid);

		String gameId = "";
		String playCode;

		Inno72ActivityPlan inno72ActivityPlan = null;
		if (inno72ActivityPlans.size() > 0) {
			inno72ActivityPlan = inno72ActivityPlans.get(0);
			gameId = inno72ActivityPlan.getGameId();
		}

		// 设置统计每个计划的已完次数
		assert inno72ActivityPlan != null;
		redisUtil.sadd(CommonBean.REDIS_ACTIVITY_PLAN_LOGIN_TIMES_KEY + inno72ActivityPlan.getId(), userId);

		if (StringUtil.isEmpty(gameId)) {
			return Results.failure("没有绑定的游戏！");
		}

		Inno72Game inno72Game = inno72GameMapper.selectByPrimaryKey(gameId);
		if (inno72Game == null) {
			return Results.failure("不存在的游戏！");
		}


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
		 * <tmall_fans_automachine_getmaskusernick_response> <msg_code>200</msg_code>
		 * <msg_info>用户不存在</msg_info>1dd77fc18f3a409196de23baedcf8ce1 <model>e****丫</model>
		 * </tmall_fans_automachine_getmaskusernick_response>
		 */
		String respJson = HttpClient.form(jstUrl + "/api/top/getMaskUserNick", requestForm, null);
		LOGGER.info("调用聚石塔接口  【请求nickName】返回 ===> {}", JSON.toJSONString(respJson));

		if (StringUtil.isEmpty(respJson)) {
			return Results.failure("请求用户名失败!");
		}

		String nickName = FastJsonUtils.getString(respJson, "model");

		String channelId = inno72Merchant.getChannelId();

		Inno72Channel inno72Channel = inno72ChannelMapper.selectByPrimaryKey(channelId);

		Map<String, String> userChannelParams = new HashMap<>();
		userChannelParams.put("channelId", channelId);
		userChannelParams.put("channelUserKey", userId);
		Inno72GameUserChannel userChannel = inno72GameUserChannelMapper.selectByChannelUserKey(userChannelParams);

		if (userChannel == null) {
			Inno72GameUser inno72GameUser = new Inno72GameUser();
			inno72GameUserMapper.insert(inno72GameUser);
			LOGGER.info("插入游戏用户表 完成 ===> {}", JSON.toJSONString(inno72GameUser));
			userChannel = new Inno72GameUserChannel(nickName, "", channelId, inno72GameUser.getId(),
					inno72Channel.getChannelName(), userId);
			inno72GameUserChannelMapper.insert(userChannel);
			LOGGER.info("插入游戏用户渠道表 完成 ===> {}", JSON.toJSONString(userChannel));

		}
		//TODO 判断机器是否有商品
		Map<String, String> params = new HashMap<>(2);
		params.put("platId",inno72ActivityPlan.getId());
		params.put("machineId",inno72Machine.getId());
		List<Integer> countGoods = inno72ActivityPlanGameResultMapper.selectCountGoods(params);
		boolean goodsCount = true;
		for ( Integer count : countGoods ){
			if (count == 0){
				goodsCount = false;
				break;
			}
		}

		UserSessionVo sessionVo = new UserSessionVo(mid, nickName, userId, access_token, gameId, sessionUuid,
				inno72ActivityPlan.getId());
		boolean b = inno72GameService.countSuccOrder(channelId, userId, inno72ActivityPlan.getId());
		sessionVo.setCanOrder(b);
		sessionVo.setCountGoods(goodsCount);
		sessionVo.setChannelId(channelId);
		sessionVo.setMachineId(mid);
		sessionVo.setMachineCode(mid);
		sessionVo.setActivityId(inno72Activity.getId());
		gameSessionRedisUtil.setSessionEx(sessionUuid, JSON.toJSONString(sessionVo));


		this.startGameLife(userChannel, inno72Activity, inno72ActivityPlan, inno72Game, inno72Machine, userId);

		LOGGER.info("playCode is" + playCode);

		// 调用聚石塔日志
		Map<String, String> requestLogForm = new HashMap<>();

		requestLogForm.put("accessToken", sessionVo.getAccessToken());
		requestLogForm.put("value1", inno72Machine.getMachineCode());
		requestLogForm.put("sellerId", inno72Merchant.getMerchantCode());
		requestLogForm.put("userId", "-1");
		requestLogForm.put("type", "login");
		requestLogForm.put("bizCode", "automachine");
		// LogReqrest logReqrest = getLogReqrest("automachine", null, 0L, "login", -1L,
		// inno72Machine.getMachineCode(), null, null, null);
		// requestLogForm.put("logReqrest", JSON.toJSONString(logReqrest));

		LOGGER.info("聚石塔日志接口参数 requestLogForm ：" + JSONObject.toJSONString(requestLogForm));
		String result = HttpClient.form(jstUrl + "/api/top/addLog", requestLogForm, null);
		LOGGER.info("聚石塔日志接口返回 ", JSON.toJSONString(result));
		String msg_logCode = FastJsonUtils.getString(result, "msg_code");
		if (!"SUCCESS".equals(msg_logCode)) {
			String msg_info = FastJsonUtils.getString(result, "msg_info");
			LOGGER.info("调用聚石塔日志接口 ===> {}", JSON.toJSONString(msg_info));
		}

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("playCode", playCode);
		resultMap.put("qrStatus", qrStatus);
		resultMap.put("sellerId", inno72Merchant.getMerchantCode());

		return Results.success(JSONObject.toJSONString(resultMap));
	}

	@Override
	public Result<String> malfunctionLog(String machineId, String channelCode) {

		Inno72Machine inno72Machine = inno72MachineMapper.selectByPrimaryKey(machineId);
		// 将货道故障信息推送到预警系统
		AlarmMessageBean<String> alarmMessageBean = new AlarmMessageBean<>();
		alarmMessageBean.setSystem("machineChannel");
		alarmMessageBean.setType("machineChannelException");
		alarmMessageBean.setData(inno72Machine.getMachineCode());
		Map<String, Object> map = new HashMap<>();
		map.put("channelCode", channelCode);
		map.put("alarmMessage", alarmMessageBean);
		redisUtil.publish("moniterAlarm", JSONObject.toJSONString(map));

		return Results.success();
	}

	/**
	 * 掉货失败
	 * @param machineId
	 * @param channelCode
	 * @param describtion
	 * @return
	 */
	@Override
	public Result<String> shipmentFail(String machineId, String channelCode, String describtion) {

		AlarmMessageBean<Object> alarmMessageBean = new AlarmMessageBean<>();
		MachineDropGoodsBean machineDropGoodsBean = new MachineDropGoodsBean();
		LOGGER.info("掉货失败参数 => machineId:{}; channelCode:{}; describtion:{}", machineId, channelCode, describtion);
		Inno72Machine inno72Machine = inno72MachineMapper.findMachineByCode(machineId);
		if (inno72Machine == null) {
			return Results.failure("机器号错误");
		}
		if (inno72Machine.getMachineStatus() == 4 && inno72Machine.getOpenStatus() == 0) {
			machineDropGoodsBean.setMachineCode(machineId); // 实际为code
			machineDropGoodsBean.setChannelNum(channelCode);
			alarmMessageBean.setSystem("machineDropGoods");
			alarmMessageBean.setType("machineDropGoodsException");
			alarmMessageBean.setData(machineDropGoodsBean);
			redisUtil.publish("moniterAlarm", JSONObject.toJSONString(alarmMessageBean));
		}

		return Results.success();
	}

	/**
	 * 生成本次游戏记录
	 *
	 * @param userChannel
	 * @param inno72Activity
	 * @param inno72ActivityPlan
	 * @param inno72Game
	 * @param inno72Machine
	 * @param userId
	 */
	private void startGameLife(Inno72GameUserChannel userChannel, Inno72Activity inno72Activity,
			Inno72ActivityPlan inno72ActivityPlan, Inno72Game inno72Game, Inno72Machine inno72Machine, String userId) {
		Inno72Locale inno72Locale = inno72LocaleMapper.selectByPrimaryKey(inno72Machine.getLocaleId());
		Inno72GameUserLife life = new Inno72GameUserLife(userChannel.getGameUserId(), userChannel.getId(),
				inno72Machine.getMachineCode(), userChannel.getUserNick(), inno72ActivityPlan.getActivityId(),
				inno72Activity.getName(), inno72ActivityPlan.getId(), inno72Game.getId(), inno72Game.getName(),
				inno72Machine.getLocaleId(), inno72Locale == null ? "" : inno72Locale.getMall(), null, "", null, null,
				userId);
		LOGGER.info("插入用户游戏记录 ===> {}", JSON.toJSONString(life));
		inno72GameUserLifeMapper.insert(life);

	}

	/**
	 * 下inno72订单，优惠券 or 商品
	 * @param channelId
	 * @param activityPlanId
	 * @param machineId
	 * @param goodsId
	 * @param channelUserKey
	 * @param product
	 * @return
	 */
	private String genInno72Order(String channelId, String activityPlanId, String machineId, String goodsId,
			String channelUserKey, Inno72Order.INNO72ORDER_GOODSTYPE product) {

		Map<String, String> paramsChannel = new HashMap<>();
		paramsChannel.put("channelId", channelId);
		paramsChannel.put("channelUserKey", channelUserKey);
		Inno72GameUserChannel userChannel = inno72GameUserChannelMapper.selectByChannelUserKey(paramsChannel);
		String gameUserId = userChannel.getGameUserId();

		// 活动计划
		Inno72ActivityPlan inno72ActivityPlan = inno72ActivityPlanMapper.selectByPrimaryKey(activityPlanId);
		// 活动
		Inno72Activity inno72Activity = inno72ActivityMapper.selectByPrimaryKey(inno72ActivityPlan.getActivityId());

		Inno72Machine inno72Machine = inno72MachineMapper.selectByPrimaryKey(machineId);

		Inno72Channel inno72Channel = inno72ChannelMapper.selectByPrimaryKey(channelId);

		Inno72Shops inno72Shops = inno72ShopsMapper.selectByPrimaryKey(inno72Activity.getShopId());

		String orderNum = Inno72OrderNumGenUtil.genOrderNum(inno72Channel.getChannelCode(),
				inno72Machine.getMachineCode());
		LocalDateTime now = LocalDateTime.now();

		boolean b = inno72GameService.countSuccOrder(channelId, channelUserKey, activityPlanId);
		Integer rep = null;
		if (product.getKey().equals(Inno72Order.INNO72ORDER_GOODSTYPE.PRODUCT.getKey())) {
			rep = Inno72Order.INNO72ORDER_REPETITION.NOT.getKey();
		} else {
			rep = b ? Inno72Order.INNO72ORDER_REPETITION.NOT.getKey()
					: Inno72Order.INNO72ORDER_REPETITION.REPETITION.getKey();
		}

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
		inno72Order.setGoodsType(product.getKey());
		inno72Order.setRepetition(rep);
		inno72Order.setShopsId(inno72Shops.getId());
		inno72Order.setShopsName(inno72Shops.getShopName());
		inno72Order.setUserId(gameUserId);
		inno72OrderMapper.insert(inno72Order);

		Inno72OrderGoods orderGoods = new Inno72OrderGoods();
		if (product.getKey().equals(Inno72Order.INNO72ORDER_GOODSTYPE.PRODUCT.getKey())) {
			Inno72Goods inno72Goods = inno72GoodsMapper.selectByCode(goodsId);
			orderGoods.setGoodsCode(inno72Goods.getCode());
			orderGoods.setGoodsId(inno72Goods.getId());
			orderGoods.setGoodsName(inno72Goods.getName());
			orderGoods.setGoodsPrice(inno72Goods.getPrice());
		} else {
			Inno72Coupon inno72Coupon = inno72CouponMapper.selectByPrimaryKey(goodsId);
			orderGoods.setGoodsCode(inno72Coupon.getCode());
			orderGoods.setGoodsId(inno72Coupon.getId());
			orderGoods.setGoodsName(inno72Coupon.getName());
			orderGoods.setGoodsPrice(BigDecimal.ZERO);
		}

		orderGoods.setOrderId(inno72Order.getId());
		orderGoods.setOrderNum(inno72Order.getOrderNum());
		orderGoods.setStatus(Inno72Order.INNO72ORDER_GOODSSTATUS.WAIT.getKey());
		inno72OrderGoodsMapper.insert(orderGoods);

		inno72OrderHistoryMapper.insert(new Inno72OrderHistory(inno72Order.getId(), inno72Order.getOrderNum(),
				JSON.toJSONString(inno72Order), "初始化插入订单!"));

		return rep == 0 ? rep + "" : inno72Order.getId();
	}

	/**
	 * 下划线转驼峰
	 *
	 * @param jObject
	 * @return
	 */
	private Map<String, Object> mapToUpperCase(JSONObject jObject) {
		Map<String, Object> result = new HashMap<>();
		if (jObject != null) {
			for (Map.Entry<String, Object> element : jObject.entrySet()) {
				result.put(StringUtil.strToUpperCase(element.getKey()), element.getValue());
			}
		}
		return result;
	}


	/**
	 * 拼装日志对象
	 * @param bizCode
	 * @param itemId
	 * @param sellerId
	 * @param type
	 * @param userId
	 * @param value1
	 * @param value2
	 * @param value3
	 * @param value4
	 * @return
	 */
	private LogReqrest getLogReqrest(String bizCode, Long itemId, Long sellerId, String type, Long userId,
			String value1, String value2, Long value3, Long value4) {
		return new LogReqrest(bizCode, itemId, sellerId, type, -1L, value1, value2, value3, value4);
	}

	/**
	 * 淘宝日志接口
	 * @param token
	 * @param itemId
	 * @param sellerId
	 * @param userId
	 * @param machineCode
	 * @param playTime
	 * @return
	 */
	@Override
	public Result<String> userDuration(String token, String itemId, String sellerId, String userId, String machineCode,
			String playTime) {
		LOGGER.info("用户互动时长 => itemId:{}; sellerId:{}; userId:{}; token:{}", itemId, sellerId, userId, token);
		// 调用聚石塔日志
		Map<String, String> requestLogForm = new HashMap<>();
		requestLogForm.put("accessToken", token);
		LogReqrest logReqrest = getLogReqrest("", Long.valueOf(itemId), Long.valueOf(sellerId), "用户互动时常",
				Long.valueOf(userId), machineCode, playTime, null, null);
		requestLogForm.put("logReqrest", JSON.toJSONString(logReqrest));
		LOGGER.info("聚石塔日志接口参数 requestLogForm ：" + JSONObject.toJSONString(requestLogForm));

		String jstUrl = inno72GameServiceProperties.get("jstUrl");
		if (StringUtil.isEmpty(jstUrl)) {
			return Results.failure("配置中心无聚石塔配置路径!");
		}
		String result = HttpClient.form(jstUrl + "/api/top/addLog", requestLogForm, null);
		LOGGER.info("聚石塔日志接口返回 ", JSON.toJSONString(result));
		String msg_logCode = FastJsonUtils.getString(result, "msg_code");
		System.out.println("++++++++++++++++++" + msg_logCode);
		String msg_info = FastJsonUtils.getString(result, "msg_info");
		if (!"SUCCESS".equals(msg_logCode)) {
			LOGGER.info("调用聚石塔日志接口 ===> {}", JSON.toJSONString(msg_info));
			return Results.failure(msg_info);
		}
		return Results.success(msg_info);
	}

	/**
	 * 调用聚石塔淘宝下单内部类
	 */
	public class SendOrder implements Runnable {

		private String activityId;
		private String mid;
		private String sessionUuid;
		private UserSessionVo userSessionVo;
		private String inno72OrderId;
		private String itemId;

		SendOrder(String inno72OrderId, String itemId, String activityId, String mid, String sessionUuid,
				UserSessionVo userSessionVo) {
			this.activityId = activityId;
			this.mid = mid;
			this.sessionUuid = sessionUuid;
			this.userSessionVo = userSessionVo;
			this.inno72OrderId = inno72OrderId;
			this.itemId = itemId;
		}

		@Override
		public void run() {

			Map<String, String> requestForm = new HashMap<>();
			requestForm.put("accessToken", userSessionVo.getAccessToken());
			requestForm.put("activityId", activityId);
			requestForm.put("mid", mid); // 实际为code
			requestForm.put("goodsId", itemId);

			String respJson;
			try {
				LOGGER.info("调用聚石塔下单接口 参数 ======》 {}", JSON.toJSONString(requestForm));
				respJson = HttpClient.form(inno72GameServiceProperties.get("jstUrl") + "/api/top/order", requestForm,
						null);
			} catch (Exception e) {
				LOGGER.info("调用聚石塔失败 ! {}", e.getMessage(), e);
				return;
			}

			if (StringUtil.isEmpty(respJson)) {
				LOGGER.info("聚石塔无返回数据 ! {}");
			}

			LOGGER.info("调用聚石塔接口  【下单】返回 ===> {}", respJson);


			try {

				/*
				 * { "tmall_fans_automachine_order_createorderbyitemid_response": { "result": { "model": { "actual_fee":
				 * 1, "order_id": "185028768691768199", "pay_qrcode_image":
				 * "https:\/\/img.alicdn.com\/tfscom\/TB1lElXE9tYBeNjSspkwu2U8VXa.png" }, "msg_code": "SUCCESS" },
				 * "request_id": "43ecpzeb5fdn" } }
				 */

				String msg_code = FastJsonUtils.getString(respJson, "msg_code");
				if (!msg_code.equals("SUCCESS")) {
					LOGGER.info("调用聚石塔下单失败 ! {}", respJson);
					return;
				}

				// 更新第三方订单号进inno72 order
				// 更新订单

				String refOrderId = FastJsonUtils.getString(respJson, "order_id");
				Result<String> stringResult = inno72GameService.updateRefOrderId(inno72OrderId, refOrderId,
						userSessionVo.getUserId());
				LOGGER.info("更新第三方订单号 => {}", JSON.toJSONString(stringResult));

				userSessionVo.setRefOrderId(refOrderId);
				userSessionVo.setInno72OrderId(inno72OrderId);
				gameSessionRedisUtil.setSessionEx(sessionUuid, JSON.toJSONString(userSessionVo));

			} catch (Exception e) {

				LOGGER.error("解析聚石塔返回数据异常! ===>  {}", e.getMessage(), e);

			}


		}
	}

	@Override
	public Result<List<Inno72SamplingGoods>> getSampling(String machineCode) {
		LOGGER.info("获取派样商品 => machineCode:{}", machineCode);
		if (StringUtil.isEmpty(machineCode)) {
			return Results.failure("machineCode为空!");
		}
		List<Inno72SamplingGoods> inno72SamplingGoodsList = inno72GoodsMapper.selectSamplingGoods(machineCode);
		LOGGER.info("返回派样商品列表 => list:{}", inno72SamplingGoodsList);
		return Results.success(inno72SamplingGoodsList);
	}
}