package com.inno72.service;

public interface Inno72TopService {

	// 关注店铺
	void fllowshopLog(String sessionUuid, String sellerId);

	// 抽奖
	void lotteryLog(String sessionUuid, String interactId, String sellerId);

}
