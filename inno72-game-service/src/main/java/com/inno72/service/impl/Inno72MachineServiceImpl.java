package com.inno72.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.*;
import com.inno72.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
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
	private Inno72MachineGameMapper inno72MachineGameMapper;
	@Resource
	private Inno72GameMapper inno72GameMapper;
	@Resource
	private Inno72GameServiceProperties inno72GameServiceProperties;
	@Resource
	private Inno72ActivityPlanMapper inno72ActivityPlanMapper;
	@Resource
	private Inno72ActivityPlanMachineMapper inno72ActivityPlanMachineMapper;
	@Resource
	private Inno72MerchantMapper inno72MerchantMapper;
	@Resource
	private Inno72ActivityMapper inno72ActivityMapper;



	@Override
	public Result<Inno72MachineVo> findGame(String machineId, String plantId, String version, String versionInno72) {
		LOGGER.info("查询售卖机游戏详情 - machineId -> {}", machineId);
		Inno72Machine inno72Machine = inno72MachineMapper.selectByPrimaryKey(machineId);
		if (inno72Machine == null) {
			return Results.failure("机器ID错误!");
		}

		List<Inno72ActivityPlan> inno72ActivityPlans = inno72ActivityPlanMapper.selectByMachineId(machineId);

		if ( inno72ActivityPlans.size() == 0 ){
			LOGGER.warn("机器 【{}】 没有配置 活动计划!", inno72Machine.getMachineCode());
			return Results.failure("无活动计划!");
		}

		Inno72ActivityPlan inno72ActivityPlan = inno72ActivityPlans.get(0);

		String gameId = inno72ActivityPlan.getGameId();
		if (StringUtil.isEmpty(gameId)){
			LOGGER.warn("活动计划id 【{}】 没有配置 游戏!", inno72ActivityPlan.getId());
			return Results.failure("无配置游戏!");
		}


		Inno72Game inno72Game = inno72GameMapper.selectByPrimaryKey(gameId);
		if (inno72Game == null){
			LOGGER.warn("游戏id 【{}】 不存在!", gameId);
			return Results.failure("游戏不存在!");
		}



		Inno72MachineVo inno72MachineVo = new Inno72MachineVo();
		BeanUtils.copyProperties(inno72Machine, inno72MachineVo);
		inno72MachineVo.setInno72Games(inno72Game);
		Inno72Activity inno72Activity = inno72ActivityMapper.selectByPrimaryKey(inno72ActivityPlan.getActivityId());
		String brandName = inno72MerchantMapper.selectBoundNameByActivityId(inno72Activity.getId());
		inno72MachineVo.setBrandName(brandName);
		inno72MachineVo.setActivityPlanId(inno72ActivityPlan.getId());
		inno72MachineVo.setInno72ActivityPlan(inno72ActivityPlan);
		if (!gameId.equals("0") && (!inno72Game.getId().equals(gameId) || !inno72Game.getVersion().equals(version)
				|| !inno72Game.getVersionInno72().equals(versionInno72))) {
			inno72MachineVo.setReload(true);
		}

		LOGGER.info("查询完成 - result -> {}", JSON.toJSONString(inno72MachineVo));

		return Results.success(inno72MachineVo);
	}

}
