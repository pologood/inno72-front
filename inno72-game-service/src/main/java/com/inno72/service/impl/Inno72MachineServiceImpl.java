package com.inno72.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.inno72.common.datetime.LocalDateTimeUtil;
import com.inno72.common.util.AesUtils;
import com.inno72.mapper.*;
import com.inno72.model.*;
import com.inno72.service.Inno72InteractMachineTimeService;
import com.inno72.service.Inno72InteractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.common.CommonBean;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.utils.StringUtil;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.Inno72MachineService;
import com.inno72.vo.Inno72MachineVo;

/**
 * Created by CodeGenerator on 2018/06/27.
 */
@Service
@Transactional
public class Inno72MachineServiceImpl extends AbstractService<Inno72Machine> implements Inno72MachineService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72MachineServiceImpl.class);

	@Resource
	private Inno72MachineMapper inno72MachineMapper;
	@Resource
	private Inno72GameMapper inno72GameMapper;
	@Resource
	private Inno72ActivityPlanMapper inno72ActivityPlanMapper;
	@Resource
	private Inno72MerchantMapper inno72MerchantMapper;
	@Resource
	private Inno72ActivityMapper inno72ActivityMapper;
	@Resource
	private IRedisUtil redisUtil;
	@Autowired
	private Inno72InteractMachineTimeService inno72InteractMachineTimeService;
	@Autowired
	private Inno72InteractService inno72InteractService;

	@Autowired
	private Inno72SupplyChannelMapper inno72SupplyChannelMapper;
