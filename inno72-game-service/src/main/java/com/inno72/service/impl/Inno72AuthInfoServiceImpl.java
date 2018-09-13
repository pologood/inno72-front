package com.inno72.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.inno72.common.util.*;
import com.inno72.mapper.*;
import com.inno72.model.*;
import com.inno72.service.Inno72GameService;
import com.inno72.service.Inno72TopService;
import com.inno72.vo.GoodsVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.CommonBean;
import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.json.JsonUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.oss.OSSUtil;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.Inno72AuthInfoService;
import com.inno72.vo.UserSessionVo;

import net.coobird.thumbnailator.Thumbnails;
import tk.mybatis.mapper.entity.Condition;

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
	private IRedisUtil redisUtil;
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

	// todo gxg 使用枚举
	private static final String QRSTATUS_NORMAL = "0"; // 二维码正常
	private static final String QRSTATUS_INVALID = "-1"; // 二维码失效
	private static final String QRSTATUS_EXIST_USER = "-2"; // 存在用户登录


	@Override
	public Result<Object> createQrCode(String machineId) {
		LOGGER.info("根据机器id生成二维码 machineCode is {} ", machineId);

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
		// 调用天猫的地址
		String url = inno72GameServiceProperties.get("tmallUrl") + _machineId + "/" + sessionUuid + "/" + env + "/?1=1"
				+ "&bluetoothAddAes=" + bluetoothAddAes + "&machineCode=" + machineCode;

		LOGGER.info("二维码字符串 {} ", url);

		// 二维码存储在本地的路径
		String localUrl = _machineId + sessionUuid + ".png";

		// 存储在阿里云上的文件名
		String objectName = "qrcode/" + localUrl;

		// 提供给前端用来调用二维码的地址
		String returnUrl = inno72GameServiceProperties.get("returnUrl") + objectName;

		try {
			boolean result = QrCodeUtil.createQrCode(localUrl, url, 1800, "png");
			if (result) {

				this.dealLocalQrCodeImg(localUrl, objectName);

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

		return Results.success(sessionStr);
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
	public Result<Object> createSamplingQrCode(String machineCode, String itemId, String isVip, String sessionKey) {
		LOGGER.info("根据机器code生成二维码 machineCode is {} ", machineCode);

		Inno72Machine inno72Machine = inno72MachineMapper.findMachineByCode(machineCode);
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
			return Results.failure(machineCode + "对应的 inno72Machine 不存在");
		}
		if (!StringUtil.isEmpty(machineCode)) {
			machineCode = AesUtils.encrypt(machineCode);
		}

		LOGGER.info("Mac蓝牙地址 {} ", bluetoothAddAes);

		// 生成sessionUuid
		String sessionUuid = UuidUtil.getUUID32();
		// 获取运行环境
		String env = getActive();
		// 调用天猫的地址
		// String testUrl =
		// "https://oauth.taobao.com/authorize?response_type=code&client_id=24952452&redirect_uri=http://inno72test.ews.m.jaeapp.com/api/samplingTop/";
		String url = inno72GameServiceProperties.get("tmallSamplingUrl") + _machineId + "/" + sessionUuid + "/" + env
				+ "/" + itemId + "/" + isVip + "/" + sessionKey + "/?1=1" + "&bluetoothAddAes=" + bluetoothAddAes
				+ "&machineCode=" + machineCode;

		LOGGER.info("二维码字符串 {} ", url);
		// 二维码存储在本地的路径
		String localUrl = _machineId + sessionUuid + ".png";

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
				map.put("env", env);
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
	public Result<Object> processBeforeLogged(String sessionUuid, String authInfo) {
		LOGGER.info("processBeforeLogged params sessionUuid is {}, authInfo is {} ", sessionUuid, authInfo);

		UserSessionVo sessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
		String mid = sessionVo.getMachineId();

		if (StringUtil.isEmpty(authInfo)) {
			return Results.failure("authInfo 不能为空！");
		}

		String accessToken = FastJsonUtils.getString(authInfo, "access_token");
		String userId = FastJsonUtils.getString(authInfo, "taobao_user_nick");

		if (StringUtil.isEmpty(accessToken)) {
			return Results.failure("accessToken 参数缺失！");
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

		String jstUrl = inno72GameServiceProperties.get("jstUrl");
		if (StringUtil.isEmpty(jstUrl)) {
			return Results.failure("配置中心无聚石塔配置路径!");
		}

		// 检查二维码是否可以重复扫
		String qrStatus = this.checkQrCode(sessionUuid);

		Inno72Activity inno72Activity = inno72ActivityMapper.selectByPrimaryKey(inno72ActivityPlan.getActivityId());
		String sellerId = inno72Activity.getSellerId();
		playCode = inno72Activity.getCode();
		LOGGER.info("sessionRedirect layCode is {}", playCode);

		Inno72Merchant inno72Merchant = inno72MerchantMapper.selectByPrimaryKey(sellerId);

		String nickName = inno72TopService.getMaskUserNick(sessionUuid, accessToken, inno72Merchant.getMerchantCode(), userId);

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

		// 检查当前机器下当前排期是否有商品
		boolean hasGoods = this.checkhasGoodsInMachine(inno72ActivityPlan.getId(), inno72Machine.getId());

//		UserSessionVo sessionVo = new UserSessionVo(mid, nickName, userId, accessToken, gameId, sessionUuid,
//				inno72ActivityPlan.getId());

		boolean b = inno72GameService.countSuccOrder(channelId, userId, inno72ActivityPlan.getId());

		sessionVo.setUserNick(nickName);
		sessionVo.setUserId(userId);
		sessionVo.setAccessToken(accessToken);
		sessionVo.setGameId(gameId);
		sessionVo.setSessionUuid(sessionUuid);
		sessionVo.setActivityPlanId(inno72ActivityPlan.getId());

		sessionVo.setCanOrder(b);
		sessionVo.setCountGoods(hasGoods);
		sessionVo.setChannelId(channelId);
		sessionVo.setActivityId(inno72Activity.getId());

		List<GoodsVo> list = loadGameInfo(mid);
		LOGGER.info("loadGameInfo is {} ", JsonUtil.toJson(list));
		sessionVo.setGoodsList(list);

		this.startGameLife(userChannel, inno72Activity, inno72ActivityPlan, inno72Game, inno72Machine, userId);

		LOGGER.info("playCode is" + playCode);

		Integer activityType = inno72Activity.getType();

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("machineCode", inno72Machine.getMachineCode());
		resultMap.put("playCode", playCode);
		resultMap.put("qrStatus", qrStatus);
		resultMap.put("sellerId", inno72Merchant.getMerchantCode());

		// 如果需要入会写入会信息
		String isVip = sessionVo.getIsVip();
		if (StringUtil.isNotEmpty(isVip) && sessionVo.equals("1")) {
			String goodsId = sessionVo.getGoodsId();
			Inno72Goods inno72Goods = inno72GoodsMapper.selectByPrimaryKey(goodsId);
			String goodsCode = inno72Goods.getCode();
			sessionVo.setGoodsCode(goodsCode);

			LOGGER.info("返回给聚石塔的入会信息 goodsCode is {}  isVip is {}, sessionKey is {}", goodsCode, sessionVo.getIsVip(), sessionVo.getSessionKey());

			resultMap.put("goodsCode", goodsCode);
			resultMap.put("isVip", sessionVo.getIsVip());
			resultMap.put("sessionKey", sessionVo.getSessionKey());
		}

		resultMap.put("activityType", activityType);
		resultMap.put("goodsCode", sessionVo.getGoodsCode() != null ? sessionVo.getGoodsCode() : "");

		LOGGER.info("processBeforeLogged返回聚石塔结果 is {}", resultMap);

		gameSessionRedisUtil.setSessionEx(sessionUuid, JSON.toJSONString(sessionVo));

		return Results.success(JSONObject.toJSONString(resultMap));
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
	private boolean checkhasGoodsInMachine(String platId, String machineId) {
		// 判断机器是否有商品
		Map<String, String> params = new HashMap<>(2);
		params.put("platId", platId);
		params.put("machineId", machineId);
		List<Integer> countGoods = inno72ActivityPlanGameResultMapper.selectCountGoods(params);
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
	 * 检查二维码是否可以重复扫
	 * @param sessionUuid
	 * @return
	 */
	private String checkQrCode(String sessionUuid) {
		// 判断是否有他人登录以及二维码是否过期
		String qrStatus = QRSTATUS_NORMAL;
		LOGGER.info("sessionUuid is {}", sessionUuid);
		// 判断二维码是否过期
		boolean result = gameSessionRedisUtil.hasKey(sessionUuid);
		LOGGER.info("qrCode hasKey result {} ", result);
		if (!result) {
			qrStatus = QRSTATUS_INVALID;
			LOGGER.info("二维码已经过期");
		} else {
			// 判断已经有用户操作
			UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
			if (userSessionVo.isLogged()) {
				qrStatus = QRSTATUS_EXIST_USER;
				LOGGER.info("已经有用户正在操作");
			}
		}
		return qrStatus;
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

	@Override
	public boolean setLogged(String sessionUuid) {
		boolean logged = false;
		try {
			UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
			userSessionVo.setLogged(true);
			gameSessionRedisUtil.setSessionEx(userSessionVo.getSessionUuid(), JSON.toJSONString(userSessionVo));
			logged = true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return logged;
	}
}
