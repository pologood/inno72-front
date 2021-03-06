package com.inno72.service;

import com.inno72.common.Result;

public interface Inno72TopService {

	/**
	 * 关注店铺
	 * @param sessionUuid
	 * @param sellerId
	 */
	void fllowshopLog(String sessionUuid, String sellerId);

	/**
	 * 抽奖
	 * @param sessionUuid
	 * @param interactId
	 * @param sellerId
	 */
	void lotteryLog(String sessionUuid, String interactId, String sellerId);

	/**
	 * 抽奖
	 * @param sessionUuid
	 * @param ua
	 * @param umid
	 * @param interactId
	 * @param shopId
	 */
	Result lottory(String sessionUuid, String ua, String umid, String interactId, String shopId);

	/**
	 * 获得脱敏名称
	 * @param sessionUuid
	 * @param sellerId
	 * @param mixNick
	 * @return
	 */
	String getMaskUserNick(String sessionUuid, String accessToken, String sellerId, String mixNick);

	/**
	 * 订单polling
	 * @return
	 */
	Result<String> orderPolling(String sessionUuid, String orderId);

}
