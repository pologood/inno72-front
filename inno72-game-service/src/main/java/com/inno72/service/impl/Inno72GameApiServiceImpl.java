package com.inno72.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.inno72.common.Result;
import com.inno72.mapper.Inno72GameResultGoodsMapper;
import com.inno72.service.Inno72GameApiService;

@Service
public class Inno72GameApiServiceImpl implements Inno72GameApiService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72GameApiServiceImpl.class);

	@Resource
	private Inno72GameResultGoodsMapper inno72GameResultGoodsMapper;

	@Override
	public Result<String> findProduct(String machineId, String gameId, String report) {
		Map<String, String> param = new HashMap<>();
		return null;
	}

	@Override
	public Result<String> order(String sessionUuid, String activityId, String machineId, String itemId, String gameId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<String> orderPolling(String sessionUuid, String orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<String> luckyDraw(String userId, String gameId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<String> shipmentReport(String machineId, String gameId, String goodsId) {
		// TODO Auto-generated method stub
		return null;
	}

}
