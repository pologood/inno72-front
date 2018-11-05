package com.inno72.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
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
import com.inno72.common.CommonBean;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.datetime.LocalDateTimeUtil;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.common.util.JSR303Util;
import com.inno72.common.utils.StringUtil;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.PointService;
import com.inno72.vo.Inno72MachineInformation;
import com.inno72.vo.Inno72MachineVo;
import com.inno72.vo.Inno72TaoBaoCheckDataVo;
import com.inno72.vo.RequestMachineInfoVo;
import com.inno72.vo.UserSessionVo;

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

		Inno72MachineInformation info = new Inno72MachineInformation();
		BeanUtils.copyProperties(requestMachineInfoVo, info);
		exec.execute(new Task(info.build()));
		return Results.success();
	}

	@Override
	public Result<String> innerPoint(String session, Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE enumInno72MachineInformationType) {
		LOGGER.info("innerPoint param : {}", session);
		exec.execute(new Task(new Inno72MachineInformation(enumInno72MachineInformationType.getType(), session)));
		return Results.success();
	}

	@Override
	public Result<String> innerTaoBaoDataSyn(Inno72TaoBaoCheckDataVo vo) {
		Result<String> valid = JSR303Util.valid(vo);
		if (valid.getCode() == Result.FAILURE){
			return valid;
		}
		exec.execute(() -> {
			try {
				semaphore.acquire();
				String sessionUuid = vo.getSessionUuid();
				UserSessionVo sessionKey = gameSessionRedisUtil.getSessionKey(sessionUuid);
				Inno72TaoBaoCheckDataVo inno72TaoBaoCheckDataVo = buildTaoBaoSynBody(sessionKey, vo);

				mongoOperations.save(inno72TaoBaoCheckDataVo, "Inno72TaoBaoCheckData");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				semaphore.release();
			}
		});
		return Results.success();
	}

	private Inno72TaoBaoCheckDataVo buildTaoBaoSynBody(UserSessionVo sessionKey, Inno72TaoBaoCheckDataVo vo) {

		String traceId = sessionKey.getTraceId();

		String activityId = sessionKey.getActivityId();
		String activityName = sessionKey.getInno72MachineVo().getActivityName();

		String provence = sessionKey.getInno72MachineVo().getProvence();
		String city = sessionKey.getInno72MachineVo().getCity();
		String district = sessionKey.getInno72MachineVo().getDistrict();
		String point = sessionKey.getInno72MachineVo().getPoint();
		String playCode = sessionKey.getPlanCode();

		String userNick = Optional.ofNullable(sessionKey.getUserNick()).orElse("");
		String userId = Optional.ofNullable(sessionKey.getUserId()).orElse("");
		String sellerId = Optional.ofNullable(sessionKey.getSellerId()).orElse("");
		String sellerName = Optional.ofNullable(sessionKey.getSellerName()).orElse("");
		String goodsCode = Optional.ofNullable(sessionKey.getGoodsCode()).orElse("");
		String goodsName = Optional.ofNullable(sessionKey.getGoodsName()).orElse("");

		//		String traceId, String activityId, String activityName, String provence, String city,
		//		String district, String point, String userId, String nickName, String sellerId,
		//		String sellerName, String goodsId, String goodsName, String playCode
		return vo
				.buildBaseInformation(traceId, activityId, activityName, provence, city, district, point, userId,
						userNick, sellerId, sellerName, goodsCode, goodsName, playCode, vo);

	}

	private ExecutorService exec = Executors.newCachedThreadPool();

	private Semaphore semaphore = new Semaphore(20);

	/**
	 * 提交处理数据线程，保存至mongodb
	 */
	class Task implements Runnable{

		private Inno72MachineInformation info;

		Task(Inno72MachineInformation info) {
			this.info = info;
		}

		@Override
		public void run() {
			try {
				semaphore.acquire();
				String sessionUuid = info.getSessionUuid();
				UserSessionVo sessionKey = gameSessionRedisUtil.getSessionKey(sessionUuid);
				buildBaseInfoFromSession(sessionKey, info);
				buildElseInfo(sessionKey, info);
				mongoOperations.save(info,"Inno72MachineInformation");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally {
				semaphore.release();
			}
		}
	}

	private void buildElseInfo(UserSessionVo sessionKey, Inno72MachineInformation info){
		String type = info.getType();
		if (type.equals(Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE.SCAN_LOGIN.getType())){
			//添加登录扫码路径
			buildScanLoginFromSession(sessionKey, info);
		}else if(type.equals(Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE.SCAN_PAY.getType())){
			//添加支付扫码路径
			buildScanPayFromSession(sessionKey, info);
		}else if(type.equals(Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE.SHIPMENT.getType())){
			//添加出货 货道号 商品 出货数量
			buildShipmentFromSession(sessionKey, info);
		}else if(type.equals(Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE.ORDER_GOODS.getType())){
			//添加下单 订单号 三方订单号
			buildOrderFromSession(sessionKey, info);
		}else if(type.equals(Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE.ORDER_COUPON.getType())){
			//添加优惠券 奖池ID 抽奖结果
			buildCouponFromSession(sessionKey, info);
		}else if(type.equals(Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE.PAY.getType())){
			//添加商品订单支付 结果
			buildPayFromSession(sessionKey, info);
		}else if(type.substring(0, 3).equals(Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE.CLICK.getType())){
			if (StringUtil.isEmpty(info.getClickType())){
				info.setClickType("0");
			}
		}

	}

	private void buildBaseInfoFromSession(UserSessionVo sessionKey, Inno72MachineInformation info) {

		String activityId;
		String activityName;
		String point;
		String city;
		String provence;
		String machineCode;
		String district;
		String playCode;

		if ( sessionKey == null ){
			String inno72MachineVoStr = redisUtil
					.get(CommonBean.REDIS_ACTIVITY_PLAN_CACHE_KEY + info.getPlanId() + ":" + info.getSessionUuid());
			if (StringUtil.isEmpty(inno72MachineVoStr)){
				Results.failure("失败");
				return;
			}

			Inno72MachineVo inno72MachineVo = JSON.parseObject(inno72MachineVoStr, Inno72MachineVo.class);
			activityName = inno72MachineVo.getActivityName();
			activityId = inno72MachineVo.getActivityId();
			point = inno72MachineVo.getPoint();
			city = inno72MachineVo.getCity();
			provence = inno72MachineVo.getProvence();
			district = inno72MachineVo.getDistrict();
			machineCode = inno72MachineVo.getMachineCode();
			playCode = inno72MachineVo.getPlayCode();

		}else{
			String traceId = sessionKey.getTraceId();
			info.setTraceId(traceId);

			activityId = sessionKey.getActivityId();
			activityName = sessionKey.getInno72MachineVo().getActivityName();

			machineCode = sessionKey.getMachineCode();

			provence = sessionKey.getInno72MachineVo().getProvence();
			city = sessionKey.getInno72MachineVo().getCity();
			district = sessionKey.getInno72MachineVo().getDistrict();
			point = sessionKey.getInno72MachineVo().getPoint();
			playCode = sessionKey.getPlanCode();

			String userNick = Optional.ofNullable(sessionKey.getUserNick()).orElse("");
			String userId = Optional.ofNullable(sessionKey.getUserId()).orElse("");
			info.setUserId(userId);
			info.setNickName(userNick);
			String sellerId = Optional.ofNullable(sessionKey.getSellerId()).orElse("");
			String sellerName = Optional.ofNullable(sessionKey.getSellerName()).orElse("");
			info.setSellerId(sellerId);
			info.setSellerName(sellerName);
			String goodsCode = Optional.ofNullable(sessionKey.getGoodsCode()).orElse("");
			String goodsName = Optional.ofNullable(sessionKey.getGoodsName()).orElse("");
			info.setGoodsId(goodsCode);
			info.setGoodsName(goodsName);

		}

		info.setPlayCode(playCode);
		info.setActivityName(activityName);
		info.setActivityId(activityId);
		info.setPoint(point);
		info.setCity(city);
		info.setProvence(provence);
		info.setMachineCode(machineCode);
		info.setDistrict(district);
		info.setServiceTime(LocalDateTimeUtil
				.transfer(LocalDateTime.now(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS")));

		Results.success(info);
	}

	private void buildOrderFromSession(UserSessionVo sessionKey, Inno72MachineInformation info) {
		String refOrderId = sessionKey.getRefOrderId();
		String inno72OrderId = sessionKey.getInno72OrderId();
		info.setRefOrderId(refOrderId);
		info.setOrderId(inno72OrderId);
	}
	private void buildCouponFromSession(UserSessionVo sessionKey, Inno72MachineInformation info) {
		info.setInteractId(sessionKey.getInteractId());
		info.setOrderId(sessionKey.getInno72CouponOrderId());
	}

	private void buildShipmentFromSession(UserSessionVo sessionKey, Inno72MachineInformation info) {
		String channelId = sessionKey.getChannelId();
		info.setChannel(channelId);
		String shipmentNum = sessionKey.getShipmentNum();
		info.setShipmentNum(shipmentNum);
	}
	private void buildScanLoginFromSession(UserSessionVo sessionKey, Inno72MachineInformation info) {
		info.setScanUrl(sessionKey.getScanLoginUrl());
	}
	private void buildScanPayFromSession(UserSessionVo sessionKey, Inno72MachineInformation info) {
		info.setScanUrl(sessionKey.getScanPayUrl());
	}
	private void buildPayFromSession(UserSessionVo sessionKey, Inno72MachineInformation info) {
		info.setOrderId(sessionKey.getInno72OrderId());
		info.setRefOrderStatus(sessionKey.getRefOrderStatus());
	}

}
