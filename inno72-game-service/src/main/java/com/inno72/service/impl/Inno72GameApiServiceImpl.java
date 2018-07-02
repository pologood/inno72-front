package com.inno72.service.impl;

import org.springframework.stereotype.Service;

import com.inno72.common.Result;
import com.inno72.service.Inno72GameApiService;

@Service
public class Inno72GameApiServiceImpl implements Inno72GameApiService {

	@Override
	public Result<String> findProduct(String machineId, String gameId, String report) {
		// TODO Auto-generated method stub
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
