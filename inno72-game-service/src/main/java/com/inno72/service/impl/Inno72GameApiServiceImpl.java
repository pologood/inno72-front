package com.inno72.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.inno72.common.ApplicationContextHandle;
import com.inno72.common.BeidemaConstants;
import com.inno72.common.CommonBean;
import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.RedisConstants;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.StandardLoginTypeEnum;
import com.inno72.common.json.JsonUtil;
import com.inno72.common.util.DateUtil;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.common.util.Inno72OrderNumGenUtil;
import com.inno72.common.util.QrCodeUtil;
import com.inno72.common.util.SignUtil;
import com.inno72.common.util.UuidUtil;
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
import com.inno72.model.AlarmDetailBean;
import com.inno72.model.AlarmDropGoodsBean;
import com.inno72.model.AlarmLackGoodsBean;
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
import com.inno72.model.Inno72InteractGoods;
import com.inno72.model.Inno72InteractMachine;
import com.inno72.model.Inno72InteractMachineGoods;
import com.inno72.model.Inno72Locale;
import com.inno72.model.Inno72Machine;
import com.inno72.model.Inno72MachineDevice;
import com.inno72.model.Inno72Merchant;
import com.inno72.model.Inno72Order;
import com.inno72.model.Inno72OrderGoods;
import com.inno72.model.Inno72OrderHistory;
import com.inno72.model.Inno72Shops;
import com.inno72.model.Inno72SupplyChannel;
import com.inno72.plugin.http.HttpClient;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.BeideMaClient;
import com.inno72.service.Inno72ChannelService;
import com.inno72.service.Inno72GameApiService;
import com.inno72.service.Inno72GameService;
import com.inno72.service.Inno72GameUserChannelService;
import com.inno72.service.Inno72InteractGoodsService;
import com.inno72.service.Inno72InteractMachineGoodsService;
import com.inno72.service.Inno72InteractMachineTimeService;
import com.inno72.service.Inno72MachineDeviceService;
import com.inno72.service.Inno72MachineService;
import com.inno72.service.Inno72NewretailService;
import com.inno72.service.Inno72OrderService;
import com.inno72.service.Inno72QrCodeService;
import com.inno72.service.Inno72TopService;
import com.inno72.service.Inno72UnStandardService;
import com.inno72.service.PointService;
import com.inno72.util.AlarmUtil;
import com.inno72.vo.GoodsVo;
import com.inno72.vo.Inno72MachineInformation;
import com.inno72.vo.Inno72MachineVo;
import com.inno72.vo.Inno72SamplingGoods;
import com.inno72.vo.MachineApiVo;
import com.inno72.vo.StandardPrepareLoginReqVo;
import com.inno72.vo.UserSessionVo;
import com.taobao.api.ApiException;

