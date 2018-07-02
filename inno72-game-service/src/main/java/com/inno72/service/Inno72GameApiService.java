package com.inno72.service;

import com.inno72.common.Result;

public interface Inno72GameApiService {

	Result<String> findProduct(String machineId, String gameId, String report);

	Result<String> order(String sessionUuid, String activityId, String machineId, String itemId, String gameId);

	Result<String> orderPolling(String sessionUuid, String orderId);

	Result<String> luckyDraw(String userId, String gameId);

	Result<String> shipmentReport(String machineId, String gameId, String goodsId);


}
