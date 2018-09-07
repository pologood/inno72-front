package com.inno72.service;

public interface Inno72TopService {

	/**
	 * 下单
	 */
	String order(String sessionUuid, String activityId, String itemId);

	/**
	 * polling 订单
	 */
	String orderPolling(String sessionUuid, String orderId);

	/**
	 * 抽奖
	 */
	String lottory(String sessionUuid, String ua, String umid, String interactId, String shopId);

	/**
	 * 出货
	 */
	void deliveryRecord(String sessionUuid, String channelId);
}