@Service
public class Inno72GameApiServiceImpl implements Inno72GameApiService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72GameApiServiceImpl.class);
	@Resource
	private BeideMaClient beideMaClient;
	@Resource
	private Inno72GameServiceProperties inno72GameServiceProperties;
	@Resource
	private Inno72UnStandardService inno72UnStandardService;
	@Resource
	private Inno72QrCodeService qrCodeService;
	@Resource
	private Inno72GameMapper inno72GameMapper;
	@Resource
	private Inno72OrderService inno72OrderService;
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
	private Inno72GameUserChannelService inno72GameUserChannelService;
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
	private AlarmUtil alarmUtil;
	@Resource
	private Inno72InteractGoodsService inno72InteractGoodsService;

	@Resource
	private Inno72InteractMachineTimeService inno72InteractMachineTimeService;

	@Resource
	private Inno72InteractMachineGoodsService inno72InteractMachineGoodsService;

	@Resource
	private Inno72MachineDeviceService inno72MachineDeviceService;

	@Resource
	private Inno72NewretailService inno72NewretailService;

	@Resource
	private Inno72MachineService inno72MachineService;

	@Resource
	private PointService pointService;

	@Value("${machinecheckappbackend.uri}")
	private String machinecheckappbackendUri;

	@Value("${machinealarm.uri}")
	private String machinealarmUri;

	@Autowired
	private Inno72GameServiceProperties properties;

	@Value("${env}")
	private String env;

	private static String FINDLOCKGOODSPUSH_URL = "/machine/channel/findLockGoodsPush";
	private static final String QRSTATUS_NORMAL = "0"; // 二维码正常
	private static final String QRSTATUS_INVALID = "-1"; // 二维码失效
	private static final String QRSTATUS_EXIST_USER = "-2"; // 存在用户登录

	private static final Integer PRODUCT_NO_EXIST = -1; // 商品不存在
	private static final Integer CANORDER_FALSE = -2; // 限制下单
	private static final Integer SAMPLING_TYPE = 1; // 类型（派样）
	@Value("${sell_session_key}")
	private String sellSessionKey;
	/**
	 *
	 * @param vo
	 * sessionUuid
	 * orderId
	 * @return string
	 */
	@Override
	public Result<Object> orderPolling(MachineApiVo vo) {


		String sessionUuid = vo.getSessionUuid();
		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);

		Inno72ChannelService channelService = (Inno72ChannelService)ApplicationContextHandle.getBean(StandardLoginTypeEnum.getValue(userSessionVo.getChannelType()));
		return channelService.orderPolling(userSessionVo,vo);
	}

	/**
	 * 标准下单接口
	 * @param vo
	 */
	@Override
	public Result<Object> standardOrder(MachineApiVo vo) {
		LOGGER.info("standardOrder params is {}", JsonUtil.toJson(vo));

		String sessionUuid = vo.getSessionUuid();
		if (StringUtil.isEmpty(sessionUuid)) {
			return Results.success("参数错误!");
		}
		// 新增
		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(vo.getSessionUuid());
		if (userSessionVo == null) {
			return Results.failure("登录失效!");
		}
		//放入类型
		if (StringUtil.notEmpty(vo.getGoodsLogic())){
			userSessionVo.setGoodsLogic(vo.getGoodsLogic());
		}
		boolean paiyangFlag = userSessionVo.findPaiyangFlag();

		// 转至派样逻辑
		if(paiyangFlag){
			return paiyangOrder(userSessionVo,vo);
		}

		List<Inno72ActivityPlanGameResult> planGameResults = this.getGameResults(vo, userSessionVo);
		LOGGER.info("获取游戏结果 {}", JsonUtil.toJson(planGameResults));
		if (planGameResults.size() == 0 ){
			return Results.failure("没有可用的商品!");
		}

		LOGGER.debug("下单 userSessionVo ==> {}", JSON.toJSONString(userSessionVo));

		List<String> resultGoodsId = new ArrayList<>();

		int lotteryCode = 1;
		boolean needPay = false;
		String payQrcodeImage = "";
		int orderCode = 1;

		// 判断是否有商品

		//		boolean hasGoods = checkHasGoods(planGameResults, userSessionVo.getMachineId());
		//		if (!hasGoods) {
		//			return Results.failure("货道可用商品数量为0！");
		//		}

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
					Result<Object> orderResult = this.sendOrder(userSessionVo, code,prizeId);
					orderCode = orderResult.getCode();
					if (orderResult.getCode() == Result.SUCCESS) {
						Map map = (Map)orderResult.getData();
						needPay = (Boolean)map.get("needPay");
						payQrcodeImage = (String)map.get("payQrcodeImage");
					}

					resultGoodsId.add(prizeId);
					break;
				case "2":
					// 下优惠券订单
					Result<Object> lottery = this.lottery(userSessionVo, vo.getUa(), vo.getUmid(), result.getPrizeId());
					LOGGER.debug("抽取奖券 结果 ==> {}", JSON.toJSONString(lottery));
					lotteryCode = lottery.getCode();
					break;
				default:
					return Results.failure("无商品类型");
			}
		}

		Map<String, Object> result = new HashMap<>();

		this.setChannelInfo(userSessionVo, result, resultGoodsId);

		if (resultGoodsId.isEmpty()) {
			orderCode = PRODUCT_NO_EXIST;
		}

		// 立顿互动需要提前下预支付订单
		String payUrl = "";
		if (vo.getGoodsLogic().equals(CommonBean.goodsLogic.LI_DUN)){
			try {
				//立顿逻辑 去下预支付订单
				// {"code":0,"data":{"spId":"1001","outTradeNo":"8b4ed0b9e6a24ab08dfa4c31f3995171","type":2,"terminalType":6,"billId":"1111194802768118016","prepayId":null,"qrCode":"https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb?prepay_id=wx28171434335967ba0ca3c07a2232894422&package=1069367431"},"msg":"ok"}
				// {"code":50002,"data":null,"msg":"账单已经被创建"}
				Result payResult = this.sendAdvancePayOrder(userSessionVo);
				if (payResult.getCode() == Result.FAILURE){
					return Results.failure(payResult.getMsg());
				}
				String data = payResult.getData().toString();
				JSONObject jsonObject = JSON.parseObject(data);
				String qrCode = jsonObject.getString("qrCode");
				result.put("payUrl", qrCode);
			} catch (Exception e) {
				LOGGER.info(e.getMessage());
				return Results.failure("支付下单错误！");
			}
		}

		result.put("time", new Date().getTime());
		result.put("lotteryResult", lotteryCode);
		result.put("orderResult", orderCode);

		result.put("needPay", needPay);
		result.put("payQrcodeImage", payQrcodeImage);

		LOGGER.info("standardOrder is {}", JsonUtil.toJson(result));
		return Results.success(result);
	}


	//TODO 下预支付订单
	private Result sendAdvancePayOrder(UserSessionVo userSessionVo) throws UnknownHostException {

		Map<String,String> param = new HashMap<String,String>();
		param.put("notifyUrl",properties.get("notifyUrl"));
		param.put("outTradeNo",userSessionVo.getInno72OrderId());
		param.put("qrTimeout",properties.get("qrTimeout"));
		param.put("quantity","1");
		param.put("spId",properties.get("spId"));
		param.put("subject","北京点七二创意互动传媒文化有限公司");
		param.put("terminalType","6");
		param.put("extra",userSessionVo.getMachineCode());
		BigDecimal temp = new BigDecimal(100);
		param.put("totalFee",new BigDecimal("20").multiply(temp).longValue()+"");
		param.put("transTimeout",properties.get("transTimeout"));
		param.put("type", Inno72Order.INNO72ORDER_PAYTYPE.WECHAT.getKey() +"");
		param.put("unitPrice",new BigDecimal("20").multiply(temp).longValue()+"");
		String sign = SignUtil.genSign(param, properties.get("secureKey"));
		param.put("sign",sign);

		InetAddress address = InetAddress.getLocalHost();
		param.put("clientIp", address.getHostAddress());
		LOGGER.info("pay invoke param = {}",JsonUtil.toJson(param));
		String respJson = HttpClient.form(properties.get("payServiceUrl"), param, null);
		LOGGER.info("pay invoke response = {}",respJson);

		return JSON.parseObject(respJson, Result.class);
	}

	private Result<Object> paiyangOrder(UserSessionVo userSessionVo, MachineApiVo vo) {

		LOGGER.debug("下单 userSessionVo ==> {}", JSON.toJSONString(userSessionVo));
		//计算canorder
		Inno72ChannelService channelService = (Inno72ChannelService)ApplicationContextHandle.getBean(StandardLoginTypeEnum.getValue(userSessionVo.getChannelType()));
		if(!StringUtils.isEmpty(vo.getItemId())){
			userSessionVo.setGoodsId(vo.getItemId());
		}
		Map<String, Object> result = new HashMap<>();
		boolean canOrder = channelService.getCanOrder(userSessionVo);
		if(!canOrder){
			result.put("orderResult", CANORDER_FALSE);
			result.put("errorMsg","您已经玩过此游戏");
			return Results.success(result);
		}
		String goodsId = userSessionVo.getGoodsId();
		String interactId = userSessionVo.getActivityId();
		LOGGER.info("paiyangOrder goodsId={},interactId={}",goodsId,interactId);
		//查询商品类型
		Inno72InteractGoods inno72InteractGoods = inno72InteractGoodsService.findByInteractIdAndGoodsId(interactId,goodsId);
		int lotteryCode = 1;
		boolean needPay = false;
		String channelCode = "";
		String payQrcodeImage = "";
		int orderCode = 1;
		List<String> resultGoodsId = null;
		Integer prizeType = inno72InteractGoods.getType();
		if(Inno72InteractGoods.TYPE_GOODS.equals(prizeType)){
			resultGoodsId = new ArrayList<>();
			// 下商品订单
			Inno72Goods inno72Goods = inno72GoodsMapper.selectByPrimaryKey(goodsId);
			if (inno72Goods == null) {
				LOGGER.debug("讲真的。配置商品..它不见了。。。! goodsId={}", goodsId);
				return Results.failure("讲真的。配置商品..它不见了。。。!");
			}
			String code = inno72Goods.getCode();
			Result<Object> orderResult = this.sendOrder(userSessionVo, code,goodsId);
			orderCode = orderResult.getCode();
			if (orderResult.getCode() == Result.SUCCESS) {
				Map map = (Map)orderResult.getData();
				needPay = (Boolean)map.get("needPay");
				channelCode = (String)map.get("channelCode");
				payQrcodeImage = (String)map.get("payQrcodeImage");
			}
			resultGoodsId.add(goodsId);
			//下单优惠卷 商品所关联的优惠卷
			if(!StringUtils.isEmpty(inno72InteractGoods.getCoupon())){
				Inno72Coupon coupon = inno72CouponMapper.selectByPrimaryKey(inno72InteractGoods.getCoupon());
				//下单优惠卷
				LOGGER.debug("下单优惠卷 id={}",coupon.getId());
				Result<Object> lottery = this.lottery(userSessionVo, vo.getUa(), vo.getUmid(), coupon.getId());
				LOGGER.debug("抽取奖券 结果 ==> {}", JSON.toJSONString(lottery));
				lotteryCode = lottery.getCode();
			}
		}else if(Inno72InteractGoods.TYPE_COUPON.equals(prizeType)){
			// 下优惠券订单
			Result<Object> lottery = this.lottery(userSessionVo, vo.getUa(), vo.getUmid(), goodsId);
			LOGGER.debug("抽取奖券 结果 ==> {}", JSON.toJSONString(lottery));
			lotteryCode = lottery.getCode();
		}else{
			return Results.failure("无商品类型");
		}

		if (resultGoodsId == null ||resultGoodsId.isEmpty()) {
			orderCode = PRODUCT_NO_EXIST;
		}

		if(resultGoodsId!=null&&resultGoodsId.size()>0){
			this.setChannelInfo(userSessionVo, result, resultGoodsId);
		}

		result.put("time", new Date().getTime());
		result.put("lotteryResult", lotteryCode);
		result.put("orderResult", orderCode);
		result.put("channelCode",channelCode);
		result.put("needPay", needPay);
		result.put("payQrcodeImage", payQrcodeImage);

		LOGGER.info("standardOrder is {}", JsonUtil.toJson(result));
		return Results.success(result);
	}



	/**
	 * 查找派样优惠卷有没有配置
	 * @param machineCode
	 * @param couponId
	 * @return
	 */
	private boolean findPaiyangCouponSettingFlag(String machineCode, String couponId) {
		//查询该机器配置的商品信息（当前时间在起止时间内的）
		Inno72InteractMachine interactMachine = inno72InteractMachineTimeService.findActiveInteractMachine(machineCode);
		if(interactMachine == null){
			LOGGER.info("getSampling,此机器无活动配置machineCode={}",machineCode);
			return false;
		}
		List<Inno72InteractMachineGoods> goodsList = inno72InteractMachineGoodsService.findMachineGoodsByMachineAndGoodsId(interactMachine.getId(),couponId);
		return goodsList!=null&&goodsList.size()>0;
	}

	/**
	 * 判断是否有货
	 * @return
	 */
	private boolean checkHasGoods(List<Inno72ActivityPlanGameResult> planGameResults, String machineId) {
		boolean hasGoods = false;
		int goodsCount = 0;
		for (Inno72ActivityPlanGameResult planGameResult : planGameResults) {
			String prizeType = planGameResult.getPrizeType();
			if (prizeType.equals("2")) {
				continue;
			}
			String prizeId = planGameResult.getPrizeId();

			Map<String, String> channelParam = new HashMap<>();
			channelParam.put("goodId", prizeId);
			channelParam.put("machineId", machineId);
			List<Inno72SupplyChannel> inno72SupplyChannels = inno72SupplyChannelMapper.selectByGoodsId(channelParam);

			if (inno72SupplyChannels.size() == 0) {
				break;
			}

			for (Inno72SupplyChannel inno72SupplyChannel : inno72SupplyChannels) {
				Integer isDelete = inno72SupplyChannel.getIsDelete();
				if (isDelete == 0) {
					goodsCount += inno72SupplyChannel.getGoodsCount();
				}
			}
		}
		if (goodsCount > 0) {
			hasGoods = true;
		}
		return hasGoods;
	}

	/**
	 * 设置货道信息
	 */
	@Override
	public Result<Object> setChannelInfo(UserSessionVo userSessionVo, Map<String, Object> result, List<String> resultGoodsId) {
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

				if (userSessionVo.getGoodsLogic().equals(CommonBean.goodsLogic.LI_DUN)){
					userSessionVo.setChannelCode(code);
					userSessionVo.setGoodsCode(goodsCode);
				}

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
		return Results.success();
	}

	/**
	 * 获得游戏结果
	 */
	private List<Inno72ActivityPlanGameResult> getGameResults(MachineApiVo vo, UserSessionVo userSessionVo) {
		LOGGER.info("获得游戏结果 params machineApiVo is {}, userSessionVo is {} ", JsonUtil.toJson(vo), JsonUtil.toJson(userSessionVo));
		String report = vo.getReport();
		String activityPlanId = userSessionVo.getActivityPlanId();
		String activityId = userSessionVo.getActivityId();
		String goodsId = userSessionVo.getGoodsId();
		int activityType = userSessionVo.getActivityType();
		String machineCode = userSessionVo.getMachineCode();
		String machineId = userSessionVo.getMachineId();
		String merchantId = userSessionVo.getChannelMerchantId();


		List<Inno72ActivityPlanGameResult> planGameResults;
		/*
		 * 商品特殊出货逻辑枚举
		 */
		switch (vo.getGoodsLogic()){
			case CommonBean.goodsLogic.LI_DUN:
				planGameResults = liDunResult(machineId, merchantId, activityId, activityType, userSessionVo);
			default:
				planGameResults = defaultResult(activityType, goodsId, activityPlanId, report);
		}
		return planGameResults;
	}

	private List<Inno72ActivityPlanGameResult> defaultResult(Integer activityType, String goodsId,
			String activityPlanId, String report){
		List<Inno72ActivityPlanGameResult> planGameResults;
		if (Inno72Activity.ActivityType.PAIYANG.getType() == activityType) {
			Inno72Goods inno72GoodsCheck = inno72GoodsMapper.selectByPrimaryKey(goodsId);
			String shopId = inno72GoodsCheck.getShopId();

			Map<String, String> params = new HashMap<>(3);
			params.put("goodsId", inno72GoodsCheck.getId());
			params.put("shopId", shopId);
			params.put("planId", activityPlanId);
			planGameResults = inno72ActivityPlanGameResultMapper.selectByParams(params);
		} else {
			Map<String, String> params = new HashMap<>();
			params.put("activityPlanId", activityPlanId);
			params.put("report", report);
			planGameResults = inno72ActivityPlanGameResultMapper.selectAllResultByCode(params);
		}
		return planGameResults;
	}

	/**
	 * 立顿活动商品出货计算规则
	 * @param machineId 机器ID
	 * @param merchantId 商家ID
	 * @param activityId 活动ID
	 * @param userSessionVo
	 * @return 可出商品的result
	 */
	private List<Inno72ActivityPlanGameResult> liDunResult(String machineId, String merchantId, String activityId,
			Integer activityType, UserSessionVo userSessionVo){
		Map<String, String> param = new HashMap<>(3);
		param.put("machineId", machineId);
		param.put("merchantId", merchantId);
		param.put("activityId", activityId);
		//查询以玩次数的单个商品的总金额 单个商品总次数 和商品ID 返回： goodsId 商品ID countId 单个商品条数  PayPrice 支付金额
		List<Map<String, String>> orders = inno72OrderMapper.findTotalMoney(param);

		BigDecimal totalPayPrice = BigDecimal.ZERO;
		BigDecimal totalRealPrice = BigDecimal.ZERO;
		Map<String, BigDecimal> goodsPrice = new HashMap<>();
		for (Map<String, String> order : orders){
			totalPayPrice = totalPayPrice.add(new BigDecimal(order.get("PayPrice")));
			String goodsId = order.get("goodsId");
			BigDecimal realPrice = goodsPrice.get(goodsId);
			if (realPrice == null){
				Inno72Goods inno72Goods = inno72GoodsMapper.selectByPrimaryKey(goodsId);
				realPrice = inno72Goods.getPrice();
				goodsPrice.put(goodsId, realPrice);
			}
			totalRealPrice = totalRealPrice.add(realPrice.multiply(new BigDecimal(order.get("countId"))));
		}
		// 因固定6件商品价格为4块和45块。 用goodsProbability
		String index = Optional.ofNullable(redisUtil.get(CommonBean.REDIS_ACTIVITY_LIDUN_GOODS_SHIPMENT_INDEX_KEY)).orElse("60");
		// 商品概率 取100整数作为概率，如果当次可以出现高价商品，则还需要累计高价商品概率，最终结果 如果大于60 则出现随机高价商品，反之则出现随机底价商品
		int result = 0;
		// 1 出高价 0 低价
		int resultTarget = 0;
		// 如果当前机器以玩次数获得金额总数比加上下次可能出现的高价商品的45元的真是价格高，则满足出现高价商品的因素
		if (totalPayPrice.compareTo(totalRealPrice.add(new BigDecimal(45))) > 0){
			String s = redisUtil.get(CommonBean.REDIS_ACTIVITY_LIDUN_GOODS_PROBABILITY_KEY);
			if (StringUtil.isEmpty(s)){s = "10";}
			BigDecimal bs = new BigDecimal(s);
			if (Integer.parseInt(s) % 10 == 0){
				// 取随机数的概率值 大于60则会出现高价商品， 概率值会随每次可以出现高价商品但是随机失败的概率结果增加10 最高100
				result = bs.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(new Random().nextInt(100)+"")).intValue();
			}else{
				result = 100;
			}
			if (result >= Integer.parseInt(index)){
				resultTarget = 1;
				redisUtil.set(CommonBean.REDIS_ACTIVITY_LIDUN_GOODS_PROBABILITY_KEY, 10+"");
			}else{
				redisUtil.set(CommonBean.REDIS_ACTIVITY_LIDUN_GOODS_PROBABILITY_KEY, (bs.intValue() + 10 > 100 ? bs.intValue() : bs.intValue() + 10)+"");
			}
		}

		Map<String, String> params = new HashMap<>(3);
		params.put("probability", resultTarget+"");
		params.put("machineId", machineId);
		params.put("merchantId", merchantId);
		params.put("activityId", activityId);
		LOGGER.info("{}, 没有对应价位的商品，查询条件{}" + (resultTarget == 0 ? "立顿低价商品" : "立顿高价商品"), JSON.toJSONString(params));
		List<Inno72ActivityPlanGameResult> planGameResults = inno72ActivityPlanGameResultMapper
				.selectLiDunGoods(params);
		if (planGameResults.size() == 0){
			params.put("probability", params.get("probability").equals("0") ? "1" : "0");
			LOGGER.info("{}, 没有对应价位的商品，重新查询条件{}" + (resultTarget == 0 ? "立顿低价商品" : "立顿高价商品"), JSON.toJSONString(params));
			planGameResults = inno72ActivityPlanGameResultMapper
					.selectLiDunGoods(params);
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
				if (o1.getGoodsCount() == null) o1.setGoodsCount(0);
				if (o2.getGoodsCount() == null) o2.setGoodsCount(0);
				if (o1.getGoodsCount() > o2.getGoodsCount()) {
					return -1;
				}

				if (o1.getGoodsCount().equals(o2.getGoodsCount())) {
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
	private Result<Object> sendOrder(UserSessionVo userSessionVo, String itemId,String goodsId) {

		String channelId = userSessionVo.getChannelId();
		String machineId = userSessionVo.getMachineId();
		String activityPlanId = userSessionVo.getActivityPlanId();
		String sessionUuid = userSessionVo.getSessionUuid();

		LOGGER.info("商品下单 userSessionVo =》 {}", JSON.toJSONString(userSessionVo));

		Inno72Machine inno72Machine = inno72MachineMapper.selectByPrimaryKey(machineId);
		if (inno72Machine == null) {
			return Results.failure("下商品订单机器信息错误!");
		}
		boolean paiyangflag = userSessionVo.findPaiyangFlag();
		String inno72OrderId;
		if(paiyangflag){
			// 下单 inno72_Order TODO 商品下单 itemId 对应的类型？
			inno72OrderId = genPaiyangInno72Order(userSessionVo, sessionUuid, userSessionVo.getCanOrder(),
					channelId, activityPlanId, machineId,
					goodsId, userSessionVo.getUserId(),
					Inno72Order.INNO72ORDER_GOODSTYPE.PRODUCT);
		}else{
			// 下单 inno72_Order 商品下单 itemId 对应的类型？
			inno72OrderId = genInno72Order(sessionUuid, channelId, activityPlanId, machineId, itemId, userSessionVo.getUserId(),
					Inno72Order.INNO72ORDER_GOODSTYPE.PRODUCT);
		}

		LOGGER.info("更新的session =====> {}", JSON.toJSONString(userSessionVo));
		if (inno72OrderId.equals("0")) {
			LOGGER.info("已经超过最大游戏数量啦 QAQ!");
			return Results.failure("已经超过最大游戏数量啦 QAQ!");
		}
		LOGGER.info("sendOrder inno72OrderId {}", inno72OrderId);
		userSessionVo.setInno72OrderId(inno72OrderId);
		Inno72ChannelService channelService = (Inno72ChannelService)ApplicationContextHandle.getBean(StandardLoginTypeEnum.getValue(userSessionVo.getChannelType()));

		Result<Object> r =  channelService.order(userSessionVo,itemId,inno72OrderId);
		pointService.innerPoint(JSON.toJSONString(userSessionVo), Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE.ORDER_GOODS);

		//        gameSessionRedisUtil.setSession(sessionUuid, JSON.toJSONString(userSessionVo));
		return r;
	}

	/**
	 * 获得支付二维码
	 * @return
	 */
	private String getPayQrUrl(String url, String sessionUuid, UserSessionVo userSessionVo) {
		String tmallPayQrUrl = "";
		String returnUrl = "";
		boolean result = false;
		try {
			tmallPayQrUrl = QrCodeUtil.readQrCode(url);
			LOGGER.info("tmallPayQrUrl is {}", tmallPayQrUrl);

			if (!StringUtil.isEmpty(tmallPayQrUrl)) {
				// 生成新的支付二维码链接
				LOGGER.info("二维码访问 url is {} ", url);

				userSessionVo.setScanPayUrl(tmallPayQrUrl);

				// 二维码存储在本地的路径
				String localUrl = "pay" + sessionUuid + ".png";

				// 存储在阿里云上的文件名
				String objectName = "payQrcode/" + localUrl;

				String payUrl = String.format(
						"%s/?sessionUuid=%s&env=%s",
						inno72GameServiceProperties.get("payRedirect"), sessionUuid, env);

				// 提供给前端用来调用二维码的地址
				returnUrl = inno72GameServiceProperties.get("returnUrl") + objectName;
				LOGGER.info("支付url is {}", payUrl);
				try {
					result = QrCodeUtil.createQrCode(localUrl, payUrl, 800, "png");
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}

				if (result) {
					qrCodeService.qrCodeImgDeal(localUrl, objectName);
				}
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}

		return returnUrl;
	}


	@Override
	public String genPaiyangInno72Order(UserSessionVo userSessionVo,String sessionUuid, boolean canOrder ,String channelId, String activityPlanId, String machineId, String goodsId, String channelUserKey, Inno72Order.INNO72ORDER_GOODSTYPE product) {
		Inno72GameUserChannel userChannel;
		if(StandardLoginTypeEnum.ALIBABA.getValue().compareTo(userSessionVo.getChannelType()) == 0 || StandardLoginTypeEnum.INNO72.getValue().compareTo(userSessionVo.getChannelType()) == 0){
			userChannel =  inno72GameUserChannelService.findInno72GameUserChannel(channelId,channelUserKey,null);
		}else{
			userChannel =  inno72GameUserChannelService.findInno72GameUserChannel(channelId,channelUserKey,userSessionVo.getSellerId());
		}
		String gameUserId = userChannel.getGameUserId();

		Inno72Machine inno72Machine = inno72MachineMapper.selectByPrimaryKey(machineId);

		Inno72Channel inno72Channel = inno72ChannelMapper.selectByPrimaryKey(channelId);

		String orderNum = Inno72OrderNumGenUtil.genOrderNum(inno72Channel.getChannelCode(),
				inno72Machine.getMachineCode());
		LocalDateTime now = LocalDateTime.now();

		Integer rep;
		if (product.getKey().equals(Inno72Order.INNO72ORDER_GOODSTYPE.PRODUCT.getKey())) {
			rep = Inno72Order.INNO72ORDER_REPETITION.NOT.getKey();
		} else {
			rep = canOrder ? Inno72Order.INNO72ORDER_REPETITION.NOT.getKey()
					: Inno72Order.INNO72ORDER_REPETITION.REPETITION.getKey();
		}

		Inno72Order inno72Order = new Inno72Order();
		inno72Order.setChannelId(channelId);
		inno72Order.setGoodsStatus(Inno72Order.INNO72ORDER_GOODSSTATUS.WAIT.getKey());
		inno72Order.setInno72ActivityId(activityPlanId);
		inno72Order.setInno72ActivityPlanId(activityPlanId);
		inno72Order.setMachineId(machineId);
		inno72Order.setOrderNum(orderNum);
		inno72Order.setOrderPrice(BigDecimal.ZERO);
		inno72Order.setOrderTime(now);
		if(StandardLoginTypeEnum.ALIBABA.getValue().compareTo(userSessionVo.getChannelType()) == 0){
			inno72Order.setOrderType(Inno72Order.INNO72ORDER_ORDERTYPE.ALI.getKey());
		}else if(StandardLoginTypeEnum.INNO72.getValue().compareTo(userSessionVo.getChannelType()) == 0){
			inno72Order.setOrderType(Inno72Order.INNO72ORDER_ORDERTYPE.INNO72.getKey());
		}else{
			inno72Order.setOrderType(Inno72Order.INNO72ORDER_ORDERTYPE.DEFAULT.getKey());
		}
		inno72Order.setPayPrice(BigDecimal.ZERO);
		inno72Order.setPayStatus(Inno72Order.INNO72ORDER_PAYSTATUS.WAIT.getKey());
		inno72Order.setPayTime(null);
		inno72Order.setRefOrderId(null);
		inno72Order.setRefOrderStatus(null);
		inno72Order.setGoodsType(product.getKey());
		inno72Order.setRepetition(rep);
		inno72Order.setOrderStatus(Inno72Order.INNO72ORDER_ORDERSTATUS.WAIT.getKey());
		inno72Order.setUserId(gameUserId);
		//下单时放入
		//TODO 整理代码
		if (StringUtil.notEmpty(userSessionVo.getGoodsLogic()) && userSessionVo.getGoodsLogic().equals(CommonBean.goodsLogic.LI_DUN)){
			inno72Order.setPayType(Inno72Order.INNO72ORDER_PAYTYPE.WECHAT.getKey());
			inno72Order.setOrderPrice(new BigDecimal("20"));
			inno72Order.setPayPrice(new BigDecimal("20"));
		}

		Inno72OrderGoods orderGoods = new Inno72OrderGoods();

		String goodsName;

		if (product.getKey().equals(Inno72Order.INNO72ORDER_GOODSTYPE.PRODUCT.getKey())) {
			Inno72Goods inno72Goods = inno72GoodsMapper.selectByPrimaryKey(goodsId);
			goodsName = inno72Goods.getName();
			orderGoods.setGoodsCode(inno72Goods.getCode());
			orderGoods.setGoodsId(inno72Goods.getId());
			orderGoods.setGoodsName(goodsName);
			orderGoods.setGoodsPrice(inno72Goods.getPrice());

			Inno72Shops inno72Shops = inno72ShopsMapper.selectByPrimaryKey(inno72Goods.getShopId());
			inno72Order.setShopsId(inno72Shops.getId());
			inno72Order.setShopsName(inno72Shops.getShopName());
			inno72Order.setPayPrice(inno72Goods.getPrice());
			inno72Order.setOrderPrice(inno72Goods.getPrice());
			userSessionVo.setOrderPrice(inno72Goods.getPrice());
			inno72Order.setMerchantId(inno72Goods.getSellerId());

			userSessionVo.setGoodsId(inno72Goods.getId());
			userSessionVo.setGoodsCode(inno72Goods.getCode());
			userSessionVo.setGoodsName(inno72Goods.getName());

			/* 埋点 */
			CommonBean.logger(
					CommonBean.POINT_TYPE_GOODS_ORDER,
					inno72Machine.getMachineCode(),
					"用户[" + userChannel.getUserNick() + "], 生成["+ goodsName + "]订单，订单号[" + orderNum +"].",
					activityPlanId+"|"+inno72Goods.getCode());

		} else {
			Inno72Coupon inno72Coupon = inno72CouponMapper.selectByPrimaryKey(goodsId);
			goodsName = inno72Coupon.getName();
			orderGoods.setGoodsCode(inno72Coupon.getCode());
			orderGoods.setGoodsId(inno72Coupon.getId());
			orderGoods.setGoodsName(goodsName);
			orderGoods.setGoodsPrice(BigDecimal.ZERO);
			Inno72Shops inno72Shops = inno72ShopsMapper.selectByPrimaryKey(inno72Coupon.getShopsId());
			inno72Order.setShopsId(inno72Shops.getId());
			inno72Order.setShopsName(inno72Shops.getShopName());
			inno72Order.setMerchantId(inno72Shops.getSellerId());

			userSessionVo.setInteractId(inno72Coupon.getCode());

			/* 埋点 */
			CommonBean.logger(
					CommonBean.POINT_TYPE_COUPON_ORDER,
					inno72Machine.getMachineCode(),
					"用户[" + userChannel.getUserNick() + "]生成["+ goodsName + "]订单，订单号[" + orderNum +"].",
					activityPlanId+"|"+inno72Coupon.getCode());
		}
		orderGoods.setOrderNum(inno72Order.getOrderNum());
		orderGoods.setStatus(Inno72Order.INNO72ORDER_GOODSSTATUS.WAIT.getKey());
		inno72OrderMapper.insert(inno72Order);
		orderGoods.setOrderId(inno72Order.getId());
		inno72OrderGoodsMapper.insert(orderGoods);

		inno72OrderHistoryMapper.insert(new Inno72OrderHistory(inno72Order.getId(), inno72Order.getOrderNum(),
				JSON.toJSONString(inno72Order), "初始化插入订单!"));

		Inno72GameUserLife userLife = inno72GameUserLifeMapper.selectByUserChannelIdLast(userSessionVo.getUserId());
		userLife.setOrderId(inno72Order.getId());
		inno72GameUserLifeMapper.updateByPrimaryKeySelective(userLife);

		//		gameSessionRedisUtil.setSession(sessionUuid, JSON.toJSONString(userSessionVo));
		return inno72Order.getId();
	}

	/**
	 * 重构合并抽奖和下单接口为一个接口。这是抽奖接口
	 * @param vo vo
	 * @param ua ua - tida
	 * @param umid umid  -  tida
	 * @param prizeId prizeId
	 * @return object
	 */
	@Override
	public Result<Object> lottery(UserSessionVo vo, String ua, String umid, String prizeId) {

		String sessionUuid = vo.getSessionUuid();
		String activityPlanId = vo.getActivityPlanId();
		String channelId = vo.getChannelId();
		String machineId = vo.getMachineId();

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

		LOGGER.info("请求聚石塔 url ===> {} , 参数 ===> {}", CommonBean.TopUrl.LOTTORY, JSON.toJSONString(requestForm));

		String respJson = "";
		try {
			respJson = HttpClient.form(CommonBean.TopUrl.LOTTORY, requestForm, null);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		if (StringUtil.isEmpty(respJson)) {
			return Results.failure("聚石塔无返回数据!");
		}

		LOGGER.info("调用聚石塔接口  【抽奖】返回 ===> {}", respJson);

		pointService.innerPoint(JSON.toJSONString(vo), Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE.ORDER_COUPON);
		// 奖券下单
		String orderId;
		try {
			if(vo.findPaiyangFlag()){

				orderId = this.genPaiyangInno72Order(vo,sessionUuid, vo.getCanOrder(),channelId, activityPlanId, _machineId, inno72Coupon.getId(), vo.getUserId(),
						Inno72Order.INNO72ORDER_GOODSTYPE.COUPON);
			}else{
				orderId = this.genInno72Order(sessionUuid, channelId, activityPlanId, _machineId, inno72Coupon.getId(), vo.getUserId(),
						Inno72Order.INNO72ORDER_GOODSTYPE.COUPON);
			}


			Result<String> insertOrderToLife = inno72GameService.updateRefOrderId(orderId, "", vo.getUserId());
			LOGGER.debug("inno72Life 插入 orderId [{}] {}！", orderId, JSON.toJSONString(insertOrderToLife));

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

		//掉货成功修改redis值
		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(vo.getSessionUuid());
		LOGGER.info("shipmentReportV2 userSessionVo is {}", JsonUtil.toJson(userSessionVo));
		if (userSessionVo == null) {
			return Results.failure("登录失效!");
		}
		Result<String> succChannelResult = null;
		if (StringUtil.isNotEmpty(vo.getChannelId())) {
			succChannelResult = shipmentReport(vo);
			LOGGER.info("succChannelResult code is {} ", succChannelResult.getCode());
		}

		boolean paiyangflag = userSessionVo.findPaiyangFlag();
		if(paiyangflag){
			String date = DateUtil.getDateStringByYYYYMMDD();
			//每个商品每个用户每天可派发数
			String key = String.format(RedisConstants.PAIYANG_GOODS_ORDER_TIMES,userSessionVo.getActivityId(),userSessionVo.getGoodsId(),date,userSessionVo.getGameUserId());
			redisUtil.incr(key);
			//用户获得商品次数
			key = String.format(RedisConstants.PAIYANG_ORDER_TIMES,userSessionVo.getActivityId(),userSessionVo.getGameUserId());
			redisUtil.incr(key);
			//用户每天获得商品次数
			key = String.format(RedisConstants.PAIYANG_DAY_ORDER_TIMES,userSessionVo.getActivityId(),date,userSessionVo.getGameUserId());
			redisUtil.incr(key);
			//此商品总掉货数量(获取展示商品时候剩余数量用)
			key = String.format(RedisConstants.PAIYANG_MACHINE_GOODS,userSessionVo.getActivityId(),userSessionVo.getMachineId(),userSessionVo.getGoodsId());
			redisUtil.incr(key);
		}

		String failChannelIds = vo.getFailChannelIds();

		if (StringUtil.isNotEmpty(failChannelIds)) {
			userSessionVo.setFailChannelIds(failChannelIds);
			//			gameSessionRedisUtil.setSession(sessionUuid, JSON.toJSONString(userSessionVo));

			pointService.innerPoint(JSON.toJSONString(userSessionVo), Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE.LOCK_CHANNEL);

			Result<String> failChannelResult = this.shipmentFail(machineCode, failChannelIds, "");
			LOGGER.info("machineCode is {}, orderId is {}, failChannelIds is {}, code is {} ", machineCode, userSessionVo.getRefOrderId(), failChannelIds,
					failChannelResult.getCode());
		}
		if(BeidemaConstants.appId.equals(userSessionVo.getSellerId())){
			if(succChannelResult!=null&&succChannelResult.getCode() == Result.SUCCESS){
				//出货成功
				beideMaClient.shipment(userSessionVo.getUserId(),userSessionVo.getRefOrderId(),1);
			}else{
				//出货失败
				beideMaClient.shipment(userSessionVo.getUserId(),userSessionVo.getRefOrderId(),0);
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

		//修改订单状态为完成
		LOGGER.info("shipmentReport userSessionVo {}", JSON.toJSONString(userSessionVo));

		if (userSessionVo == null) {
			return Results.failure("登录失效!");
		}
		String orderId = userSessionVo.getRefOrderId();

		//修改订单状态为成功
		inno72OrderService.updateOrderStatus(userSessionVo.getInno72OrderId(),Inno72Order.INNO72ORDER_ORDERSTATUS.COMPLETE.getKey());

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

		// LOGGER.info("减货 参数 ===》 【machineId=>{}，channelId=>{}】;结果 ==> {}", machineId, channelId, i);

		try {
			findLockGoodsPush(machineCode, inno72SupplyChannel.getId());
		} catch (Exception e) {
			LOGGER.info("调用 saveLackGoodsBean 异常", e);
		}
		Integer channelType = userSessionVo.getChannelType();
		LOGGER.info("shipmentReport channelType is {}", channelType);

		Inno72ChannelService channelService = (Inno72ChannelService)ApplicationContextHandle.getBean(StandardLoginTypeEnum.getValue(channelType == null ? 0 : channelType));
		channelService.shipment(channelId,machineCode,userSessionVo,orderId,vo);

		Inno72Goods inno72Goods = inno72GoodsMapper.selectByChannelId(inno72SupplyChannel.getId());

		// todo gxg 观察代码执行情况

		channelService.feedBackInTime(userSessionVo.getInno72OrderId(),machineCode);

		pointService.innerPoint(JSON.toJSONString(userSessionVo), Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE.SHIPMENT);
		inno72UnStandardService.gamePointTime(sessionUuid,Inno72GameUserLife.SHIPMENT_TIME_TYPE);
		/* 埋点 */
		CommonBean.logger(
				CommonBean.POINT_TYPE_FINISH,
				machineCode,
				"机器 ["+machineCode+"], 货道 [" + inno72SupplyChannel.getName() + "(code:"+inno72SupplyChannel.getCode()+")] "
						+ "出货完成. 商品名称[" + inno72Goods.getName() + "]."
						+ "货道容量 ["+inno72SupplyChannel.getVolumeCount()+"]. "
						+ "原数量 ["+inno72SupplyChannel.getGoodsCount()+"], 当前数量 ["+updateChannel.getGoodsCount()+"]"
						+ "当前用户 [" + userSessionVo.getUserNick() + "]",
				userSessionVo.getActivityId() + "|" + inno72Goods.getCode() + "|" + inno72Goods.getName());

		return Results.success();
	}

	private Result findLockGoodsPush(String machineCode, String channelId) {
		LOGGER.info("saveLackGoodsBean invoked! machineCode={},channelId={}", machineCode, channelId);
		// 获取商品id
		String goodsId = inno72SupplyChannelMapper.findGoodsIdByChannelId(channelId);
		if (StringUtils.isEmpty(goodsId)) {
			LOGGER.info("saveLackGoodsBean 根据channelId={}无法查到goodsId", channelId);
			return Results.failure("数据异常");
		}

		AlarmLackGoodsBean alarmLackGoodsBean = new AlarmLackGoodsBean();
		alarmLackGoodsBean.setGoodsId(goodsId);
		alarmLackGoodsBean.setMachineCode(machineCode);
		alarmUtil.saveLackGoodsBean(alarmLackGoodsBean);

		return Results.success();
	}

	@Override
	public Result<Object> prepareLoginQrCode(StandardPrepareLoginReqVo req) {
		String machineCode = req.getMachineCode();
		String ext = req.getExt();
		Integer operType = req.getOperType();
		Integer loginType = req.getLoginType();
		String sessionUuid = machineCode;

		Inno72Machine inno72Machine = inno72MachineMapper.findMachineByCode(machineCode);

		String returnUrl = "";

		gameSessionRedisUtil.delSession(machineCode);

		if (StandardPrepareLoginReqVo.OperTypeEnum.CREATE_QRCODE.getKey() == operType) {
			// 生成二维码流程
			Inno72ChannelService channelService = (Inno72ChannelService)ApplicationContextHandle.getBean(StandardLoginTypeEnum.getValue(req.getLoginType()));
			String qrCOntent = channelService.buildQrContent(inno72Machine,sessionUuid,req);
			// 二维码存储在本地的路径
			String localUrl = "pre" + inno72Machine.getId() + sessionUuid +".png";
			returnUrl = qrCodeService.createQrCode(qrCOntent, localUrl);
		}
		// 开始会话流程
		try {
			this.startSession(inno72Machine, ext, sessionUuid);
			UserSessionVo userSessionVo =  new UserSessionVo(inno72Machine.getMachineCode());
			userSessionVo.setLoginType(req.getLoginType());
			userSessionVo.setChannelType(req.getLoginType());
		} catch(Exception e){
			LOGGER.error("prepareLoginQrCode",e);
			return Results.failure("系统异常");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("qrCodeUrl", returnUrl);
		map.put("sessionUuid", sessionUuid);
		return Results.success(map);
	}

	/**
	 * 开始会话
	 * @param inno72Machine
	 * @param ext
	 * @param sessionUuid
	 */
	private void startSession(Inno72Machine inno72Machine, String ext, String sessionUuid){
		UserSessionVo userSessionVo =  new UserSessionVo(inno72Machine.getMachineCode());
		userSessionVo.setMachineCode(inno72Machine.getMachineCode());
		userSessionVo.setMachineId(inno72Machine.getId());
		userSessionVo.setLogged(false);
		String activityId = inno72MachineService.findActivityIdByMachineCode(inno72Machine.getMachineCode());
		String rVoJson = redisUtil.get(CommonBean.REDIS_ACTIVITY_PLAN_CACHE_KEY +activityId+":"+ inno72Machine.getMachineCode());
		LOGGER.debug("redis cache machine data =====> {}", rVoJson);

		if (StringUtil.isEmpty(rVoJson)) {
			inno72MachineService.findGame(inno72Machine.getMachineCode(), "-1", "", "");
		}

		rVoJson = redisUtil.get(CommonBean.REDIS_ACTIVITY_PLAN_CACHE_KEY +activityId+":"+ inno72Machine.getMachineCode());

		Inno72MachineVo inno72MachineVo = JSON.parseObject(rVoJson, Inno72MachineVo.class);
		userSessionVo.setInno72MachineVo(inno72MachineVo);
		String planCode = FastJsonUtils.getString(rVoJson, "planCode");
		if (!StringUtil.isEmpty(planCode)) {
			userSessionVo.setPlanCode(planCode);
		}
		userSessionVo.setActivityId(inno72MachineVo.getActivityId());
		LOGGER.debug("parse rVoJson string finish --> {}", inno72MachineVo);

		// 解析 ext
		this.analysisExt(userSessionVo, ext);

		//设置新零售入会url
		//		if(userSessionVo.findPaiyangFlag()&&userSessionVo.getInno72MachineVo().getPaiyangType()==Inno72Interact.PAIYANG_TYPE_NEWRETAIL){
		//			initNewRetailMemberUrl(userSessionVo);
		//		}

		//		gameSessionRedisUtil.setSession(sessionUuid, JsonUtil.toJson(userSessionVo));

		// 设置15秒内二维码不能被扫
		gameSessionRedisUtil.setSessionEx(sessionUuid + "qrCode", sessionUuid, 15);

		LOGGER.info("goodsId {}, sellerId {}", userSessionVo.getGoodsId(), userSessionVo.getSellerId());
		if (StringUtil.isNotEmpty(userSessionVo.getGoodsId())) {
			pointService.innerPoint(JSON.toJSONString(userSessionVo), Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE.PRODUCT_CLICK);
		} else if (StringUtil.isNotEmpty(userSessionVo.getSellerId())) {
			pointService.innerPoint(JSON.toJSONString(userSessionVo), Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE.PRODUCT_CLICK);
		}
	}

	/**
	 * 设置新零售入会url
	 * @param userSessionVo
	 */
	private void initNewRetailMemberUrl(UserSessionVo userSessionVo,String callbackUrl) throws ApiException {
		String goodsId = userSessionVo.getGoodsId();
		//查找新零售sessionKey
		Inno72Merchant inno72Merchant = inno72MerchantMapper.findMerchantByByGoodsId(goodsId);
		//查找deviceCode
		Inno72MachineDevice device = inno72MachineDeviceService.findByMachineCodeAndSellerId(userSessionVo.getMachineCode(),inno72Merchant.getMerchantCode());
		//调用淘宝获取入会二维码url
		String url = inno72NewretailService.getStoreMemberurl(inno72Merchant.getSellerSessionKey(),device.getDeviceCode(),callbackUrl);
		userSessionVo.setNewRetailMemberUrl(url);

	}

	/**
	 * 解析ext
	 * @param userSessionVo
	 * @param ext
	 */
	private void analysisExt(UserSessionVo userSessionVo, String ext) {
		LOGGER.info("analysisExt is {}", ext);
		// 如果是派样商品的话，goodsCode 先保存到缓存里
		if (StringUtil.isNotEmpty(ext)) {
			String isVip = FastJsonUtils.getString(ext, "isVip");
			String itemId = FastJsonUtils.getString(ext, "itemId");
			String goodsCode = FastJsonUtils.getString(ext, "goodsCode");
			String sessionKey = FastJsonUtils.getString(ext, "sessionKey");
			String sellerId = FastJsonUtils.getString(ext, "sellerId");
			Inno72Merchant merchant = null;
			if (StringUtil.isNotEmpty(isVip)) {
				userSessionVo.setIsVip(isVip);
			}
			if (StringUtil.isNotEmpty(itemId)) {
				userSessionVo.setGoodsId(itemId);
				merchant = inno72MerchantMapper.findByGoodsId(itemId);
				if(merchant == null){
					merchant = inno72MerchantMapper.findByCoupon(itemId);
					if(merchant!=null){
						userSessionVo.setGoodsType(UserSessionVo.GOODSTYPE_COUPON);
						userSessionVo.setSellerName(merchant.getMerchantName());
					}
				} else {
					Inno72Goods inno72Goods = inno72GoodsMapper.selectByPrimaryKey(itemId);
					userSessionVo.setGoodsCode(inno72Goods.getCode());
					userSessionVo.setGoodsName(inno72Goods.getName());
				}
			}
			if (StringUtil.isNotEmpty(sessionKey)) {
				userSessionVo.setSessionKey(sessionKey);
			}
			if (StringUtil.isNotEmpty(goodsCode)) {
				userSessionVo.setGoodsCode(goodsCode);
				if(merchant == null) merchant = inno72MerchantMapper.findByGoodsCode(goodsCode);
				userSessionVo.setSellerName(merchant.getMerchantName());
			}
			if (StringUtil.isNotEmpty(sellerId)) {
				if(merchant == null)  {
					Inno72Merchant param = new Inno72Merchant();
					param.setMerchantCode(sellerId);
					param.setIsDelete(0);
					merchant = inno72MerchantMapper.selectOne(param);
					// userSessionVo.setSellerName(merchant.getMerchantName());
				}
			}
			if(merchant!=null){
				userSessionVo.setSellerId(merchant.getMerchantCode());
				userSessionVo.setMerchantName(merchant.getMerchantName());
				userSessionVo.setSellerName(merchant.getMerchantName());
				userSessionVo.setChannelName(merchant.getChannelName());
				userSessionVo.setChannelId(merchant.getChannelId());
				userSessionVo.setMerchantAccountName(merchant.getMerchantAccountName());
				userSessionVo.setMerchantAccountId(merchant.getMerchantId());
				userSessionVo.setChannelMerchantId(merchant.getId());
			}

		}
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

		// 保存用户游戏渠道 信息
		this.saveGameUserChannel(sessionUuid, channelId, inno72Activity, inno72ActivityPlan, inno72Game, inno72Machine);

		UserSessionVo sessionVo = new UserSessionVo(inno72Machine.getMachineCode(),machineId, null, null, null, gameId, sessionUuid,
				inno72ActivityPlan.getId());

		boolean canOrder = inno72GameService.countSuccOrder(channelId, sessionUuid, inno72ActivityPlan.getId(),inno72ActivityPlan.getActivityId());

		sessionVo.setUserId(sessionUuid); // 非第三方用户 使用 sessionUuid 作为userid
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
		// sessionVo.setRefOrderId(life.getId());

		//		gameSessionRedisUtil.setSession(sessionUuid, JSON.toJSONString(sessionVo));


		LOGGER.info("prepareLoginNologin output {} {}", sessionUuid, inno72Merchant.getMerchantCode());

		Map<String, Object> resultMap = new HashMap<>();

		resultMap.put("sessionUuid", sessionUuid);
		resultMap.put("sellerId", inno72Merchant.getMerchantCode());

		return Results.success(resultMap);

	}

	/**
	 * 保存用户游戏渠道 信息
	 */
	private void saveGameUserChannel(String sessionUuid, String channelId, Inno72Activity inno72Activity,
			Inno72ActivityPlan inno72ActivityPlan, Inno72Game inno72Game, Inno72Machine inno72Machine) {
		Map<String, String> userChannelParams = new HashMap<>();
		userChannelParams.put("channelId", channelId);
		userChannelParams.put("channelUserKey", sessionUuid);
		Inno72GameUserChannel userChannel = inno72GameUserChannelMapper.selectByChannelUserKey(userChannelParams);
		Inno72Channel inno72Channel = inno72ChannelMapper.selectByPrimaryKey(channelId);

		if (userChannel == null) {
			Inno72GameUser inno72GameUser = new Inno72GameUser();
			inno72GameUserMapper.insert(inno72GameUser);
			LOGGER.info("插入游戏用户表 完成 ===> {}", JSON.toJSONString(inno72GameUser));
			userChannel = new Inno72GameUserChannel(sessionUuid, "", channelId, inno72GameUser.getId(),
					inno72Channel.getChannelName(), sessionUuid, "",StandardLoginTypeEnum.ALIBABA.getValue());
			inno72GameUserChannelMapper.insert(userChannel);
			LOGGER.info("插入游戏用户渠道表 完成 ===> {}", JSON.toJSONString(userChannel));
		}

		this.startGameLife(userChannel, inno72Activity, inno72ActivityPlan, inno72Game, inno72Machine, sessionUuid);
	}


	/**
	 *
	 * @param mid mid
	 * @return List
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

	/**
	 * 掉货失败
	 * @param machineId machineId
	 * @param channelCode channelCode
	 * @param describtion describtion
	 * @return return
	 */
	@Override
	public Result<String> shipmentFail(String machineId, String channelCode, String describtion) {

		LOGGER.info("掉货失败参数 => machineId:{}; channelCode:{}; describtion:{}", machineId, channelCode, describtion);
		Inno72Machine inno72Machine = inno72MachineMapper.findMachineByCode(machineId);
		if (inno72Machine == null) {
			return Results.failure("机器号错误");
		}

		// todo 暂时注释掉 inno72Machine.getOpenStatus() == 0
		if (inno72Machine.getMachineStatus() == 4) {
			AlarmDropGoodsBean alarmDropGoodsBean = new AlarmDropGoodsBean();
			alarmDropGoodsBean.setChannelNum(channelCode);
			alarmDropGoodsBean.setMachineCode(machineId);
			alarmUtil.saveDropGoodsBean(alarmDropGoodsBean);
		}

		return Results.success();
	}

	/**
	 * 生成本次游戏记录
	 *
	 * @param userChannel userChannel
	 * @param inno72Activity inno72Activity
	 * @param inno72ActivityPlan inno72ActivityPlan
	 * @param inno72Game inno72Game
	 * @param inno72Machine inno72Machine
	 * @param userId userId
	 */
	private Inno72GameUserLife startGameLife(Inno72GameUserChannel userChannel, Inno72Activity inno72Activity,
			Inno72ActivityPlan inno72ActivityPlan, Inno72Game inno72Game, Inno72Machine inno72Machine, String userId) {
		Inno72Locale inno72Locale = inno72LocaleMapper.selectByPrimaryKey(inno72Machine.getLocaleId());
		Inno72GameUserLife life = new Inno72GameUserLife(userChannel == null ? null : userChannel.getGameUserId(),
				userChannel == null ? null : userChannel.getId(), inno72Machine.getMachineCode(),
				userChannel == null ? null : userChannel.getUserNick(), inno72ActivityPlan.getActivityId(),
				inno72Activity.getName(), inno72ActivityPlan.getId(), inno72Game.getId(), inno72Game.getName(),
				inno72Machine.getLocaleId(), inno72Locale == null ? "" : inno72Locale.getMall(), null, "", null, null,
				userId, "", "");
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

		Integer rep;
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

		return inno72Order.getId();

	}


	/**
	 * 下inno72订单，优惠券 or 商品
	 * @param channelId 渠道id
	 * @param activityPlanId 活动计划id
	 * @param machineId 机器id
	 * @param goodsId 商品id
	 * @param channelUserKey 用户主键
	 * @param product 类型
	 * @return 订单号
	 */
	private String genInno72Order(String sessionUuid, String channelId, String activityPlanId, String machineId, String goodsId,
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

		boolean b = inno72GameService.countSuccOrder(channelId, channelUserKey, activityPlanId,inno72ActivityPlan.getActivityId());
		Integer rep;
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

		String goodsName;

		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);

		if (product.getKey().equals(Inno72Order.INNO72ORDER_GOODSTYPE.PRODUCT.getKey())) {
			Inno72Goods inno72Goods = inno72GoodsMapper.selectByCode(goodsId);
			goodsName = inno72Goods.getName();
			orderGoods.setGoodsCode(inno72Goods.getCode());
			orderGoods.setGoodsId(inno72Goods.getId());
			orderGoods.setGoodsName(goodsName);
			orderGoods.setGoodsPrice(inno72Goods.getPrice());

			Inno72Shops inno72Shops = inno72ShopsMapper.selectByPrimaryKey(inno72Goods.getShopId());
			inno72Order.setShopsId(inno72Shops.getId());
			inno72Order.setShopsName(inno72Shops.getShopName());

			userSessionVo.setGoodsId(inno72Goods.getId());
			userSessionVo.setGoodsName(goodsName);
			userSessionVo.setGoodsCode(inno72Goods.getCode());

			/* 埋点 */
			CommonBean.logger(
					CommonBean.POINT_TYPE_GOODS_ORDER,
					inno72Machine.getMachineCode(),
					"用户[" + userChannel.getUserNick() + "], 生成["+ goodsName + "]订单，订单号[" + orderNum +"].",
					inno72ActivityPlan.getActivityId()+"|"+inno72Goods.getCode());

		} else {
			Inno72Coupon inno72Coupon = inno72CouponMapper.selectByPrimaryKey(goodsId);
			goodsName = inno72Coupon.getName();
			orderGoods.setGoodsCode(inno72Coupon.getCode());
			orderGoods.setGoodsId(inno72Coupon.getId());
			orderGoods.setGoodsName(goodsName);
			orderGoods.setGoodsPrice(BigDecimal.ZERO);
			Inno72Shops inno72Shops = inno72ShopsMapper.selectByPrimaryKey(inno72Coupon.getShopsId());
			inno72Order.setShopsId(inno72Shops.getId());
			inno72Order.setShopsName(inno72Shops.getShopName());

			userSessionVo.setInteractId(inno72Coupon.getCode());

			/* 埋点 */
			CommonBean.logger(
					CommonBean.POINT_TYPE_COUPON_ORDER,
					inno72Machine.getMachineCode(),
					"用户[" + userChannel.getUserNick() + "]生成["+ goodsName + "]订单，订单号[" + orderNum +"].",
					inno72ActivityPlan.getActivityId()+"|"+inno72Coupon.getCode());
		}

		orderGoods.setOrderNum(inno72Order.getOrderNum());
		orderGoods.setStatus(Inno72Order.INNO72ORDER_GOODSSTATUS.WAIT.getKey());
		inno72OrderMapper.insert(inno72Order);
		orderGoods.setOrderId(inno72Order.getId());
		inno72OrderGoodsMapper.insert(orderGoods);

		inno72OrderHistoryMapper.insert(new Inno72OrderHistory(inno72Order.getId(), inno72Order.getOrderNum(),
				JSON.toJSONString(inno72Order), "初始化插入订单!"));

		userSessionVo.setRefOrderStatus(inno72Order.getRefOrderStatus());
		//		gameSessionRedisUtil.setSession(sessionUuid, JSON.toJSONString(userSessionVo));

		return inno72Order.getId();
	}

	/**
	 * 下划线转驼峰
	 *
	 * @param jObject json Object
	 * @return 下划线转驼峰
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
				Map<String, String> channelParam = new HashMap<>();
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

				Inno72Merchant inno72Merchant = inno72MerchantMapper.selectByPrimaryKey(sampLingGoods.getSellerId());
				sampLingGoods.setSellerCode(inno72Merchant.getMerchantCode());

				// 根据商品id查询相关店铺信息
				Map<String, String> param = new HashMap<>();
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


	@Override
	public Result<String> setHeartbeat(String machineCode, String page, String planCode, String activity, String desc) {
		LOGGER.debug("setHeartbeat machineCode is {}, page is {}, planCode is {}, activity is {}, desc is {}",
				machineCode, page, planCode, activity, desc);
		Inno72Machine inno72Machine = inno72MachineMapper.findMachineByCode(machineCode);
		if (inno72Machine == null) {
			return Results.failure("此机器编码没有查到相对应的机器！");
		} else {
			LOGGER.debug("setHeartbeat inno72Machine is {}", JsonUtil.toJson(inno72Machine));
		}

		if (!(inno72Machine.getMachineStatus() == 4)) { // 4 表示机器状态正常
			LOGGER.debug("setHeartbeat 机器状态不正常 machineCode is {}", machineCode);
			return Results.failure("机器状态不正常");
		}

		String localeId = inno72Machine.getLocaleId();
		Inno72Locale inno72Locale = inno72LocaleMapper.selectByPrimaryKey(localeId);
		Integer monitor = inno72Locale.getMonitor();

		if (monitor == 1) {
			LOGGER.debug("setHeartbeat 当前机器不接收报警 monitor is {}", monitor);
			return Results.failure("当前机器不接收报警");
		}

		Map<String, String> remarkMap = new HashMap<>();
		remarkMap.put("planCode", planCode);
		remarkMap.put("activity", activity);
		remarkMap.put("desc", desc);

		AlarmDetailBean alarmDetailBean = new AlarmDetailBean();
		alarmDetailBean.setMachineId(inno72Machine.getId());
		alarmDetailBean.setType(1);
		alarmDetailBean.setPageInfo(page);
		alarmDetailBean.setRemark(JsonUtil.toJson(remarkMap));

		LOGGER.debug("setHeartbeat alarmDetailBean is {}", JsonUtil.toJson(alarmDetailBean));

		try {
			alarmUtil.saveAlarmDetail(alarmDetailBean);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Results.failure("设置心跳失败");
		}

		return Results.success();
	}

	@Override
	public Result<Object> newRetailmemberJoin(String sessionUuid, String sellSessionKey, String taobaoUserId, String meberJoinCallBackUrl){
		//查看是否入会
		try {
			Boolean vipflag = inno72NewretailService.getMemberIdentity(sellSessionKey,taobaoUserId);
			if(vipflag){
				//如果是会员
				return Results.success(1);
			}else{
				//如果不是会员获取入会的二维码url
				UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
				String goodsId = userSessionVo.getGoodsId();
				Inno72Merchant m = inno72MerchantMapper.findMerchantByByGoodsId(goodsId);
				//查询deviceCode
				Inno72MachineDevice device = inno72MachineDeviceService.findByMachineCodeAndSellerId(userSessionVo.getMachineCode(),m.getMerchantCode());
				String url = inno72NewretailService.getStoreMemberurl(sellSessionKey,device.getDeviceCode(),meberJoinCallBackUrl);
				try {
					String joinUrl = QrCodeUtil.readQrCode(url);
					return Results.success(joinUrl);
				}catch (Exception e){
					LOGGER.error("解析入会二维码错误url = {}",url);
					LOGGER.error("解析入会二维码错误",e);
					return Results.failure("解析入会二维码错误");
				}
			}
		} catch (ApiException e) {
			e.printStackTrace();
			LOGGER.error("newRetailmemberJoin",e);
			return Results.failure("调用淘宝失败");
		}
	}

}
