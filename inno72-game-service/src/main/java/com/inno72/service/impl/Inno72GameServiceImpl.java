package com.inno72.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.mapper.Inno72ActivityPlanMapper;
import com.inno72.mapper.Inno72GameMapper;
import com.inno72.mapper.Inno72GameUserChannelMapper;
import com.inno72.mapper.Inno72GameUserLifeMapper;
import com.inno72.mapper.Inno72OrderHistoryMapper;
import com.inno72.mapper.Inno72OrderMapper;
import com.inno72.model.Inno72ActivityPlan;
import com.inno72.model.Inno72Game;
import com.inno72.model.Inno72GameUserChannel;
import com.inno72.model.Inno72GameUserLife;
import com.inno72.model.Inno72Order;
import com.inno72.model.Inno72OrderHistory;
import com.inno72.service.Inno72GameService;
import com.inno72.vo.UserSessionVo;


/**
 * Created by CodeGenerator on 2018/06/27.
 */
@Service
@Transactional
public class Inno72GameServiceImpl extends AbstractService<Inno72Game> implements Inno72GameService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72GameServiceImpl.class);

	@Resource
	private Inno72GameMapper inno72GameMapper;

	@Resource
	private Inno72OrderMapper inno72OrderMapper;
	@Resource
	private Inno72OrderHistoryMapper inno72OrderHistoryMapper;
	@Resource
	private Inno72GameUserLifeMapper inno72GameUserLifeMapper;
	@Resource
	private Inno72GameUserChannelMapper inno72GameUserChannelMapper;
	@Resource
	private Inno72ActivityPlanMapper inno72ActivityPlanMapper;


	@Override
	public Result<String> updateRefOrderId(String inno72OrderId, String refId, String thirdUserId) {
		Inno72Order inno72Order = inno72OrderMapper.selectByPrimaryKey(inno72OrderId);
		inno72Order.setId(inno72OrderId);
		inno72Order.setRefOrderId(refId);
		inno72OrderMapper.updateByPrimaryKeySelective(inno72Order);

		// 插入订单历史
		Inno72OrderHistory history = new Inno72OrderHistory();
		history.setDetails("更新第三方订单号");
		history.setHistoryOrder(JSON.toJSONString(inno72Order));
		history.setOrderId(inno72Order.getId());
		history.setOrderNum(inno72Order.getOrderNum());
		inno72OrderHistoryMapper.insert(history);

		Inno72GameUserLife userLife = inno72GameUserLifeMapper.selectByUserChannelIdLast(thirdUserId);
		userLife.setOrderId(inno72OrderId);
		inno72GameUserLifeMapper.updateByPrimaryKeySelective(userLife);

		return Results.success();
	}

	@Override
	public void updateOrderReport(UserSessionVo userSessionVo) {
		// 更新订单
		String inno72OrderId = userSessionVo.getInno72OrderId();
		Inno72Order inno72Order = inno72OrderMapper.selectByPrimaryKey(inno72OrderId);
		if (inno72Order == null) {
			LOGGER.info("更新第三方订单状态时候查询的订单不存在 [{}]", inno72OrderId);
			return;
		}
		inno72Order.setId(inno72OrderId);
		inno72Order.setGoodsStatus(Inno72Order.INNO72ORDER_GOODSSTATUS.SUCC.getKey());
		inno72OrderMapper.updateByPrimaryKeySelective(inno72Order);

		// 插入订单历史
		Inno72OrderHistory history = new Inno72OrderHistory();
		history.setDetails("更新订单游戏结果");
		history.setHistoryOrder(JSON.toJSONString(inno72Order));
		history.setOrderId(inno72Order.getId());
		history.setOrderNum(inno72Order.getOrderNum());
		inno72OrderHistoryMapper.insert(history);

		Inno72GameUserLife userLife = inno72GameUserLifeMapper.selectByUserChannelIdLast(userSessionVo.getUserId());
		userLife.setGameResult("1");
		inno72GameUserLifeMapper.updateByPrimaryKeySelective(userLife);
	}

	@Override
	public boolean countSuccOrderNologin(String channelId, String activityPlanId) {

		Map<String, String> orderParams = new HashMap<>();
		orderParams.put("activityPlanId", activityPlanId);
		int totalOrders = inno72OrderMapper.findGoodsStatusSuccWithoutUserId(orderParams);

		Inno72ActivityPlan inno72ActivityPlan = inno72ActivityPlanMapper.selectByPrimaryKey(activityPlanId);
		Integer userMaxTimes = inno72ActivityPlan.getUserMaxTimes();

		orderParams.put("orderTime", "1");
		int todayOrders = inno72OrderMapper.findGoodsStatusSuccWithoutUserId(orderParams);

		Integer dayUserMaxTimes = inno72ActivityPlan.getDayUserMaxTimes();

		LOGGER.info(
				"countSuccOrderNologin  totalOrders=>{}; todayOrders => {}; dayUserMaxTimes => {}; userMaxTimes => {}",
				totalOrders, todayOrders, dayUserMaxTimes, userMaxTimes);

		return totalOrders < userMaxTimes && todayOrders < dayUserMaxTimes;
	}

	@Override
	public boolean countSuccOrder(String channelId, String channelUserKey, String activityPlanId) {
		Map<String, String> paramsChannel = new HashMap<>();
		paramsChannel.put("channelId", channelId);
		paramsChannel.put("channelUserKey", channelUserKey);
		Inno72GameUserChannel userChannel = inno72GameUserChannelMapper.selectByChannelUserKey(paramsChannel);
		String gameUserId = userChannel.getGameUserId();

		Map<String, String> orderParams = new HashMap<>();
		orderParams.put("activityPlanId", activityPlanId);
		orderParams.put("gameUserId", gameUserId);
		List<Inno72Order> inno72Orders = inno72OrderMapper.findGoodsStatusSucc(orderParams);

		Inno72ActivityPlan inno72ActivityPlan = inno72ActivityPlanMapper.selectByPrimaryKey(activityPlanId);
		Integer userMaxTimes = inno72ActivityPlan.getUserMaxTimes();

		orderParams.put("orderTime", "1");
		List<Inno72Order> todayInno72Orders = inno72OrderMapper.findGoodsStatusSucc(orderParams);
		Integer dayUserMaxTimes = inno72ActivityPlan.getDayUserMaxTimes();

		LOGGER.info("countSuccOrder   inno72Orders.size() => {} ; dayUserMaxTimes => {}; userMaxTimes => {}",
				inno72Orders.size(), dayUserMaxTimes, userMaxTimes);

		return inno72Orders.size() < userMaxTimes && todayInno72Orders.size() < dayUserMaxTimes;
	}
}
