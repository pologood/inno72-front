package com.inno72.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.inno72.common.CommonBean;
import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.StandardLoginTypeEnum;
import com.inno72.common.json.JsonUtil;
import com.inno72.common.util.AesUtils;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.common.util.Inno72OrderNumGenUtil;
import com.inno72.common.util.QrCodeUtil;
import com.inno72.common.util.UuidUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.feign.MachineCheckBackendFeignClient;
import com.inno72.machine.vo.SupplyRequestVo;
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
import com.inno72.model.AlarmDetailBean;
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
import com.inno72.oss.OSSUtil;
import com.inno72.plugin.http.HttpClient;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.Inno72GameApiService;
import com.inno72.service.Inno72GameService;
import com.inno72.service.Inno72TopService;
import com.inno72.util.AlarmUtil;
import com.inno72.vo.AlarmMessageBean;
import com.inno72.vo.GoodsVo;
import com.inno72.vo.Inno72SamplingGoods;
import com.inno72.vo.LogReqrest;
import com.inno72.vo.MachineApiVo;
import com.inno72.vo.StandardRedirectLoginReqVo;
import com.inno72.vo.UserSessionVo;

import net.coobird.thumbnailator.Thumbnails;

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

	@Resource
	private Inno72TopService inno72TopService;
	@Resource
	private MachineCheckBackendFeignClient machineCheckBackendFeignClient;
	@Resource
	private AlarmUtil alarmUtil;

	@Value("${machinecheckappbackend.uri}")
	private String machinecheckappbackendUri;

	@Value("${machinealarm.uri}")
	private String machinealarmUri;

	private static String FINDLOCKGOODSPUSH_URL = "/machine/channel/findLockGoodsPush";
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
	 * todo gxg 非零元单需要返回货道信息
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

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Result<Object> oneKeyOrderNologin(MachineApiVo vo) {

		String sessionUuid = vo.getSessionUuid();
		if (StringUtil.isEmpty(sessionUuid)) {
			return Results.success("参数错误!");
		}

		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(vo.getSessionUuid());
		if (userSessionVo == null) {
			return Results.failure("登录失效!");
		}

		String report = vo.getReport();
		String activityPlanId = userSessionVo.getActivityPlanId();

		Map<String, String> params = new HashMap<>();
		params.put("activityPlanId", activityPlanId);
		params.put("report", report);

		List<Inno72ActivityPlanGameResult> planGameResults = inno72ActivityPlanGameResultMapper
				.selectAllResultByCode(params);

		if (planGameResults.size() == 0) {
			return Results.failure("无配置商品!");
		}

		LOGGER.debug("下单 userSessionVo ==> {}", JSON.toJSONString(userSessionVo));
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
					localOrder(userSessionVo, code);
					resultGoodsId.add(result.getPrizeId());

					break;
				case "2":
					return Results.failure("lottery商品不支持");
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
		LOGGER.info("oneKeyOrderNologin is {}", JsonUtil.toJson(result));
		return Results.success(result);
	}

	/**
	 * 下单，商品and优惠券
	 * @param vo
	 * @return
	 */
	@Override
	public Result<Object> oneKeyOrder(MachineApiVo vo) {
		LOGGER.info("oneKeyOrder params is {}", JsonUtil.toJson(vo));

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
		String activityPlanId = userSessionVo.getActivityPlanId();

		Map<String, String> params = new HashMap<>();
		params.put("activityPlanId", activityPlanId);
		params.put("report", report);

		List<Inno72ActivityPlanGameResult> planGameResults = inno72ActivityPlanGameResultMapper
				.selectAllResultByCode(params);

		if (planGameResults.size() == 0) {
			return Results.failure("无配置商品!");
		}
		LOGGER.debug("下单 userSessionVo ==> {}", JSON.toJSONString(userSessionVo));
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
	 * 货道排序
	 * @param inno72SupplyChannels
	 */
	private void channelSort(List<Inno72SupplyChannel> inno72SupplyChannels) {
		Collections.sort(inno72SupplyChannels, new Comparator<Inno72SupplyChannel>() {
			@Override
			public int compare(Inno72SupplyChannel o1, Inno72SupplyChannel o2) {
				if (o1.getGoodsCount() == null) o1.setGoodsCount(0);
				if (o2.getGoodsCount() == null) o2.setGoodsCount(0);
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
	 * todo gxg 统一标准化接口 paiYangOrder
	 * 派样订单
	 *
	 * @param vo
	 * @return
	 */
	@Override
	public Result<Object> paiYangOrder(MachineApiVo vo) {
		String sessionUuid = vo.getSessionUuid();
		if (StringUtil.isEmpty(sessionUuid)) {
			return Results.success("参数错误!");
		}
		// 新增
		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(vo.getSessionUuid());
		if (userSessionVo == null) {
			return Results.failure("登录失效!");
		}
		String activityPlanId = userSessionVo.getActivityPlanId();
		if (StringUtil.isEmpty(userSessionVo)) {
			return Results.failure("活动排期不见了，嗯。真的!");
		}
		String itemId = vo.getItemId();
		if (StringUtil.isEmpty(itemId)) {
			return Results.failure("没有需要下单的商品!");
		}

		Inno72Goods inno72GoodsCheck = inno72GoodsMapper.selectByPrimaryKey(itemId);
		if (inno72GoodsCheck == null) {
			return Results.failure("商品不见了，嗯。真的!");
		}

		String shopId = inno72GoodsCheck.getShopId();
		if (StringUtil.isEmpty(shopId)) {
			return Results.failure("商品没有店铺配置，尴尬了!");
		}

		LOGGER.info("paiYangOrder itemId is {}, shopId is {}, activityPlanId is {}", itemId, shopId, activityPlanId);

		Map<String, String> params = new HashMap<>(3);
		params.put("goodsId", itemId);
		params.put("shopId", shopId);
		params.put("planId", activityPlanId);

		List<Inno72ActivityPlanGameResult> planGameResults = inno72ActivityPlanGameResultMapper.selectByParams(params);

		if (planGameResults.size() == 0) {
			return Results.failure("无配置商品!");
		}
		LOGGER.debug("下单 userSessionVo ==> {}", JSON.toJSONString(userSessionVo));
		List<String> resultGoodsId = new ArrayList<>();
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

					Result<Object> lottery = this.lottery(userSessionVo, vo.getUa(), vo.getUmid(), result.getPrizeId());
					LOGGER.debug("抽取奖券 结果 ==> {}", JSON.toJSONString(lottery));
					// 下优惠券订单
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
		return Results.success(result);
	}


	private Result<String> localOrder(UserSessionVo userSessionVo, String itemId) {

		String channelId = userSessionVo.getChannelId();
		String machineId = userSessionVo.getMachineId();
		String activityPlanId = userSessionVo.getActivityPlanId();

		Inno72Machine inno72Machine = inno72MachineMapper.selectByPrimaryKey(machineId);
		if (inno72Machine == null) {
			return Results.failure("下商品订单机器信息错误!");
		}
		// 下单 inno72_Order
		String inno72OrderId = this.genInno72NologinOrder(channelId, activityPlanId, machineId, itemId,
				Inno72Order.INNO72ORDER_GOODSTYPE.PRODUCT);

		Inno72GameUserLife userLife = inno72GameUserLifeMapper.selectByPrimaryKey(userSessionVo.getRefOrderId());
		if (userLife == null) {
			return Results.failure("游戏生存期信息错误");
		}
		userLife.setOrderId(inno72OrderId);
		inno72GameUserLifeMapper.updateByPrimaryKeySelective(userLife);

		return Results.success(inno72OrderId);
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
			gameSessionRedisUtil.setSessionEx(sessionUuid, JSON.toJSONString(userSessionVo));

			// 更新第三方订单号进inno72 order
			Result<String> stringResult = inno72GameService.updateRefOrderId(inno72OrderId, ref_order_id,
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
		String machineCode = vo.getMachineCode();

		LOGGER.info("抽奖下单 userSessionVo =》 {}", JSON.toJSONString(vo));

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
			return Results.failure("商户好像出了点问题!");
		}
		LOGGER.info("lottery shop is {}, shopsId is {}", JsonUtil.toJson(shop), inno72Coupon.getShopsId());

		String accessToken = vo.getAccessToken();

		Map<String, String> requestForm = new HashMap<>();

		requestForm.put("accessToken", accessToken);
		requestForm.put("ua", ua); // 安全ua
		requestForm.put("umid", umid);// umid
		requestForm.put("interactId", inno72Coupon.getCode());// 互动实例ID
		requestForm.put("shopId", shop.getShopCode());// 店铺ID

		String requestUrl = jstUrl + "/api/top/lottory";

		LOGGER.info("请求聚石塔 url ===> {} , 参数 ===> {}", requestUrl, JSON.toJSONString(requestForm));

		String respJson = HttpClient.form(requestUrl, requestForm, null);

		if (StringUtil.isEmpty(respJson)) {
			return Results.failure("聚石塔无返回数据!");
		}

		LOGGER.info("调用聚石塔接口  【抽奖】返回 ===> {}", respJson);
		// TODO 奖券下单
		String orderId = "";
		try {
			orderId = this.genInno72Order(channelId, activityPlanId, _machineId, inno72Coupon.getId(), vo.getUserId(),
					Inno72Order.INNO72ORDER_GOODSTYPE.COUPON);
		} catch (Exception e) {
			LOGGER.info("获取优惠券下单失败 ==> {}", e.getMessage(), e);
			return Results.failure("下单失败!");
		}

		try {

			boolean msg_code = Boolean.parseBoolean(FastJsonUtils.getString(respJson, "succ"));
			LOGGER.info("lottery msg_code is {}", msg_code);
			if (!msg_code) {
				String msg_info = FastJsonUtils.getString(respJson, "reason");
				LOGGER.info("抽奖失败 ===> {}", msg_info);
				return Results.failure(msg_info);
			}

			String is_success = FastJsonUtils.getString(respJson, "is_win");
			LOGGER.info("lottery is_success is {} ", is_success);
			if (is_success.equals("true")) {
				Inno72Merchant inno72Merchant = inno72MerchantMapper.selectByPrimaryKey(shop.getSellerId());
				inno72TopService.lotteryLog(sessionUuid, inno72Coupon.getCode(), inno72Merchant.getMerchantCode());

				LOGGER.info("抽奖成功 is_success is {}", is_success);
				Inno72Order inno72Order = new Inno72Order();
				inno72Order.setId(orderId);
				inno72Order.setGoodsStatus(Inno72Order.INNO72ORDER_GOODSSTATUS.SUCC.getKey());
				inno72Order.setPayTime(LocalDateTime.now());
				inno72OrderMapper.updateByPrimaryKeySelective(inno72Order);
				inno72OrderHistoryMapper.insert(new Inno72OrderHistory(inno72Order.getId(), inno72Order.getOrderNum(),
						JSON.toJSONString(inno72Order), "修改状态为已发放优惠券"));
				LOGGER.info("抽奖成功 修改状态为已发放优惠券 orderId is {}", orderId);
			} else {
				LOGGER.info("抽奖失败 is_success is {}", is_success);
				return Results.failure("抽奖失败");
			}

			String data = FastJsonUtils.getString(respJson, "data");
			LOGGER.info("结果数据 ====> {}", data);
			JSONObject parseDataObject = JSON.parseObject(data);

			return Results.success(mapToUpperCase(parseDataObject));

		} catch (Exception e) {
			return Results.failure(e.getMessage());
		}
	}

	public Result<String> shipmentReportV2(MachineApiVo vo) {
		LOGGER.info("shipmentReportV2 params vo is {} ", JsonUtil.toJson(vo));
		String machineCode = vo.getMachineId();

		if (StringUtil.isNotEmpty(vo.getChannelId())) {
			Result<String> succChannelResult = shipmentReport(vo);
			LOGGER.info("succChannelResult code is {} ", succChannelResult.getCode());
		}

		String failChannelIds = vo.getFailChannelIds();
		if (StringUtil.isNotEmpty(failChannelIds)) {

			List<String> describtion = vo.getDescribtion();
			LOGGER.info("describtion is {} ", JsonUtil.toJson(describtion));

			for (String failChannelId : failChannelIds.split(",")) {
				Result<String> failChannelResult = this.shipmentFail(machineCode, failChannelId, "");
				LOGGER.info("machineCode is {}, failChannelId is {}, code is {} ", machineCode, failChannelId,
						failChannelResult.getCode());
			}
		}
		return Results.success();
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

		if (StringUtil.isEmpty(machineCode) || StringUtil.isEmpty(channelId) || StringUtil.isEmpty(sessionUuid)) {
			return Results.failure("参数缺失！");
		}

		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
		if (userSessionVo == null) {
			return Results.failure("登录失效!");
		}
		String orderId = userSessionVo.getRefOrderId();

		Inno72Machine machineByCode = inno72MachineMapper.findMachineByCode(machineCode);

		if (machineByCode == null) {
			LOGGER.info("机器不存在! ===> {}", JSON.toJSONString(vo));
		}

		assert machineByCode != null;
		String machineId = machineByCode.getId();

		Inno72SupplyChannel inno72SupplyChannel = inno72SupplyChannelMapper
				.selectChannel(new Inno72SupplyChannel(machineId, null, channelId));
		if (inno72SupplyChannel == null) {
			return Results.failure("货道错误");
		}


		LOGGER.info("减货接口 ==> 未减货货道 [{}]", JSON.toJSONString(inno72SupplyChannel));
		Inno72SupplyChannel updateChannel = new Inno72SupplyChannel();
		updateChannel.setId(inno72SupplyChannel.getId());
		updateChannel.setUpdateTime(LocalDateTime.now());
		updateChannel.setGoodsCount(
				(inno72SupplyChannel.getGoodsCount() - 1) < 0 ? 0 : (inno72SupplyChannel.getGoodsCount() - 1));
		LOGGER.info("减货接口 ==> 要减货的货道 [{}]", JSON.toJSONString(updateChannel));
		inno72SupplyChannelMapper.updateByPrimaryKeySelective(updateChannel);

		inno72GameService.updateOrderReport(userSessionVo);
		// int i = inno72SupplyChannelMapper.subCount(new Inno72SupplyChannel(machineId, null, channelId));

		// 调整调用位置 by gxg 20180907
		try {
			findLockGoodsPush(machineId, inno72SupplyChannel.getId());
			// if(r.getCode()==1) return r;
		} catch (Exception e) {
			LOGGER.info("调用findLockGoodsPush异常", e);
		}

		// LOGGER.info("减货 参数 ===》 【machineId=>{}，channelId=>{}】;结果 ==> {}", machineId, channelId, i);
		if (StandardLoginTypeEnum.ALIBABA.getValue().equals(userSessionVo.getLoginType())) {
			if (StringUtil.isNotEmpty(orderId)) {
				new Thread(new DeliveryRecord(machineCode, channelId, userSessionVo)).run();
			} else {
				LOGGER.info("调用出货无orderId 请求参数=>{}", JSON.toJSONString(vo));
			}
		}
		return Results.success();
	}

	private Result findLockGoodsPush(String machineId, String channelId) {
		LOGGER.info("findLockGoodsPush invoked! machineId={},channelId={}", machineId, channelId);
		// 获取商品id
		String goodsId = inno72SupplyChannelMapper.findGoodsIdByChannelId(channelId);
		if (StringUtils.isEmpty(goodsId)) {
			LOGGER.info("findLockGoodsPush 根据channelId={}无法查到goodsId", channelId);
			return Results.failure("数据异常");
		}
		// 实时发送缺货报警
		LOGGER.info("实时发送缺货报警machineId={},channelId={}", machineId, channelId);
		SupplyRequestVo vo = new SupplyRequestVo();
		vo.setGoodsId(goodsId);
		vo.setMachineId(machineId);
		HttpClient.post(machinecheckappbackendUri + FINDLOCKGOODSPUSH_URL, new Gson().toJson(vo));
		// machineCheckBackendFeignClient.findLockGoodsPush(vo);
		return Results.success();
	}

	class DeliveryRecord implements Runnable {

		private String channelId;
		private UserSessionVo userSessionVo;
		private String machineCode;

		public DeliveryRecord(String channelId, String machineCode, UserSessionVo userSessionVo) {
			this.channelId = channelId;
			this.machineCode = machineCode;
			this.userSessionVo = userSessionVo;
		}

		@Override
		public void run() {
			// todo gxg 抽到聚石塔 出货
			Map<String, String> requestForm = new HashMap<>();

			requestForm.put("accessToken", userSessionVo.getAccessToken());
			requestForm.put("orderId", userSessionVo.getRefOrderId()); // 安全ua
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

		}
	}

	public String redirectLogin(StandardRedirectLoginReqVo req) {
		// 调用天猫的地址
		String url = inno72GameServiceProperties.get("tmallUrl") + req.getMachineId() + "/" + req.getMachineCode() + "/"
				+ req.getEnv() + "/?1=1" + "&bluetoothAddAes=" + req.getEnv() + "&machineCode=" + req.getMachineCode();

		LOGGER.info("返回 redirectLogin url {}", url);
		return url;
	}

	@Override
	public Result<Object> prepareLoginQrCode(String machineId, int loginType) {

		Inno72Machine inno72Machine = inno72MachineMapper.findMachineByCode(machineId);
		Map<String, Object> map = new HashMap<String, Object>();
		// 在machine库查询bluetooth地址 "6893a2ada9dd4f7eb8dc33adfc6eda73"
		String bluetoothAdd = "";
		String bluetoothAddAes = "";
		String _machineId = "";
		if (inno72Machine != null) {
			bluetoothAdd = inno72Machine.getBluetoothAddress();
			if (!StringUtil.isEmpty(bluetoothAdd)) {
				bluetoothAddAes = AesUtils.encrypt(bluetoothAdd);
			}
			_machineId = inno72Machine.getId();
		} else {
			return Results.failure(machineId + "对应的 inno72Machine 不存在");
		}
		String machineCode = "";
		if (!StringUtil.isEmpty(machineId)) {
			machineCode = AesUtils.encrypt(machineId);
		}

		LOGGER.info("Mac蓝牙地址 {} ", bluetoothAddAes);

		// 生成sessionUuid
		String sessionUuid = UuidUtil.getUUID32();
		// 获取运行环境
		String env = getActive();

		String url = String.format(
				"%s?machineId=%s&sessionUuid=%s&env=%s&bluetoothAddAes=%s&machineCode=%s&loginType=%s",
				inno72GameServiceProperties.get("qrCode"), _machineId, sessionUuid, env, bluetoothAddAes, machineCode,
				loginType);

		LOGGER.info("二维码字符串 {} ", url);
		// 二维码存储在本地的路径
		String localUrl = "pre" + _machineId + sessionUuid + ".png";

		// 存储在阿里云上的文件名
		String objectName = "qrcode/" + localUrl;
		// 提供给前端用来调用二维码的地址
		String returnUrl = inno72GameServiceProperties.get("returnUrl") + objectName;

		try {
			boolean result = QrCodeUtil.createQrCode(localUrl, url, 1800, "png");
			if (result) {
				File f = new File(localUrl);
				if (f.exists()) {
					// 压缩图片
					Thumbnails.of(localUrl).scale(0.5f).outputQuality(0f).toFile(localUrl);
					// 上传阿里云
					OSSUtil.uploadLocalFile(localUrl, objectName);
					// 删除本地文件
					f.delete();
				}

				// 设置二维码过期时间
				gameSessionRedisUtil.setSessionEx(sessionUuid, "");

				map.put("qrCodeUrl", returnUrl);
				map.put("sessionUuid", sessionUuid);
				// LOGGER.info("二维码生成成功 - result -> {}", JSON.toJSONString(map).replace("\"",
				// "'"));
				LOGGER.info("二维码生成成功 - result -> {}", JsonUtil.toJson(map));
			} else {
				LOGGER.info("二维码生成失败");
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return Results.success(map);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Result<Object> prepareLoginNologin(String machineCode) {
		
		LOGGER.info("prepareLoginNologin {}", machineCode);
		
		if (StringUtils.isBlank(machineCode)) {
			return Results.failure("machineCode 参数缺失！");
		}

		Inno72Machine inno72Machine = inno72MachineMapper.findMachineByCode(machineCode);
		if (inno72Machine == null) {
			return Results.failure("机器错误！");
		}

		String machineId = inno72Machine.getId();
		List<Inno72ActivityPlan> inno72ActivityPlans = inno72ActivityPlanMapper.selectByMachineId(machineId);

		String gameId = "";
		String playCode;

		Inno72ActivityPlan inno72ActivityPlan = null;
		if (inno72ActivityPlans.size() > 0) {
			inno72ActivityPlan = inno72ActivityPlans.get(0);
			gameId = inno72ActivityPlan.getGameId();
		}

		if (StringUtil.isEmpty(gameId)) {
			return Results.failure("没有绑定的游戏！");
		}

		if (inno72ActivityPlan == null) {
			return Results.failure("当前没有活动排期！");
		}

		Inno72Game inno72Game = inno72GameMapper.selectByPrimaryKey(gameId);
		if (inno72Game == null) {
			return Results.failure("不存在的游戏！");
		}

		String sessionUuid = UuidUtil.getUUID32();

		// 设置统计每个计划的已完次数
		redisUtil.sadd(CommonBean.REDIS_ACTIVITY_PLAN_LOGIN_TIMES_KEY + inno72ActivityPlan.getId(),
				"nologin" + sessionUuid);

		Inno72Activity inno72Activity = inno72ActivityMapper.selectByPrimaryKey(inno72ActivityPlan.getActivityId());
		if (inno72Activity == null) {
			return Results.failure("你存在此活动");
		}

		String sellerId = inno72Activity.getSellerId();
		playCode = inno72Activity.getCode();
		LOGGER.info("sessionRedirect layCode is {}", playCode);

		Inno72Merchant inno72Merchant = inno72MerchantMapper.selectByPrimaryKey(sellerId);
		if (inno72Merchant == null) {
			return Results.failure("供应商不存在");
		}
		String channelId = inno72Merchant.getChannelId();

		Map<String, String> params = new HashMap<>(2);
		params.put("platId", inno72ActivityPlan.getId());
		params.put("machineId", inno72Machine.getId());
		List<Integer> countGoods = inno72ActivityPlanGameResultMapper.selectCountGoods(params);
		boolean goodsCount = true;
		if (countGoods.size() == 0) {
			goodsCount = false;
		}
		for (Integer count : countGoods) {
			if (count < 1) {
				goodsCount = false;
				break;
			}
		}

		Inno72GameUserLife life = this.startGameLife(null, inno72Activity, inno72ActivityPlan, inno72Game,
				inno72Machine, null);

		UserSessionVo sessionVo = new UserSessionVo(machineId, null, null, null, gameId, sessionUuid,
				inno72ActivityPlan.getId());

		boolean canOrder = inno72GameService.countSuccOrderNologin(channelId, inno72ActivityPlan.getId());
		sessionVo.setCanOrder(canOrder);
		sessionVo.setCountGoods(goodsCount);
		sessionVo.setChannelId(channelId);
		sessionVo.setMachineId(machineId);
		sessionVo.setMachineCode(inno72Machine.getMachineCode());
		sessionVo.setActivityId(inno72Activity.getId());
		sessionVo.setLoginType(StandardLoginTypeEnum.NOLOGIN.getValue());

		List<GoodsVo> list = loadGameInfo(machineId);
		LOGGER.info("loadGameInfo is {} ", JsonUtil.toJson(list));
		sessionVo.setGoodsList(list);
		sessionVo.setRefOrderId(life.getId());

		gameSessionRedisUtil.setSessionEx(sessionUuid, JSON.toJSONString(sessionVo));


		LOGGER.info("prepareLoginNologin output {} {}", sessionUuid, inno72Merchant.getMerchantCode());

		Map<String, Object> resultMap = new HashMap<>();

		resultMap.put("sessionUuid", sessionUuid);
		resultMap.put("sellerId", inno72Merchant.getMerchantCode());

		return Results.success(resultMap);

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
	public Result<String> sessionRedirect(String sessionUuid, String mid, String token, String code, String userId,
			String itemId) {
		LOGGER.info("session 回执请求 => sessionUuid:{}; mid:{}; token:{}; code:{}; userId:{}; itemId:{}", sessionUuid, mid,
				token, code, userId, itemId);

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
		if (inno72Machine == null) {
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
		// assert inno72ActivityPlan != null;
		// redisUtil.sadd(CommonBean.REDIS_ACTIVITY_PLAN_LOGIN_TIMES_KEY + inno72ActivityPlan.getId(), userId);
		if (inno72ActivityPlan == null) {
			return Results.failure("当前没有活动排期！");
		}
		// 设置统计每个计划的已完次数
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
		requestForm.put("mid", inno72Machine.getMachineCode());
		requestForm.put("sellerId", inno72Merchant.getMerchantCode());
		requestForm.put("mixNick", userId); // 实际为taobao_user_nick
		/*
		 * <tmall_fans_automachine_getmaskusernick_response> <msg_code>200</msg_code>
		 * <msg_info>用户不存在</msg_info>1dd77fc18f3a409196de23baedcf8ce1 <model>e****丫</model>
		 * </tmall_fans_automachine_getmaskusernick_response>
		 */
		LOGGER.info("getMaskUserNick params is {}", JsonUtil.toJson(requestForm));
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
		// TODO 判断机器是否有商品
		Map<String, String> params = new HashMap<>(2);
		params.put("platId", inno72ActivityPlan.getId());
		params.put("machineId", inno72Machine.getId());
		List<Integer> countGoods = inno72ActivityPlanGameResultMapper.selectCountGoods(params);
		boolean goodsCount = true;
		if (countGoods.size() == 0) {
			goodsCount = false;
		}
		for (Integer count : countGoods) {
			if (count < 1) {
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
		sessionVo.setMachineCode(inno72Machine.getMachineCode());
		sessionVo.setActivityId(inno72Activity.getId());

		List<GoodsVo> list = loadGameInfo(mid);
		LOGGER.info("loadGameInfo is {} ", JsonUtil.toJson(list));
		sessionVo.setGoodsList(list);

		gameSessionRedisUtil.setSessionEx(sessionUuid, JSON.toJSONString(sessionVo));

		this.startGameLife(userChannel, inno72Activity, inno72ActivityPlan, inno72Game, inno72Machine, userId);

		LOGGER.info("playCode is" + playCode);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("playCode", playCode);
		resultMap.put("qrStatus", qrStatus);
		resultMap.put("sellerId", inno72Merchant.getMerchantCode());

		return Results.success(JSONObject.toJSONString(resultMap));
	}

	/**
	 *
	 * @param mid
	 * @return
	 */
	private List<GoodsVo> loadGameInfo(String mid) {
		// 获取activePlanId
		LOGGER.info("loadGameInfo mid={}", mid);
		List<String> activityPlanIdList = inno72ActivityPlanMapper.findActivityPlanIdByMid(mid);
		if (activityPlanIdList == null || activityPlanIdList.size() == 0 || activityPlanIdList.size() > 1) {
			LOGGER.error("数据异常，获取activityPlanIdList");
			// 此处不抛出异常，以免影响其他业务
			return null;
		}
		String activityPlanId = activityPlanIdList.get(0);
		LOGGER.info("loadGameInfo activityPlanId ={}", activityPlanId);
		List<GoodsVo> list = inno72ActivityPlanMapper.getGoodsList(activityPlanId, mid);
		LOGGER.info("loadGameInfo GoodsList ={}", new Gson().toJson(list));
		return list;
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
		// todo 暂时注释掉 inno72Machine.getOpenStatus() == 0
		if (inno72Machine.getMachineStatus() == 4) {
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
	private Inno72GameUserLife startGameLife(Inno72GameUserChannel userChannel, Inno72Activity inno72Activity,
			Inno72ActivityPlan inno72ActivityPlan, Inno72Game inno72Game, Inno72Machine inno72Machine, String userId) {
		Inno72Locale inno72Locale = inno72LocaleMapper.selectByPrimaryKey(inno72Machine.getLocaleId());
		Inno72GameUserLife life = new Inno72GameUserLife(userChannel == null ? null : userChannel.getGameUserId(),
				userChannel == null ? null : userChannel.getId(), inno72Machine.getMachineCode(),
				userChannel == null ? null : userChannel.getUserNick(), inno72ActivityPlan.getActivityId(),
				inno72Activity.getName(), inno72ActivityPlan.getId(), inno72Game.getId(), inno72Game.getName(),
				inno72Machine.getLocaleId(), inno72Locale == null ? "" : inno72Locale.getMall(), null, "", null, null,
				userId);
		LOGGER.info("插入用户游戏记录 ===> {}", JSON.toJSONString(life));
		inno72GameUserLifeMapper.insert(life);
		return life;

	}


	private String genInno72NologinOrder(String channelId, String activityPlanId, String machineId, String goodsId,
			Inno72Order.INNO72ORDER_GOODSTYPE product) {
		
		LOGGER.info("genInno72NologinOrder input {} {} {} {}", channelId, activityPlanId, machineId, goodsId);
		// 活动计划
		Inno72ActivityPlan inno72ActivityPlan = inno72ActivityPlanMapper.selectByPrimaryKey(activityPlanId);
		// 活动
		Inno72Activity inno72Activity = inno72ActivityMapper.selectByPrimaryKey(inno72ActivityPlan.getActivityId());

		Inno72Machine inno72Machine = inno72MachineMapper.selectByPrimaryKey(machineId);

		Inno72Channel inno72Channel = inno72ChannelMapper.selectByPrimaryKey(channelId);

		String orderNum = Inno72OrderNumGenUtil.genOrderNum(inno72Channel.getChannelCode(),
				inno72Machine.getMachineCode());
		LocalDateTime now = LocalDateTime.now();

		boolean b = inno72GameService.countSuccOrderNologin(channelId, activityPlanId);

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
		inno72Order.setPayStatus(Inno72Order.INNO72ORDER_PAYSTATUS.SUCC.getKey());
		inno72Order.setPayTime(LocalDateTime.now());
		inno72Order.setRefOrderId(null);
		inno72Order.setRefOrderStatus(null);
		inno72Order.setGoodsType(product.getKey());
		inno72Order.setRepetition(rep);

		Inno72OrderGoods orderGoods = new Inno72OrderGoods();
		if (product.getKey().equals(Inno72Order.INNO72ORDER_GOODSTYPE.PRODUCT.getKey())) {
			Inno72Goods inno72Goods = inno72GoodsMapper.selectByCode(goodsId);
			orderGoods.setGoodsCode(inno72Goods.getCode());
			orderGoods.setGoodsId(inno72Goods.getId());
			orderGoods.setGoodsName(inno72Goods.getName());
			orderGoods.setGoodsPrice(inno72Goods.getPrice());

			Inno72Shops inno72Shops = inno72ShopsMapper.selectByPrimaryKey(inno72Goods.getShopId());
			inno72Order.setShopsId(inno72Shops.getId());
			inno72Order.setShopsName(inno72Shops.getShopName());

		} else {
			Inno72Coupon inno72Coupon = inno72CouponMapper.selectByPrimaryKey(goodsId);
			orderGoods.setGoodsCode(inno72Coupon.getCode());
			orderGoods.setGoodsId(inno72Coupon.getId());
			orderGoods.setGoodsName(inno72Coupon.getName());
			orderGoods.setGoodsPrice(BigDecimal.ZERO);
			Inno72Shops inno72Shops = inno72ShopsMapper.selectByPrimaryKey(inno72Coupon.getShopsId());
			inno72Order.setShopsId(inno72Shops.getId());
			inno72Order.setShopsName(inno72Shops.getShopName());
		}

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

		inno72Order.setUserId(gameUserId);


		Inno72OrderGoods orderGoods = new Inno72OrderGoods();
		if (product.getKey().equals(Inno72Order.INNO72ORDER_GOODSTYPE.PRODUCT.getKey())) {
			Inno72Goods inno72Goods = inno72GoodsMapper.selectByCode(goodsId);
			orderGoods.setGoodsCode(inno72Goods.getCode());
			orderGoods.setGoodsId(inno72Goods.getId());
			orderGoods.setGoodsName(inno72Goods.getName());
			orderGoods.setGoodsPrice(inno72Goods.getPrice());

			Inno72Shops inno72Shops = inno72ShopsMapper.selectByPrimaryKey(inno72Goods.getShopId());
			inno72Order.setShopsId(inno72Shops.getId());
			inno72Order.setShopsName(inno72Shops.getShopName());

		} else {
			Inno72Coupon inno72Coupon = inno72CouponMapper.selectByPrimaryKey(goodsId);
			orderGoods.setGoodsCode(inno72Coupon.getCode());
			orderGoods.setGoodsId(inno72Coupon.getId());
			orderGoods.setGoodsName(inno72Coupon.getName());
			orderGoods.setGoodsPrice(BigDecimal.ZERO);
			Inno72Shops inno72Shops = inno72ShopsMapper.selectByPrimaryKey(inno72Coupon.getShopsId());
			inno72Order.setShopsId(inno72Shops.getId());
			inno72Order.setShopsName(inno72Shops.getShopName());
		}

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
		LogReqrest logReqrest = new LogReqrest("", Long.valueOf(itemId), Long.valueOf(sellerId), "用户互动时常",
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
			requestForm.put("mid", userSessionVo.getMachineCode()); // 实际为code
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
		LOGGER.info("获取派样商品接口（入参） => machineCode:{}", machineCode);
		if (StringUtil.isEmpty(machineCode)) {
			return Results.failure("machineCode为空!");
		}
		// 获取存储图片阿里云地址
		String aliyunUrl = inno72GameServiceProperties.get("returnUrl");

		// 根据机器code查询派样商品
		List<Inno72SamplingGoods> inno72SamplingGoodsList = inno72GoodsMapper.selectSamplingGoods(machineCode);
		if (inno72SamplingGoodsList != null && inno72SamplingGoodsList.size() > 0) {
			for (Inno72SamplingGoods sampLingGoods : inno72SamplingGoodsList) {
				// 根据商品id查询货道
				Map<String, String> channelParam = new HashMap<String, String>();
				channelParam.put("goodId", sampLingGoods.getId());
				channelParam.put("machineId", sampLingGoods.getMachineId());
				List<Inno72SupplyChannel> inno72SupplyChannels = inno72SupplyChannelMapper
						.selectByGoodsId(channelParam);

				Integer goodsCount = 0;
				if (inno72SupplyChannels != null && inno72SupplyChannels.size() > 0) {
					// 所有具有相同商品id的货道中中道商品数量相加
					for (Inno72SupplyChannel channel : inno72SupplyChannels) {
						goodsCount += channel.getGoodsCount();
					}
				}
				sampLingGoods.setNum(goodsCount);

				// 根据商品id查询相关店铺信息
				Map<String, String> param = new HashMap<String, String>();
				param.put("shopId", sampLingGoods.getShopId());
				param.put("activityId", sampLingGoods.getActiveId());
				Inno72SamplingGoods shopInfo = inno72GoodsMapper.selectShopInfo(param);
				if (shopInfo != null) {
					sampLingGoods.setShopName(shopInfo.getShopName());
					sampLingGoods.setIsVip(shopInfo.getIsVip());
					sampLingGoods.setSessionKey(shopInfo.getSessionKey());
				}

				// 为商品表中商品图片路径拼上阿里云路径
				if (sampLingGoods.getImg() != null && !"".equals(sampLingGoods.getImg())) {
					sampLingGoods.setImg(aliyunUrl + sampLingGoods.getImg());
				}
				if (sampLingGoods.getBanner() != null && !"".equals(sampLingGoods.getBanner())) {
					sampLingGoods.setBanner(aliyunUrl + sampLingGoods.getBanner());
				}
			}
			LOGGER.info("返回派样商品列表 => list:{}", JSON.toJSONString(inno72SamplingGoodsList));
			return Results.success(inno72SamplingGoodsList);
		} else {
			LOGGER.info("获取派样商品接口（返回） => list:{}", "返回结果为空");
			return Results.failure("此机器当前没有排期或者没有对应的派样商品");
		}


	}


	private String getActive() {
		String active = System.getenv("spring_profiles_active");
		LOGGER.info("获取spring_profiles_active：{}", active);
		if (active == null || active.equals("")) {
			LOGGER.info("未读取到spring_profiles_active的环境变量,使用默认值: dev");
			active = "dev";
		}
		return active;
	}
	
	@Override
	public Result<String> setHeartbeat(String machineCode, String page, String planCode, String activity, String desc) {
		LOGGER.info("setHeartbeat machineCode is {}, page is {}, planCode is {}, activity is {}, desc is {}",
				machineCode, page, planCode, activity, desc);
		Inno72Machine inno72Machine = inno72MachineMapper.findMachineByCode(machineCode);
		if (inno72Machine == null) {
			return Results.failure("此机器编码没有查到相对应的机器！");
		} else {
			LOGGER.info("setHeartbeat inno72Machine is {}", JsonUtil.toJson(inno72Machine));
		}

		if (!(inno72Machine.getMachineStatus() == 4)) { // 4 表示机器状态正常
			LOGGER.info("setHeartbeat 机器状态不正常 machineCode is {}", machineCode);
			return Results.failure("机器状态不正常");
		}

		if (inno72Machine.getOpenStatus() == null || !(inno72Machine.getOpenStatus() == 0)) { // 0 表示接受报警
			LOGGER.info("setHeartbeat 当前机器不接收报警 machineCode is {}", machineCode);
			return Results.failure("当前机器不接收报警");
		}

		Map<String, String> remarkMap = new HashMap();
		remarkMap.put("planCode", planCode);
		remarkMap.put("activity", activity);
		remarkMap.put("desc", desc);

		AlarmDetailBean alarmDetailBean = new AlarmDetailBean();
		alarmDetailBean.setMachineId(inno72Machine.getId());
		alarmDetailBean.setType(1);
		alarmDetailBean.setPageInfo(page);
		alarmDetailBean.setRemark(JsonUtil.toJson(remarkMap));

		LOGGER.info("setHeartbeat alarmDetailBean is {}", JsonUtil.toJson(alarmDetailBean));

		try {
			alarmUtil.saveAlarmDetail(alarmDetailBean);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return Results.failure("设置心跳失败");
	}
}
