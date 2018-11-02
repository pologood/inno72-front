package com.inno72.service.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.common.util.JSR303Util;
import com.inno72.common.utils.StringUtil;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.PointService;
import com.inno72.vo.Inno72MachineInformation;
import com.inno72.vo.RequestMachineInfoVo;
import com.inno72.vo.UserSessionVo;

import jdk.nashorn.internal.ir.RuntimeNode;

@Service
public class PointServiceImpl implements PointService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PointServiceImpl.class);

	@Resource
	private IRedisUtil redisUtil;

	@Resource
	private GameSessionRedisUtil gameSessionRedisUtil;

	@Resource
	private MongoOperations mongoOperations;

	@Override
	public Result<String> information(String request) {
		LOGGER.info("information param : {}", request);
		if (StringUtil.isEmpty(request)){
			return Results.failure("参数错误!");
		}
		RequestMachineInfoVo requestMachineInfoVo = JSON.parseObject(request, RequestMachineInfoVo.class);
		Result<String> valid = JSR303Util.valid(requestMachineInfoVo);
		if (valid.getCode() == Result.FAILURE){
			return valid;
		}

		UserSessionVo sessionKey = gameSessionRedisUtil.getSessionKey(requestMachineInfoVo.getSessionUuid());

		Inno72MachineInformation info = new Inno72MachineInformation();
		BeanUtils.copyProperties(requestMachineInfoVo, info);
		exec.execute(new Task(sessionKey, info));
		return Results.success();
	}

	@Override
	public Result<String> innerPoint(String session, Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE enumInno72MachineInformationType) {
		LOGGER.info("innerPoint param : {}", session);
		UserSessionVo sessionKey = gameSessionRedisUtil.getSessionKey(session);
		if ( sessionKey == null ){
			return Results.failure("登录失效!");
		}
		exec.execute(new Task(sessionKey, new Inno72MachineInformation()));
		return Results.success();
	}

	private ExecutorService exec = Executors.newCachedThreadPool();

	private Semaphore semaphore = new Semaphore(20);

	/**
	 * TODO 创建session需提前至初始化查询机器信息接口
	 * 提交处理数据线程，保存至mongodb
	 */
	class Task implements Runnable{

		private UserSessionVo sessionKey;
		private Inno72MachineInformation info;

		public Task( UserSessionVo sessionKey,Inno72MachineInformation info) {
			this.sessionKey = sessionKey;
			this.info = info;
		}

		@Override
		public void run() {
			try {
				semaphore.acquire();
				buildBaseInfoFromSession(sessionKey, info);


			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally {
				semaphore.release();
			}
		}
	}


	private Result<Inno72MachineInformation> buildBaseInfoFromSession(UserSessionVo sessionKey, Inno72MachineInformation info) {

		String traceId = sessionKey.getTraceId();
		info.setTraceId(traceId);

		String activityId = sessionKey.getActivityId();
		String activityName = sessionKey.getInno72MachineVo().getActivityName();

		String machineCode = sessionKey.getMachineCode();

		String provence = sessionKey.getInno72MachineVo().getProvence();
		String city = sessionKey.getInno72MachineVo().getCity();
		String district = sessionKey.getInno72MachineVo().getDistrict();
		String point = sessionKey.getInno72MachineVo().getPoint();

		String userNick = sessionKey.getUserNick();
		String userId = sessionKey.getUserId();

//		String clientTime = sessionKey.getClientTime();
		String actionTime = sessionKey.getActionTime();

		String sellerId = sessionKey.getSellerId();
		String sellerName = sessionKey.getSellerName();

		String goodsCode = sessionKey.getGoodsCode();
		String goodsName = sessionKey.getGoodsName();

		String playCode = sessionKey.getPlayCode();

		return Results.success(info);
	}

	public static void lll(Inno72MachineInformation info){
		info.setShipmentNum("1111");
	}

	private Result<Inno72MachineInformation> buildOrderFromSession(UserSessionVo sessionKey, Inno72MachineInformation info) {
		String refOrderId = sessionKey.getRefOrderId();
		String inno72OrderId = sessionKey.getInno72OrderId();
		info.setRefOrderId(refOrderId);
		info.setOrderId(inno72OrderId);
		return Results.success(info);
	}
	private Result<Inno72MachineInformation> buildOrderStatusFromSession(UserSessionVo sessionKey, Inno72MachineInformation info) {
		String refOrderStatus = sessionKey.getRefOrderStatus();
		info.setRefOrderStatus(refOrderStatus);
		return Results.success(info);
	}
	private Result<Inno72MachineInformation> buildShipmentFromSession(UserSessionVo sessionKey, Inno72MachineInformation info) {
		String channelId = sessionKey.getChannelId();
		info.setChannel(channelId);
		String shipmentNum = sessionKey.getShipmentNum();
		info.setShipmentNum(shipmentNum);
		return Results.success(info);
	}

}
