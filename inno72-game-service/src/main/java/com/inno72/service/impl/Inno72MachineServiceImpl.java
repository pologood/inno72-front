package com.inno72.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.common.CommonBean;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.Inno72ActivityMapper;
import com.inno72.mapper.Inno72ActivityPlanMapper;
import com.inno72.mapper.Inno72GameMapper;
import com.inno72.mapper.Inno72MachineMapper;
import com.inno72.mapper.Inno72MerchantMapper;
import com.inno72.model.Inno72Activity;
import com.inno72.model.Inno72ActivityPlan;
import com.inno72.model.Inno72Game;
import com.inno72.model.Inno72Machine;
import com.inno72.model.Inno72Merchant;
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


	@Override
	public Result<Inno72MachineVo> findGame(String machineId, String planId, String version, String versionInno72) {
		LOGGER.info("查询售卖机游戏详情 - machineCode -> {}", machineId);

		Result<Inno72MachineVo> inno72MachineVoResult = this.initVoFromRedis(planId, machineId);
		if (inno72MachineVoResult.getCode() == Result.FAILURE){
			return inno72MachineVoResult;
		}
		if (inno72MachineVoResult.getCode() == 2){
			inno72MachineVoResult = this.initDefaultGameFromRedis(machineId);
			if (inno72MachineVoResult.getCode() != Result.SUCCESS ){
				return inno72MachineVoResult;
			}
		}

		Inno72MachineVo inno72MachineVo = inno72MachineVoResult.getData();

		if (!planId.equals("-1")
				&& (!inno72MachineVo.getActivityPlanId().equals(planId)
				|| !inno72MachineVo.getInno72Games().getVersion().equals(version)
				|| !inno72MachineVo.getInno72Games().getVersionInno72().equals(versionInno72)
				)
		) {
			LOGGER.debug("查询机器游戏关联完成 - result -> {}", JSON.toJSONString(inno72MachineVo));
			inno72MachineVo.setReload(true);
		}

		Inno72ActivityPlan inno72ActivityPlan = inno72MachineVo.getInno72ActivityPlan();

		if (inno72ActivityPlan != null){
			LocalDateTime startTime = inno72ActivityPlan.getStartTime();
			LocalDateTime endTime = inno72ActivityPlan.getEndTime();
			LocalDateTime now = LocalDateTime.now();
			if ( startTime.isBefore(now) && endTime.isAfter(now)){
				LOGGER.debug("活动过期 ==>   ", JSON.toJSONString(inno72ActivityPlan));
				inno72MachineVo.setReload(true);
				redisUtil.del(CommonBean.REDIS_ACTIVITY_PLAN_CACHE_KEY + planId + ":" +machineId);
			}
		}

		return Results.success(inno72MachineVo);
	}

	private Result<Inno72MachineVo> initVoFromRedis(String planId, String machineId){

		Inno72MachineVo inno72MachineVo = null;

		if ( StringUtil.isNotBlank(planId) ){
			String rVoJson = redisUtil.get(CommonBean.REDIS_ACTIVITY_PLAN_CACHE_KEY + planId + ":" +machineId);
			LOGGER.debug("redis cache machine data =====> {}", rVoJson);
			if ( StringUtil.isNotEmpty(rVoJson) ){
				inno72MachineVo = JSON.parseObject(rVoJson, Inno72MachineVo.class);
				LOGGER.debug("parse rVoJson string finish --> {}", inno72MachineVo);
			}
		}

		if ( inno72MachineVo == null ){

			inno72MachineVo = new Inno72MachineVo();

			Inno72Machine inno72Machine = inno72MachineMapper.findMachineByCode(machineId);
			if (inno72Machine == null) {
				return Results.failure("机器code错误!");
			}

			List<Inno72ActivityPlan> inno72ActivityPlans = inno72ActivityPlanMapper.selectByMachineId(inno72Machine.getId());

			if (inno72ActivityPlans.size() == 0) {
				LOGGER.debug("机器 【{}】 没有配置 活动计划!", inno72Machine.getMachineCode());
				return Results.warn("无活动计划!",2);
			}

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
			inno72MachineVo.setReload(false);
			inno72MachineVo.setPrizeType(inno72ActivityPlan.getPrizeType());
			inno72MachineVo.setPlanCode(inno72Activity.getCode());

			redisUtil.setex(
					CommonBean.REDIS_ACTIVITY_PLAN_CACHE_KEY + inno72ActivityPlan.getId()+ ":" +machineId,
					CommonBean.REDIS_ACTIVITY_PLAN_CACHE_EX_KEY,
					JSON.toJSONString(inno72MachineVo)
			);
		}
		return Results.success(inno72MachineVo);
	}

	private Result<Inno72MachineVo> initDefaultGameFromRedis(String machineId){

		Inno72MachineVo inno72MachineVo = null;

		if ( StringUtil.isNotBlank(machineId) ){
			String rVoJson = redisUtil.get(CommonBean.REDIS_ACTIVITY_DEFAULT_PLAN_CACHE_KEY + machineId);
			LOGGER.debug("redis cache machine data =====> {}", rVoJson);
			if ( StringUtil.isNotEmpty(rVoJson) ){
				inno72MachineVo = JSON.parseObject(rVoJson, Inno72MachineVo.class);
				LOGGER.debug("parse rVoJson string finish --> {}", inno72MachineVo);
			}
		}

		if ( inno72MachineVo == null ) {

			inno72MachineVo = new Inno72MachineVo();

			Inno72Activity activity = inno72ActivityMapper.selectDefaultAct();

			inno72MachineVo.setPlanCode(activity.getCode());
			inno72MachineVo.setActivityPlanId("0");
			inno72MachineVo.setActivityId(activity.getId());

			Inno72Game inno72Game = inno72GameMapper.selectByPrimaryKey(activity.getGameId());
			inno72MachineVo.setInno72Games(inno72Game);

			redisUtil.setex(CommonBean.REDIS_ACTIVITY_DEFAULT_PLAN_CACHE_KEY + machineId, CommonBean.REDIS_ACTIVITY_DEFAULT_PLAN_CACHE_EX_KEY,
					JSON.toJSONString(inno72MachineVo));
		}

		return Results.success(inno72MachineVo);
	}
}

