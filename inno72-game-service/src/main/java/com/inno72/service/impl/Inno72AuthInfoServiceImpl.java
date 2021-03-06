package com.inno72.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.inno72.common.*;
import com.inno72.common.json.JsonUtil;
import com.inno72.common.util.DateUtil;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.*;
import com.inno72.model.*;
import com.inno72.oss.OSSUtil;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.*;
import com.inno72.vo.*;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CodeGenerator on 2018/06/27.
 */
@Service
@Transactional
public class Inno72AuthInfoServiceImpl implements Inno72AuthInfoService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72AuthInfoServiceImpl.class);

	@Resource
	private GameSessionRedisUtil gameSessionRedisUtil;

    @Resource
    private IRedisUtil redisUtil;
	@Resource
	private Inno72GameServiceProperties inno72GameServiceProperties;
	@Resource
	private Inno72MachineMapper inno72MachineMapper;
	@Resource
	private Inno72GameMapper inno72GameMapper;
	@Resource
	private Inno72GameUserMapper inno72GameUserMapper;
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
	private Inno72GameService inno72GameService;
	@Resource
	private Inno72LocaleMapper inno72LocaleMapper;
	@Resource
	private Inno72GameUserLifeMapper inno72GameUserLifeMapper;
	@Resource
	private Inno72ActivityShopsMapper inno72ActivityShopsMapper;
	@Resource
	private Inno72GoodsMapper inno72GoodsMapper;
	@Resource
	private Inno72TopService inno72TopService;
	@Resource
	private Inno72InteractService inno72InteractService;
	@Resource
	private Inno72InteractGoodsService inno72InteractGoodsService;
	@Resource
	private Inno72MachineService inno72MachineService;
	@Resource
	private Inno72ShopsMapper inno72ShopsMapper;
	@Resource
	private Inno72CouponMapper inno72CouponMapper;
	@Resource
	private RedisTemplate redisTemplate;
	@Resource
	private Inno72ConnectionService inno72ConnectionService;

	@Resource
	private PointService pointService;

	// todo gxg 使用枚举
	private static final String QRSTATUS_NORMAL = "0"; // 二维码正常
	private static final String QRSTATUS_INVALID = "-1"; // 二维码失效
	private static final String QRSTATUS_EXIST_USER = "-2"; // 存在用户登录


	/**
	 * 处理本地生成二维码
	 */
	private void dealLocalQrCodeImg(String localUrl, String objectName) {
		try {
			File f = new File(localUrl);
			if (f.exists()) {
				// 压缩图片
				Thumbnails.of(localUrl).scale(0.5f).outputQuality(0f).toFile(localUrl);
				// 上传阿里云
				OSSUtil.uploadLocalFile(localUrl, objectName);
				// 删除本地文件
				f.delete();
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	@Override
	public Result<Object> sessionPolling(String sessionUuid) {
		if (StringUtils.isEmpty(sessionUuid)) {
			return Results.failure("参数缺失！");
		}

		UserSessionVo sessionStr = gameSessionRedisUtil.getSessionKey(sessionUuid);

		if (sessionStr == null) {
			return Results.failure("未登录！");
		}

		Long scard = redisUtil.scard(CommonBean.REDIS_ACTIVITY_PLAN_LOGIN_TIMES_KEY + sessionStr.getActivityPlanId());
		sessionStr.setPlayTimes(scard);
        sessionStr.setInno72MachineVo(null);

		return Results.success(sessionStr);
	}

	@Override
	public Result<Object> processBeforeLogged(String sessionUuid, String authInfo, String traceId) {
		LOGGER.info("processBeforeLogged params sessionUuid is {}, authInfo is {}, traceId is {} ", sessionUuid, authInfo, traceId);

		UserSessionVo sessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
		if (sessionVo == null) {
			return Results.failure("sessionUuid 不存在!");
		}

		LOGGER.info("processBeforeLogged userSessionVo is {}", sessionVo);

		if (sessionVo.getLogged()) {
			return Results.failure("sessionUuid 已经存在登录用户");
		}

		String mid = sessionVo.getMachineId();
		Inno72MachineVo inno72MachineVo = sessionVo.getInno72MachineVo();
        if(inno72MachineVo == null){
            LOGGER.error("inno72MachineVo 为空！sessionUuid={}",sessionUuid);
            return Results.failure("inno72MachineVo 为空！");
        }

		if (StringUtil.isEmpty(authInfo)) {
			return Results.failure("authInfo 不能为空！");
		}

		String channelType = FastJsonUtils.getString(authInfo, "channelType");

		if (StringUtil.isEmpty(channelType)) {
			channelType = "0";
		}
		sessionVo.setChannelType(Integer.parseInt(channelType));
        if(inno72MachineVo.getActivityType() == Inno72MachineVo.ACTIVITYTYPE_PAIYANG){
            return paiYangProcessBeforeLogged(sessionUuid,sessionVo,authInfo,traceId);
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

		if (StringUtil.isEmpty(gameId)) {
			return Results.failure("没有绑定的游戏！");
		}

		Inno72Game inno72Game = inno72GameMapper.selectByPrimaryKey(gameId);
		if (inno72Game == null) {
			return Results.failure("不存在的游戏！");
		}

		String jstUrl = inno72GameServiceProperties.get("jstUrl");
		if (StringUtil.isEmpty(jstUrl)) {
			return Results.failure("配置中心无聚石塔配置路径!");
		}

		Inno72Activity inno72Activity = inno72ActivityMapper.selectByPrimaryKey(inno72ActivityPlan.getActivityId());

		Inno72Merchant inno72Merchant;
		String merchantCode = sessionVo.getSellerId();
		if (!StringUtil.isEmpty(merchantCode)) {
			Map<String, Object> params = new HashMap<>();
			params.put("activityId", inno72Activity.getId());
			params.put("merchantCode", merchantCode);
			inno72Merchant = inno72MerchantMapper.findMerchantByMap(params);
		} else {
			inno72Merchant = inno72MerchantMapper.findMerchantByActivityId(inno72Activity.getId());
			sessionVo.setSellerId(inno72Merchant.getMerchantCode());
		}

		sessionVo.setMerchantAccountId(inno72Merchant.getMerchantAccountId());
		sessionVo.setMerchantAccountName(inno72Merchant.getMerchantAccountName());
		sessionVo.setChannelMerchantId(inno72Merchant.getId());
		sessionVo.setChannelId(inno72Merchant.getChannelId());
		sessionVo.setChannelName(inno72Merchant.getChannelName());

		playCode = inno72Activity.getCode();
		LOGGER.info("sessionRedirect layCode is {}", playCode);

		String accessToken = null;
		String userId = null;
		String nickName = null;

		String channelT = FastJsonUtils.getString(authInfo, "channelType");
		if (StringUtil.notEmpty(channelT) && channelT.equals("2")){
			LOGGER.info("淘宝用户流程 authInfo - {}", authInfo);
			userId = FastJsonUtils.getString(authInfo, "openId");
			accessToken = FastJsonUtils.getString(authInfo, "code");
			nickName = FastJsonUtils.getString(authInfo, "nickname");
			sessionVo.setScanned(true);
		}else{
			accessToken = FastJsonUtils.getString(authInfo, "access_token");
			userId = FastJsonUtils.getString(authInfo, "taobao_user_nick");
			nickName = inno72TopService.getMaskUserNick(sessionUuid, accessToken, inno72Merchant.getMerchantCode(), userId);
		}
		if (StringUtil.isEmpty(accessToken)) {
			return Results.failure("accessToken 参数缺失！");
		}

		// 设置统计每个计划的已完次数
		redisUtil.sadd(CommonBean.REDIS_ACTIVITY_PLAN_LOGIN_TIMES_KEY + inno72ActivityPlan.getId(), userId);

		String channelId = inno72Merchant.getChannelId();

		Inno72Channel inno72Channel = inno72ChannelMapper.selectByPrimaryKey(channelId);

		Map<String, String> userChannelParams = new HashMap<>();
		userChannelParams.put("channelId", channelId);
		userChannelParams.put("channelUserKey", userId);
		Inno72GameUserChannel userChannel = inno72GameUserChannelMapper.selectByChannelUserKey(userChannelParams);

		if (userChannel == null) {
			Inno72GameUser inno72GameUser = new Inno72GameUser();
			inno72GameUserMapper.insert(inno72GameUser);
			LOGGER.info("新增插入游戏用户表 完成 ===> {}", JSON.toJSONString(inno72GameUser));
			userChannel = new Inno72GameUserChannel(nickName, "", channelId, inno72GameUser.getId(),
					inno72Channel.getChannelName(), userId, accessToken,StandardLoginTypeEnum.WEIXIN.getValue());
			inno72GameUserChannelMapper.insert(userChannel);
			LOGGER.info("新增插入游戏用户渠道表 完成 ===> {}", JSON.toJSONString(userChannel));

		} else {
			userChannel.setAccessToken(accessToken);
			inno72GameUserChannelMapper.updateByPrimaryKey(userChannel);
		}

		this.checkGoodsId(sessionVo);

		// 检查当前机器下当前排期是否有商品
		boolean hasGoods = this.checkhasGoodsInMachine(inno72ActivityPlan.getId(), inno72Machine.getId(), inno72Activity.getType(), sessionVo);
		LOGGER.info("hasGoods is {}", hasGoods);

//		UserSessionVo sessionVo = new UserSessionVo(mid, nickName, userId, accessToken, gameId, sessionUuid,
//				inno72ActivityPlan.getId());

		boolean canOrder = false;

		if (inno72Activity.getType().equals(Inno72Activity.ActivityType.PAIYANG.getType())) {
			canOrder = inno72GameService.countSuccOrderPy(channelId, userId, inno72ActivityPlan.getId(), sessionVo.getGoodsId(), inno72Activity.getId());
		} else if (inno72Activity.getType().equals(Inno72Activity.ActivityType.COMMON.getType())) {
			canOrder = inno72GameService.countSuccOrder(channelId, userId,inno72ActivityPlan.getId(), inno72ActivityPlan.getActivityId());
		}

		// canOrder = inno72GameService.countSuccOrder(channelId, userId, inno72ActivityPlan.getId());
		sessionVo.setActivityType(inno72Activity.getType());
		sessionVo.setUserNick(nickName);
		sessionVo.setUserId(userId);
		sessionVo.setAccessToken(accessToken);
		sessionVo.setGameId(gameId);
		sessionVo.setSessionUuid(sessionUuid);
		sessionVo.setActivityPlanId(inno72ActivityPlan.getId());

		sessionVo.setCanOrder(canOrder);
		sessionVo.setCountGoods(hasGoods);
		sessionVo.setChannelId(channelId);
		sessionVo.setActivityId(inno72Activity.getId());

		List<GoodsVo> list = loadGameInfo(mid);
		LOGGER.info("loadGameInfo is {} ", JsonUtil.toJson(list));
		sessionVo.setGoodsList(list);

		this.startGameLife(userChannel, inno72Activity, inno72ActivityPlan, inno72Game, inno72Machine, userId,
				inno72Merchant.getMerchantCode(),
				sessionVo.getGoodsCode() == null ? "" : sessionVo.getGoodsCode());

		LOGGER.info("playCode is" + playCode);

		Integer activityType = inno72Activity.getType();

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("machineCode", inno72Machine.getMachineCode());
		resultMap.put("playCode", playCode);
		resultMap.put("qrStatus", QRSTATUS_NORMAL);
		resultMap.put("sellerId", inno72Merchant.getMerchantCode());

		this.dealIsVip(resultMap, sessionVo);
//		this.dealFollowSessionKey(resultMap, sessionVo);

		resultMap.put("activityType", activityType);
		resultMap.put("goodsCode", sessionVo.getGoodsCode() != null ? sessionVo.getGoodsCode() : "");

		resultMap.put("traceId", traceId);

		LOGGER.info("processBeforeLogged返回聚石塔结果 is {}", resultMap);

//		gameSessionRedisUtil.setSession(sessionUuid, JSON.toJSONString(sessionVo));

		CommonBean.logger(
				CommonBean.POINT_TYPE_LOGIN,
				inno72Machine.getMachineCode(),
				"用户" + nickName + "，登录机器 ["+inno72Machine.getMachineCode()+"], 当前活动 ["+ inno72Activity.getName() +"]",
				inno72Activity.getId()+"|"+userId);

		return Results.success(resultMap);
	}

	private Result<Object> paiYangProcessBeforeLogged(String sessionUuid, UserSessionVo sessionVo, String authInfo,String traceId) {

		Inno72ChannelService channelService = (Inno72ChannelService)ApplicationContextHandle.getBean(StandardLoginTypeEnum.getValue(sessionVo.getChannelType()));
		return channelService.paiYangProcessBeforeLogged(sessionUuid,sessionVo,authInfo,traceId);
	}
	/**
	 * 获取活动商品展示个数
	 * @param gameId
	 * @return
	 */
	private Integer findIntentGoodsSize(String gameId) {
		//查找对应的游戏里面的显示条数
		Inno72Game game = inno72GameService.findById(gameId);
		return game.getMaxGoodsNum();
	}
	@Override
	public boolean findCanOrder(Inno72Interact interact, UserSessionVo sessionVo, String userId){
		boolean canOrder = true;
		Integer times = interact.getTimes(); //同一用户参与活动次数
		Integer dayTimes = interact.getDayTimes();//同一用户每天参与活动次数
		Integer number = interact.getNumber();//同一用户获得商品次数
		Integer dayNumber = interact.getDayNumber();//同一用户每天获得商品次数
		//获取这个商品的限制个数
		Integer userDayNumber = inno72InteractGoodsService.findByInteractIdAndGoodsId(interact.getId(),sessionVo.getGoodsId()).getUserDayNumber();
		//获取展示商品的个数
		Integer goodsSize = findIntentGoodsSize(interact.getGameId());
		//设置canOrder
		String key = null;
		Integer timesTmp = 0;
		if(times!=null&&times!=-1){
			key = String.format(RedisConstants.PAIYANG_ORDER_TIMES,interact.getId(),userId);
			if(redisUtil.exists(key)){
				Integer mytimes = Integer.parseInt(redisUtil.get(key));
				timesTmp = times*goodsSize;
				if(mytimes >= timesTmp){
					LOGGER.info("限制：interact.getTimes={},goodsSize={},mytimes={}",times,goodsSize,mytimes);
					canOrder = false;
				}
			}
		}
		String date = DateUtil.getDateStringByYYYYMMDD();
		if(dayTimes!=null&&dayTimes!=-1){
			key = String.format(RedisConstants.PAIYANG_DAY_ORDER_TIMES,interact.getId(),date,userId);
			if(redisUtil.exists(key)){
				timesTmp = dayTimes*goodsSize;
				Integer mydayTimes = Integer.parseInt(redisUtil.get(key));
				if(mydayTimes >= timesTmp){
					LOGGER.info("限制：interact.getDayTimes={},goodsSize={},mydayTimes={}",dayTimes,goodsSize,mydayTimes);
					canOrder = false;
				}
			}
		}



		if(number!=null&&number!=-1){
			key = String.format(RedisConstants.PAIYANG_ORDER_TIMES,interact.getId(),userId);
			if(redisUtil.exists(key)){
				Integer mytimes = Integer.parseInt(redisUtil.get(key));
				if(mytimes >= number){
					LOGGER.info("限制：interact.getNumber={},myNumber={}",number,mytimes);
					canOrder = false;
				}
			}
		}

		if(dayNumber!=null&&dayNumber != -1){
			if(redisUtil.exists(String.format(RedisConstants.PAIYANG_DAY_ORDER_TIMES,interact.getId(),date,userId))){
				Integer mydayTimes = Integer.parseInt(redisUtil.get(String.format(RedisConstants.PAIYANG_DAY_ORDER_TIMES,interact.getId(),date,userId)));
				if(mydayTimes >= dayNumber){
					LOGGER.info("限制：dayNumber={},myDayNumber={}",dayNumber,mydayTimes);
					canOrder = false;
				}
			}
		}

		if(userDayNumber!=null&&userDayNumber!=-1){
			key = String.format(RedisConstants.PAIYANG_GOODS_ORDER_TIMES,interact.getId(),sessionVo.getGoodsId(),date,userId);
			if(redisUtil.exists(key)){
				Integer mydayTimes = Integer.parseInt(redisUtil.get(key));
				if(mydayTimes >= userDayNumber){
					LOGGER.info("限制：userDayNumber={},myUserDayNumber={}",userDayNumber,mydayTimes);
					canOrder = false;
				}
			}
		}
		return canOrder;
	}

	/**
	 * 检查goodsid
	 */
	private void checkGoodsId(UserSessionVo sessionVo) {
		if (StringUtil.isEmpty(sessionVo.getGoodsId()) && StringUtil.isNotEmpty(sessionVo.getGoodsCode())) {
			Inno72Goods inno72Goods = inno72GoodsMapper.selectByCode(sessionVo.getGoodsCode());
			sessionVo.setGoodsId(inno72Goods.getId());
		}
	}

	/**
	 * 处理关注sessionkey
	 */
	void dealFollowSessionKey(Map<String, Object> resultMap, UserSessionVo sessionVo) {

		String goodsId = sessionVo.getGoodsId();
		String goodsCode = sessionVo.getGoodsCode();

		LOGGER.info("dealFollowSessionKey goodsId is {}, goodsCode is {}", goodsId, goodsCode);
		if (StringUtil.isEmpty(goodsId) && StringUtil.isEmpty(goodsCode)) {
			resultMap.put("followSessionKey", "");
		} else {
			Inno72Goods inno72Goods = null;
			if (StringUtil.isNotEmpty(goodsId)) {
				inno72Goods = inno72GoodsMapper.selectByPrimaryKey(goodsId);
			} else if (StringUtil.isNotEmpty(goodsCode)) {
				inno72Goods = inno72GoodsMapper.selectByCode(goodsCode);
			}
			String shopId = inno72Goods.getShopId();
			Inno72Shops inno72Shops = inno72ShopsMapper.selectByPrimaryKey(shopId);
			resultMap.put("followSessionKey", inno72Shops.getFocusSessionKey());
		}

	}

	/**
	 * 处理是否入会
	 */
	void dealIsVip(Map<String, Object> resultMap, UserSessionVo sessionVo) {
		LOGGER.info("dealIsVip params sessionVo is {}", sessionVo);
		// 如果需要入会写入会信息
		String isVip = sessionVo.getIsVip();
		if (StringUtil.isNotEmpty(isVip) && sessionVo.getIsVip().equals("1")) {
			String goodsId = sessionVo.getGoodsId();
			if (!StringUtil.isEmpty(goodsId)) {
				if(sessionVo.getGoodsType()!=null && UserSessionVo.GOODSTYPE_COUPON.compareTo(sessionVo.getGoodsType())==0 ){
					Inno72Coupon inno72Coupon = inno72CouponMapper.selectByPrimaryKey(goodsId);
					sessionVo.setGoodsCode(inno72Coupon.getCode());
				}else{
					Inno72Goods inno72Goods = inno72GoodsMapper.selectByPrimaryKey(goodsId);
					String goodsCode = inno72Goods.getCode();
					sessionVo.setGoodsCode(goodsCode);
				}
			}
			String goodsCode = sessionVo.getGoodsCode();
			LOGGER.info("返回给聚石塔的入会信息 goodsCode is {}  isVip is {}, sessionKey is {}", goodsCode, sessionVo.getIsVip(), sessionVo.getSessionKey());
			resultMap.put("goodsCode", goodsCode);
			resultMap.put("isVip", sessionVo.getIsVip());
			resultMap.put("sessionKey", sessionVo.getSessionKey());
		}
	}

	/**
	 * 校验是否入会(暂时没用到)
	 */
	private void checkInMemember(Map<String, Object> resultMap, UserSessionVo sessionVo, String activityId) {
		String goodsCode = sessionVo.getGoodsCode();

		Inno72Goods inno72Goods = inno72GoodsMapper.selectByCode(goodsCode);

		if (StringUtil.isNotEmpty(goodsCode)) {
			Condition condition = new Condition(Inno72ActivityShops.class);
			condition.createCriteria().andEqualTo("activityId", activityId);
			condition.createCriteria().andEqualTo("shopsId", inno72Goods.getShopId());
			List<Inno72ActivityShops> inno72ActivityShops = inno72ActivityShopsMapper.selectByCondition(condition);
			LOGGER.info("checkInMemember inno72ActivityShops is {}", inno72ActivityShops);
			if (CollectionUtils.isNotEmpty(inno72ActivityShops)) {
				Inno72ActivityShops _inno72ActivityShops = inno72ActivityShops.get(0);
				Integer isVip = _inno72ActivityShops.getIsVip();
				String sessionKey = _inno72ActivityShops.getSessionKey();
				LOGGER.info("checkInMemember isVip is {}, sessionKey is {}", isVip, sessionKey);
				resultMap.put("isVip", isVip);
				resultMap.put("sessionKey", sessionKey);
			}
		}
	}

	/**
	 * 检查当前机器下当前排期是否有商品
	 * @return
	 */
	private boolean checkhasGoodsInMachine(String platId, String machineId, int type, UserSessionVo sessionVo) {
		LOGGER.info("checkhasGoodsInMachine type is {}", type);
		List<Integer> countGoods = new ArrayList<>();

		// 判断机器是否有商品
		Map<String, String> params = new HashMap<>(2);
		params.put("platId", platId);
		params.put("machineId", machineId);
		if (type == Inno72Activity.ActivityType.PAIYANG.getType()) {
			params.put("goodsId", sessionVo.getGoodsId() != null ? sessionVo.getGoodsId() : "");
			countGoods = inno72ActivityPlanGameResultMapper.selectCountGoodsPy(params);
		} else if (type == Inno72Activity.ActivityType.COMMON.getType()) {
			countGoods = inno72ActivityPlanGameResultMapper.selectCountGoods(params);
		}

		boolean hasGoods = true;
		if (countGoods.size() == 0) {
			hasGoods = false;
		}
		for (Integer count : countGoods) {
			if (count < 1) {
				hasGoods = false;
				break;
			}
		}
		return hasGoods;
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
			Inno72ActivityPlan inno72ActivityPlan, Inno72Game inno72Game, Inno72Machine inno72Machine, String userId, String merchantCode, String goodsCode) {
		Inno72Locale inno72Locale = inno72LocaleMapper.selectByPrimaryKey(inno72Machine.getLocaleId());
		Inno72GameUserLife life = new Inno72GameUserLife(userChannel == null ? null : userChannel.getGameUserId(),
				userChannel == null ? null : userChannel.getId(), inno72Machine.getMachineCode(),
				userChannel == null ? null : userChannel.getUserNick(), inno72ActivityPlan.getActivityId(),
				inno72Activity.getName(), inno72ActivityPlan.getId(), inno72Game.getId(), inno72Game.getName(),
				inno72Machine.getLocaleId(), inno72Locale == null ? "" : inno72Locale.getMall(), null, "", null, null,
				userId, merchantCode == null ? "" : merchantCode, goodsCode == null ? "" : goodsCode);
		LOGGER.info("插入用户游戏记录 ===> {}", JSON.toJSONString(life));
		inno72GameUserLifeMapper.insert(life);
		return life;

	}
	private void sendMsg(String sessionUuid,Integer type) {
		LOGGER.info("sendMsg sessionUuid={},type={}",sessionUuid,type);
		UserSessionVo sessionVo = new UserSessionVo(sessionUuid);
		Long version = System.currentTimeMillis();
		String machineCode = sessionVo.getMachineCode();
		String activityId = sessionVo.getActivityId();
		Inno72ConnectionLoginVo data = new Inno72ConnectionLoginVo();
		data.setActivityId(activityId);
		data.setMachineCode(machineCode);
		data.setVersion(version);
		data.setCanOrder(sessionVo.getCanOrder());
		data.setCountGoods(sessionVo.getCountGoods());
		data.setUserNick(sessionVo.getUserNick());
		data.setType(type);
		data.setUserId(sessionVo.getGameUserId());
		inno72ConnectionService.send(machineCode,activityId,version,type,JsonUtil.toJson(data));
	}
	@Override
	public boolean setLogged(String sessionUuid) {
		boolean logged = false;
		try {
			UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
			if (!userSessionVo.getLogged()) {
				userSessionVo.setLogged(true);
				logged = true;
				pointService.innerPoint(JSON.toJSONString(userSessionVo), Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE.LOGIN);
				sendMsg(sessionUuid,Inno72MachineConnectionMsg.TYPE_ENUM.LOGIN.getKey());
			} else {
				LOGGER.info("sessionUuid {} 用户已经登录，不能继续登录！", sessionUuid);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return logged;
	}

	@Override
	public void setFollowed(String sessionUuid) {
		try {
			UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
			userSessionVo.setFllowed(true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