//	@Resource
//	private InteractMachineTimeService interactMachineTimeService;


	@Override
	public Result<Inno72MachineVo> findGame(String machineId, String planId, String version, String versionInno72) {
		LOGGER.debug("查询售卖机游戏详情 - machineCode -> {}", machineId);

		Result<Inno72MachineVo> inno72MachineVoResult = this.initVoFromRedis(planId, machineId);
		if (inno72MachineVoResult.getCode() == Result.FAILURE) {
			return inno72MachineVoResult;
		}
		if (inno72MachineVoResult.getCode() == 2) {
			inno72MachineVoResult = this.initDefaultGameFromRedis(machineId);
			if (inno72MachineVoResult.getCode() != Result.SUCCESS) {
				return inno72MachineVoResult;
			}
		}

		Inno72MachineVo inno72MachineVo = inno72MachineVoResult.getData();

		if(inno72MachineVo.getActivityType() == Inno72MachineVo.ACTIVITYTYPE_PAIYANG){
			//查找机器活动有没有到期
			Inno72InteractMachine interactMachine = inno72InteractMachineTimeService.findInteractMachine(inno72MachineVo.getActivityId(),machineId);
			Inno72InteractMachineTime time = inno72InteractMachineTimeService.findActiveTimeByInteractMachineId(interactMachine.getId());
			//查询日期是否到期
			if (time == null) {
				LOGGER.info("活动过期 ==>   ", JSON.toJSONString(interactMachine));
				redisUtil.del(CommonBean.REDIS_ACTIVITY_PLAN_CACHE_KEY +inno72MachineVo.getActivityPlanId()+":"+ machineId);
				inno72MachineVo.setReload(true);
			}

			if (!planId.equals("-1") && (!inno72MachineVo.getActivityPlanId().equals(planId) || !inno72MachineVo
					.getInno72Games().getVersion().equals(version) || !inno72MachineVo.getInno72Games().getVersionInno72()
					.equals(versionInno72))) {
				LOGGER.debug("查询机器游戏关联完成 - result -> {}", JSON.toJSONString(inno72MachineVo));
				inno72MachineVo.setReload(true);
			}
		}else{
			if (!planId.equals("-1") && (!inno72MachineVo.getActivityPlanId().equals(planId) || !inno72MachineVo
					.getInno72Games().getVersion().equals(version) || !inno72MachineVo.getInno72Games().getVersionInno72()
					.equals(versionInno72))) {
				LOGGER.debug("查询机器游戏关联完成 - result -> {}", JSON.toJSONString(inno72MachineVo));
				inno72MachineVo.setReload(true);
			}

			Inno72ActivityPlan inno72ActivityPlan = inno72MachineVo.getInno72ActivityPlan();
			if (inno72ActivityPlan != null) {
				LocalDateTime startTime = inno72ActivityPlan.getStartTime();
				LocalDateTime endTime = inno72ActivityPlan.getEndTime();

				LocalDateTime now = LocalDateTime.now();
				if (!(endTime.isAfter(now) && startTime.isBefore(now))) {
					LOGGER.info("活动过期 ==>   ", JSON.toJSONString(inno72ActivityPlan));
					redisUtil.del(CommonBean.REDIS_ACTIVITY_PLAN_CACHE_KEY  +inno72MachineVo.getActivityPlanId()+":"+ machineId);
					inno72MachineVo.setReload(true);
				}
			}
		}

		return Results.success(inno72MachineVo);
	}


	@Override
	public Integer getMachineGoodsCount(String goodsId, String machineId) {
		// 根据商品id查询货道
		Map<String, String> channelParam = new HashMap<String, String>();
		channelParam.put("goodId", goodsId);
		channelParam.put("machineId", machineId);
		List<Inno72SupplyChannel> inno72SupplyChannels = inno72SupplyChannelMapper
				.selectByGoodsId(channelParam);

		Integer goodsCount = 0;
		if (inno72SupplyChannels != null && inno72SupplyChannels.size() > 0) {
			// 所有具有相同商品id的货道中中道商品数量相加
			for (Inno72SupplyChannel channel : inno72SupplyChannels) {
				goodsCount += channel.getGoodsCount();
			}
		}
		return goodsCount;
	}

	private Result<Inno72MachineVo> initVoFromRedis(String planId, String machineId) {

		Inno72MachineVo inno72MachineVo = null;

		if (StringUtil.isNotBlank(planId)) {
			String rVoJson = redisUtil.get(CommonBean.REDIS_ACTIVITY_PLAN_CACHE_KEY +  planId + ":" + machineId);
			LOGGER.debug("redis cache machine data =====> {}", rVoJson);
			if (StringUtil.isNotEmpty(rVoJson)) {
				inno72MachineVo = JSON.parseObject(rVoJson, Inno72MachineVo.class);
				LOGGER.debug("parse rVoJson string finish --> {}", inno72MachineVo);
			}
		}

		if (inno72MachineVo == null) {

			inno72MachineVo = new Inno72MachineVo();

			Inno72Machine inno72Machine = inno72MachineMapper.findMachineByCode(machineId);
			if (inno72Machine == null) {
				return Results.failure("机器code错误!");
			}

			List<Inno72ActivityPlan> inno72ActivityPlans = inno72ActivityPlanMapper
					.selectByMachineId(inno72Machine.getId());
			boolean paiyangFlag = false;
			Inno72Interact interact = null;
			if (inno72ActivityPlans == null || inno72ActivityPlans.size() == 0) {
				LOGGER.debug("机器 【{}】 没有配置 活动计划!查询派样", inno72Machine.getMachineCode());

				//查看派样
				//查询该机器配置的商品信息（当前时间在起止时间内的）
				Inno72InteractMachine interactMachine = inno72InteractMachineTimeService.findActiveInteractMachine(machineId);
				if(interactMachine == null){
					LOGGER.info("此机器无派样活动配置machineCode={}",machineId);
					return Results.warn("无活动计划!", 2);
				}
				String interactId = interactMachine.getInteractId();
				interact = inno72InteractService.findById(interactId);
				if(interact == null){
					return Results.warn("无活动计划!", 2);
				}

				paiyangFlag = true;
			}
			if(paiyangFlag){
				String gameId = interact.getGameId();
				if (StringUtil.isEmpty(gameId)) {
					LOGGER.debug("派样活动interactId= 【{}】 没有配置 游戏!", interact.getId());
					return Results.failure("无配置游戏!");
				}
				//查找游戏配置
				Inno72Game inno72Game = inno72GameMapper.selectByPrimaryKey(gameId);
				if (inno72Game == null) {
					LOGGER.debug("游戏id 【{}】 不存在!", gameId);
					return Results.failure("游戏不存在!");
				}

				BeanUtils.copyProperties(inno72Machine, inno72MachineVo);
				inno72MachineVo.setInno72Games(inno72Game);
				inno72MachineVo.setPlanCode(interact.getPlanCode());
				inno72MachineVo.setActivityId(interact.getId());
				inno72MachineVo.setActivityPlanId(interact.getId());
				inno72MachineVo.setActivityType(Inno72MachineVo.ACTIVITYTYPE_PAIYANG);
				inno72MachineVo.setPaiyangType(interact.getPaiyangType());
				inno72MachineVo.setActivityName(interact.getName());
			}else{
				Inno72ActivityPlan inno72ActivityPlan = inno72ActivityPlans.get(0);
				LOGGER.debug("活动计划详情 =====> {}", JSON.toJSONString(inno72ActivityPlan));

				String gameId = inno72ActivityPlan.getGameId();
				if (StringUtil.isEmpty(gameId)) {
					LOGGER.debug("活动计划id 【{}】 没有配置 游戏!", inno72ActivityPlan.getId());
					return Results.failure("无配置游戏!");
				}

				Inno72Game inno72Game = inno72GameMapper.selectByPrimaryKey(gameId);
				if (inno72Game == null) {
					LOGGER.debug("游戏id 【{}】 不存在!", gameId);
					return Results.failure("游戏不存在!");
				}

				BeanUtils.copyProperties(inno72Machine, inno72MachineVo);
				inno72MachineVo.setInno72Games(inno72Game);
				Inno72Activity inno72Activity = inno72ActivityMapper.selectByPrimaryKey(inno72ActivityPlan.getActivityId());
				String brandName = inno72MerchantMapper.selectBoundNameByActivityId(inno72Activity.getId());
				String sellerId = inno72Activity.getSellerId();
				Inno72Merchant inno72Merchant = inno72MerchantMapper.selectByPrimaryKey(sellerId);
				if (inno72Merchant == null) {
					LOGGER.debug("商户id 【{}】 不存在!", sellerId);
					return Results.failure("商户配置错误!");
				}
				inno72MachineVo.setChannelId(inno72Merchant.getChannelId());
				inno72MachineVo.setBrandName(brandName);
				inno72MachineVo.setActivityPlanId(inno72ActivityPlan.getId());
				inno72MachineVo.setInno72ActivityPlan(inno72ActivityPlan);
				inno72MachineVo.setActivityId(inno72ActivityPlan.getActivityId());
				inno72MachineVo.setPrizeType(inno72ActivityPlan.getPrizeType());
				inno72MachineVo.setPlanCode(inno72Activity.getCode());
				inno72MachineVo.setActivityType(Inno72MachineVo.ACTIVITYTYPE_NOTPAIYANG);
				inno72MachineVo.setActivityName(inno72Activity.getName());
			}
			inno72MachineVo.setReload(false);

			//设置机器点位信息
			Inno72AdminArea inno72AdminArea = inno72MachineMapper.findAreaByMachineCode(machineId);
			inno72MachineVo.setProvence(inno72AdminArea.getProvince());
			inno72MachineVo.setCity(inno72AdminArea.getCity());
			inno72MachineVo.setDistrict(inno72AdminArea.getDistrict());
			inno72MachineVo.setPoint(inno72AdminArea.getCircle());

			redisUtil.set(CommonBean.REDIS_ACTIVITY_PLAN_CACHE_KEY +inno72MachineVo.getActivityPlanId()+":"+ machineId,
					 JSON.toJSONString(inno72MachineVo));
		}
		return Results.success(inno72MachineVo);
	}
	@Override
	public String findActivityIdByMachineCode(String machineCode){
		Inno72Machine inno72Machine = inno72MachineMapper.findMachineByCode(machineCode);
		if (inno72Machine == null) {
			return null;
		}

		List<Inno72ActivityPlan> inno72ActivityPlans = inno72ActivityPlanMapper
				.selectByMachineId(inno72Machine.getId());
		if(inno72ActivityPlans!=null && inno72ActivityPlans.size()>0){
			return inno72ActivityPlans.get(0).getId();
		}
		//查看派样
		//查询该机器配置的商品信息（当前时间在起止时间内的）
		Inno72InteractMachine interactMachine = inno72InteractMachineTimeService.findActiveInteractMachine(machineCode);
		if(interactMachine == null){
			return null;
		}
		String interactId = interactMachine.getInteractId();
		Inno72Interact interact = inno72InteractService.findById(interactId);
		if(interact != null){
			return interact.getId();
		}
		return null;
	}

	@Override
	public Result findActivityForApp(String mid, Long _t) {

		String res = "";
		try {
			LocalDateTime now = LocalDateTime.now();

			LocalDateTime localDateTime = LocalDateTimeUtil.long2LocalDateTime(_t);
			Duration between = Duration.between(now, localDateTime);

			long minutes = between.toMinutes();
			if (minutes > 5){
				return Results.failure(AesUtils.encrypt("请求过期"));
			}
		}catch (Exception e){
			LOGGER.info("解析请求时间错误", e.getMessage());
			return Results.failure(AesUtils.encrypt("请求错误"));
		}
		return Results.success(AesUtils.encrypt(inno72InteractService.findPlanCodeByMid(mid)));
		
	}

	private Result<Inno72MachineVo> initDefaultGameFromRedis(String machineId) {

		Inno72MachineVo inno72MachineVo = null;

		if (StringUtil.isNotBlank(machineId)) {
			String rVoJson = redisUtil.get(CommonBean.REDIS_ACTIVITY_DEFAULT_PLAN_CACHE_KEY + machineId);
			LOGGER.debug("redis cache machine data =====> {}", rVoJson);
			if (StringUtil.isNotEmpty(rVoJson)) {
				inno72MachineVo = JSON.parseObject(rVoJson, Inno72MachineVo.class);
				LOGGER.debug("parse rVoJson string finish --> {}", inno72MachineVo);
			}
		}

		if (inno72MachineVo == null) {

			inno72MachineVo = new Inno72MachineVo();

			Inno72Activity activity = inno72ActivityMapper.selectDefaultAct();

			inno72MachineVo.setPlanCode(activity.getCode());
			inno72MachineVo.setActivityPlanId("0");
			inno72MachineVo.setActivityId(activity.getId());

			Inno72Game inno72Game = inno72GameMapper.selectByPrimaryKey(activity.getGameId());
			inno72MachineVo.setInno72Games(inno72Game);

			redisUtil.setex(CommonBean.REDIS_ACTIVITY_DEFAULT_PLAN_CACHE_KEY + machineId,
					CommonBean.REDIS_ACTIVITY_DEFAULT_PLAN_CACHE_EX_KEY, JSON.toJSONString(inno72MachineVo));
		}

		return Results.success(inno72MachineVo);
	}
}

