package com.inno72.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.json.JsonUtil;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.common.util.Inno72OrderNumGenUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.*;
import com.inno72.model.*;
import com.inno72.plugin.http.HttpClient;
import com.inno72.service.Inno72GameService;
import com.inno72.service.Inno72OrderService;
import com.inno72.service.Inno72TopService;
import com.inno72.vo.GoodsVo;
import com.inno72.vo.MachineApiVo;
import com.inno72.vo.UserSessionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class Inno72OrderServiceImpl implements Inno72OrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72OrderServiceImpl.class);

	@Resource
	private Inno72GameServiceProperties inno72GameServiceProperties;
	@Resource
	private Inno72OrderMapper inno72OrderMapper;
	@Resource
	private GameSessionRedisUtil gameSessionRedisUtil;
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
	private Inno72SupplyChannelMapper inno72SupplyChannelMapper;
	@Resource
	private Inno72CouponMapper inno72CouponMapper;
	@Resource
	private Inno72GameService inno72GameService;
	@Resource
	private Inno72TopService inno72TopService;

	@Override
	public Result<Object> order(MachineApiVo vo) {

		LOGGER.info("下单参数 is {}", JsonUtil.toJson(vo));

		String sessionUuid = vo.getSessionUuid();
		if (StringUtil.isEmpty(sessionUuid)) {
			return Results.success("参数错误!");
		}

		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(vo.getSessionUuid());
		if (userSessionVo == null) {
			return Results.failure("登录失效!");
		}

		// 通过排期id 查找活动属于什么类型 如果一般类型通过 report ，派样类型通过 itemid
		List<Inno72ActivityPlanGameResult> planGameResults = this.getPlanGameResult(vo);

		if (planGameResults.size() == 0) {
			return Results.failure("无配置商品!");
		}

		LOGGER.debug("下单 userSessionVo is {}", JSON.toJSONString(userSessionVo));
		List<String> resultGoodsId = new ArrayList<>();

		int lotteryCode = 1;
		for (Inno72ActivityPlanGameResult result : planGameResults) {
			String prizeType = result.getPrizeType();
			switch (prizeType) {
				case "1":
					// 下商品订单
					String prizeId = result.getPrizeId();
					Inno72Goods inno72Goods = inno72GoodsMapper.selectByPrimaryKey(prizeId);
					if (inno72Goods == null) {
						LOGGER.debug("讲真的。配置商品..它不见了。。。! 不信你看配置 ==> {}", JSON.toJSONString(result));
						return Results.failure("讲真的。配置商品..它不见了。。。!");
					}
					String code = inno72Goods.getCode();
					Result<String> orderResult = this.sendOrder(userSessionVo, code);

					resultGoodsId.add(result.getPrizeId());

					break;
				case "2":
					// 下优惠券订单
					Result<Object> lottery = this.lottery(userSessionVo, vo.getUa(), vo.getUmid(), result.getPrizeId());
					LOGGER.debug("抽取奖券 结果 ==> {}", JSON.toJSONString(lottery));
					lotteryCode = lottery.getCode();
					LOGGER.info("lotteryCode is {} ", lottery.getCode());
					break;
				default:
					return Results.failure("无商品类型");
			}
		}

		Map<String, Object> result = new HashMap<>(2);
		if (resultGoodsId.size() > 0) {
			// 请求接口 获取出货 货道号
			Map<String, Object> map = new HashMap<>();
			map.put("machineId", userSessionVo.getMachineId());
			map.put("goodsCodes", resultGoodsId);
			List<Inno72SupplyChannel> inno72SupplyChannels = inno72SupplyChannelMapper.selectListByParam(map);

			LOGGER.info("查询 货道号 结果 ==> {}", JSON.toJSONString(inno72SupplyChannels));

			if (inno72SupplyChannels.size() == 0) {
				return Results.failure("没有商品!");
			}

			Map<String, GoodsVo> goodsVoMap = new HashMap<>();

			channelSort(inno72SupplyChannels);

			LOGGER.info("inno72SupplyChannels sort is {} ", JsonUtil.toJson(inno72SupplyChannels));

			for (Inno72SupplyChannel inno72SupplyChannel : inno72SupplyChannels) {

				Integer isDel = inno72SupplyChannel.getIsDelete();
				if (isDel != 0) {
					LOGGER.info("paiYangOrder channel is {} , isDel is {} ", inno72SupplyChannel.getCode(), isDel);
					continue;
				}

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

			Collection<GoodsVo> values = goodsVoMap.values();
			result.put("goods", values);
		}

		result.put("time", new Date().getTime());
		result.put("lotteryResult", lotteryCode);
		LOGGER.info("oneKeyOrder is {}", JsonUtil.toJson(result));
		return Results.success(result);
	}

	/**
	 * 获得排期游戏结果，根据不同活动类型处理
	 * @param vo
	 * @return
	 */
	private List<Inno72ActivityPlanGameResult> getPlanGameResult(MachineApiVo vo) {
		List<Inno72ActivityPlanGameResult> planGameResults = null;

		Inno72Activity inno72Activity = inno72ActivityMapper.selectByPrimaryKey(vo.getActivityPlanId());
		Integer type = inno72Activity.getType();
		Map<String, String> params = new HashMap<>();
		String activityPlanId = vo.getActivityPlanId();

		if (Inno72Activity.ActivityType.COMMON.getType() == type) {
			String itemId = vo.getItemId();
			Inno72Goods inno72Goods = inno72GoodsMapper.selectByPrimaryKey(itemId);
			String shopId = inno72Goods.getShopId();

			params.put("goodsId", itemId);
			params.put("shopId", shopId);
			params.put("planId", activityPlanId);
			planGameResults = inno72ActivityPlanGameResultMapper.selectByParams(params);
		} else if (Inno72Activity.ActivityType.PAIYANG.getType() == type) {
			String report = vo.getReport();
			params.put("activityPlanId", activityPlanId);
			params.put("report", report);
			planGameResults = inno72ActivityPlanGameResultMapper.selectAllResultByCode(params);
		}
		return planGameResults;
	}

	/**
	 * 货道排序
	 * @param inno72SupplyChannels
	 */
	private void channelSort(List<Inno72SupplyChannel> inno72SupplyChannels) {
		Collections.sort(inno72SupplyChannels, new Comparator<Inno72SupplyChannel>() {
			@Override
			public int compare(Inno72SupplyChannel o1, Inno72SupplyChannel o2) {
				if (o1.getGoodsCount() == null)
					o1.setGoodsCount(0);
				if (o2.getGoodsCount() == null)
					o2.setGoodsCount(0);
				if (o1.getGoodsCount() > o2.getGoodsCount()) {
					return -1;
				}

				if (o1.getGoodsCount() == o2.getGoodsCount()) {
					Integer code1, code2 = 0;
					try {
						code1 = Integer.parseInt(o1.getCode());
					} catch (Exception e) {
						LOGGER.info("数据异常code={}非数字", o1.getCode());
						code1 = 0;
					}
					try {
						code2 = Integer.parseInt(o2.getCode());
					} catch (Exception e) {
						LOGGER.info("数据异常code={}非数字", o2.getCode());
						code2 = 0;
					}
					if (code1 > code2) {
						return 1;
					}
					if (code1 < code2) {
						return -1;
					}
					return 0;
				}

				return 1;
			}
		});
	}

	/**
	 * 重构合并抽奖和下单接口为一个接口。这是下单接口
	 * @param userSessionVo
	 * @param itemId
	 * @return
	 */
	private Result<String> sendOrder(UserSessionVo userSessionVo, String itemId) {

		String channelId = userSessionVo.getChannelId();
		String machineCode = userSessionVo.getMachineCode();
		String machineId = userSessionVo.getMachineId();
		String activityPlanId = userSessionVo.getActivityPlanId();
		String sessionUuid = userSessionVo.getSessionUuid();
		String jstUrl = inno72GameServiceProperties.get("jstUrl");
		String activityId = userSessionVo.getActivityId();

		LOGGER.info("商品下单 userSessionVo =》 {}", JSON.toJSONString(userSessionVo));

		Inno72Machine inno72Machine = inno72MachineMapper.selectByPrimaryKey(machineId);
		if (inno72Machine == null) {
			return Results.failure("下商品订单机器信息错误!");
		}

		// 下单 inno72_Order TODO 商品下单 itemId 对应的类型？
		String inno72OrderId = genInno72Order(channelId, activityPlanId, machineId, itemId, userSessionVo.getUserId(),
				Inno72Order.INNO72ORDER_GOODSTYPE.PRODUCT);


		String accessToken = userSessionVo.getAccessToken();
		LOGGER.info("更新的session =====> {}", JSON.toJSONString(userSessionVo));
		if (inno72OrderId.equals("0")) {
			LOGGER.info("已经超过最大游戏数量啦 QAQ!");
			return Results.failure("已经超过最大游戏数量啦 QAQ!");
		}

		Map<String, String> requestForm = new HashMap<>();
		requestForm.put("accessToken", accessToken);
		requestForm.put("activityId", activityId);
		requestForm.put("mid", machineCode); // 实际为code
		requestForm.put("goodsId", itemId);
		requestForm.put("mixNick", userSessionVo.getUserId()); // 实际为taobao_user_nick

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

			// String msg_code = FastJsonUtils.getString(respJson, "msg_code");
			// if (!msg_code.equals("SUCCESS")) {
			// String msg_info = FastJsonUtils.getString(respJson, "msg_info");
			// return Results.failure(msg_info);
			// }
			String ref_order_id = FastJsonUtils.getString(respJson, "order_id");
			userSessionVo.setInno72OrderId(inno72OrderId);
			userSessionVo.setRefOrderId(ref_order_id);
			gameSessionRedisUtil.setSession(sessionUuid, JSON.toJSONString(userSessionVo));

			// 更新第三方订单号进inno72 order
			Result<String> stringResult = inno72GameService
					.updateRefOrderId(inno72OrderId, ref_order_id, userSessionVo.getUserId());
			LOGGER.info("修改第三方订单进inno72——order 结果 {}", JSON.toJSONString(stringResult));
			return Results.success(inno72OrderId);

		} catch (Exception e) {
			LOGGER.error("解析聚石塔返回数据异常! ===>  {}", e.getMessage(), e);
			return Results.failure("解析聚石塔返回数据异常!");
		}
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

		String orderNum = Inno72OrderNumGenUtil
				.genOrderNum(inno72Channel.getChannelCode(), inno72Machine.getMachineCode());
		LocalDateTime now = LocalDateTime.now();

		boolean b = inno72GameService.countSuccOrder(channelId, channelUserKey, activityPlanId,inno72ActivityPlan.getActivityId());
		Integer rep = null;
		if (product.getKey().equals(Inno72Order.INNO72ORDER_GOODSTYPE.PRODUCT.getKey())) {
			rep = Inno72Order.INNO72ORDER_REPETITION.NOT.getKey();
		} else {
			rep = b ?
					Inno72Order.INNO72ORDER_REPETITION.NOT.getKey() :
					Inno72Order.INNO72ORDER_REPETITION.REPETITION.getKey();
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

		inno72Order.setUserId(gameUserId);

		// 根据商品类型保存 订单商品表里保存不同的数据
		Inno72OrderGoods orderGoods = new Inno72OrderGoods();
		Inno72Shops inno72Shops = null;
		if (product.getKey().equals(Inno72Order.INNO72ORDER_GOODSTYPE.PRODUCT.getKey())) {
			Inno72Goods inno72Goods = inno72GoodsMapper.selectByCode(goodsId);
			orderGoods.setGoodsCode(inno72Goods.getCode());
			orderGoods.setGoodsId(inno72Goods.getId());
			orderGoods.setGoodsName(inno72Goods.getName());
			orderGoods.setGoodsPrice(inno72Goods.getPrice());
			inno72Shops = inno72ShopsMapper.selectByPrimaryKey(inno72Goods.getShopId());
		} else {
			Inno72Coupon inno72Coupon = inno72CouponMapper.selectByPrimaryKey(goodsId);
			orderGoods.setGoodsCode(inno72Coupon.getCode());
			orderGoods.setGoodsId(inno72Coupon.getId());
			orderGoods.setGoodsName(inno72Coupon.getName());
			orderGoods.setGoodsPrice(BigDecimal.ZERO);
			inno72Shops = inno72ShopsMapper.selectByPrimaryKey(inno72Coupon.getShopsId());
		}

		inno72Order.setShopsId(inno72Shops.getId());
		inno72Order.setShopsName(inno72Shops.getShopName());

		orderGoods.setOrderId(inno72Order.getId());
		orderGoods.setOrderNum(inno72Order.getOrderNum());
		orderGoods.setStatus(Inno72Order.INNO72ORDER_GOODSSTATUS.WAIT.getKey());

		inno72OrderMapper.insert(inno72Order);
		inno72OrderGoodsMapper.insert(orderGoods);

		inno72OrderHistoryMapper.insert(new Inno72OrderHistory(inno72Order.getId(), inno72Order.getOrderNum(),
				JSON.toJSONString(inno72Order), "初始化插入订单!"));

		return rep == 0 ? rep + "" : inno72Order.getId();
	}

	/**
	 * 重构合并抽奖和下单接口为一个接口。这是抽奖接口
	 * @param vo
	 * @param ua
	 * @param umid
	 * @param prizeId
	 * @return
	 */
	private Result<Object> lottery(UserSessionVo vo, String ua, String umid, String prizeId) {

		String jstUrl = inno72GameServiceProperties.get("jstUrl");
		if (StringUtil.isEmpty(jstUrl)) {
			return Results.failure("配置中心无聚石塔配置路径!");
		}

		String sessionUuid = vo.getSessionUuid();
		String activityPlanId = vo.getActivityPlanId();
		String channelId = vo.getChannelId();
		String machineId = vo.getMachineId();

		LOGGER.info("抽奖下单 userSessionVo is {}", JSON.toJSONString(vo));

		Inno72Machine inno72Machine = inno72MachineMapper.selectByPrimaryKey(machineId);
		if (inno72Machine == null) {
			return Results.failure("下优惠券机器信息错误!");
		}
		String _machineId = inno72Machine.getId();

		// 查奖池ID
		Inno72Coupon inno72Coupon = inno72CouponMapper.selectByPrimaryKey(prizeId);
		if (inno72Coupon == null) {
			return Results.failure("没有有效的奖券了!");
		}

		// 查商户CODE
		Inno72Shops shop = inno72ShopsMapper.selectByPrimaryKey(inno72Coupon.getShopsId());
		if (shop == null || StringUtil.isEmpty(shop.getShopCode())) {
			return Results.failure("商户没找到!");
		}

		LOGGER.info("lottery shop is {}, shopsId is {}", JsonUtil.toJson(shop), inno72Coupon.getShopsId());

		Result respJsonResult = inno72TopService.lottory(sessionUuid, ua, umid, inno72Coupon.getCode(), shop.getShopCode());

		if (StringUtil.isEmpty(respJsonResult.getCode() == Result.FAILURE)) {
			return Results.failure("请求聚石塔失败!");
		}

		String respJson = (String) respJsonResult.getData();

		LOGGER.info("请求聚石塔抽奖接口返回结果 {}", respJson);

		String orderId = "";
		try {
			orderId = this.genInno72Order(channelId, activityPlanId, _machineId, inno72Coupon.getId(), vo.getUserId(),
					Inno72Order.INNO72ORDER_GOODSTYPE.COUPON);
		} catch (Exception e) {
			LOGGER.error("获取优惠券下单失败 {}", e.getMessage(), e);
			return Results.failure("获取优惠券下单失败!");
		}

		try {
			boolean msg_code = Boolean.parseBoolean(FastJsonUtils.getString(respJson, "succ"));
			LOGGER.info("获取优惠券 succ result is {}", msg_code);
			if (!msg_code) {
				String msg_info = FastJsonUtils.getString(respJson, "reason");
				LOGGER.info("获取优惠券 reason result is {}", msg_info);
				return Results.failure(msg_info);
			}

			String isWin = FastJsonUtils.getString(respJson, "is_win");

			LOGGER.info("获取优惠券 isWin is {}", isWin);

			if (isWin.equals("true")) {
				Inno72Merchant inno72Merchant = inno72MerchantMapper.selectByPrimaryKey(shop.getSellerId());

				// 记录抽奖日志
				inno72TopService.lotteryLog(sessionUuid, inno72Coupon.getCode(), inno72Merchant.getMerchantCode());

				// 更新订单出货状态为已出货，并保存订单历史
				Inno72Order inno72Order = new Inno72Order();
				inno72Order.setId(orderId);
				inno72Order.setGoodsStatus(Inno72Order.INNO72ORDER_GOODSSTATUS.SUCC.getKey());
				inno72Order.setPayTime(LocalDateTime.now());
				inno72OrderMapper.updateByPrimaryKeySelective(inno72Order);
				inno72OrderHistoryMapper.insert(new Inno72OrderHistory(inno72Order.getId(), inno72Order.getOrderNum(),
						JSON.toJSONString(inno72Order), "修改状态为已发放优惠券"));
				LOGGER.info("获取优惠券 修改状态为已发放优惠券 orderId is {}", orderId);
			} else {
				return Results.failure("抽奖失败");
			}

			String data = FastJsonUtils.getString(respJson, "data");
			LOGGER.info("获取优惠券 结果数据 is {}", data);

			JSONObject parseDataObject = JSON.parseObject(data);
			return Results.success(mapToUpperCase(parseDataObject));

		} catch (Exception e) {
			return Results.failure(e.getMessage());
		}
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
}
